
### Kafka View UI
- Kafka Offser 3
### Kafka Command
- KAFKA_CLUSTER_ID="$(bin/kafka-storage.sh random-uuid)"

### Create Logs path
- bin/kafka-storage.sh format --standalone -t $KAFKA_CLUSTER_ID -c config/server.properties

### Start the server
- bin/kafka-server-start.sh config/server.properties

### Create a Topic:
- bin/kafka-topics.sh --create --topic update-user-location-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

### Alter a Topic:
- bin/kafka-topics.sh --alter --topic update-user-location-topic --bootstrap-server localhost:9092 --partitions 3

### Delete a topic
- bin/kafka-topics.sh --delete --topic update-user-location-topic --bootstrap-server localhost:9092



### Description of topic: <br>
- bin/kafka-topics.sh --describe --topic update-user-location-topic --bootstrap-server localhost:9092

### Description on Partition:
- kafka-topics.sh --describe --topic your-topic-name --bootstrap-server localhost:9092

### Write to topic:
- bin/kafka-console-producer.sh --topic user-location-events --bootstrap-server localhost:9092

### Read From Topic:
- bin/kafka-console-consumer.sh --topic update-user-location-topic --from-beginning --bootstrap-server localhost:9092


# Notes
- If there are more consumers than the partition, then some of the consumer will sit ideal
- Each consumer needs to be attached to at least one consumer group
- Producer can produce to a specific partition of a topic
- Consumer can consume from a specific partition of a topic,
- How kafka route messages to partitions: partition = hash(key) % number_of_partitions _**only when the kew is not null**_
- When key = null, Kafka does NOT hash — it uses a different strategy based on the configured partitioner.Key = null → Random sticky selection per batch, then rotated. Better distribution but still may look like “always same” during small number of messages.
- Kafka may call your partition(...) method more than once per record.<br>
✅ Why?<br>
Kafka first calls partition() to select a partition.  <br>
Then, internally Kafka may call it again during record serialization / batching to validate or re-evaluate the partition before send.<br>
<br>
✅ So 2 calls per message is expected behavior.<br>
It does NOT send the record twice — it only picks the partition twice.<br>
 - 1 partition → consumed by exactly 1 consumer (in a group).  
Kafka ensures ordering within a partition. Kafka will NOT assign the same partition to multiple consumers in the same group


## Ways to Scale Consumers
- Run more instances of your consumer service with the same group.id. <br>
Example: Topic: 6 partitions <br>
Deploy 6 instances of consumer (same group.id) <br>
✅ Full parallelism (1 instance per partition) <br>
If you deploy 10 instances → 4 will be idle.<br>


## How does Kafka decide where a consumer should start reading?
- Kafka tracks offsets per (topic, partition, consumer group).
- When a consumer in a group reads message offset 5, and commits it, Kafka stores this offset in the __consumer_offsets internal topic.
- If the same consumer group restarts, Kafka looks at the last committed offset and continues from there.

## Offset committing strategy matters
### Auto-commit (default)
- Kafka commits offsets every 5 seconds automatically.
```
enable.auto.commit = true
auto.commit.interval.ms = 5000
```
- ⚠ Problem: If the consumer crashes after reading but before 5s → message reprocessed (duplicate).

### Manual commit (recommended in real systems)
- You commit only after successful processing → safer & more controlled.

### When will Kafka NOT remember?
- Only in these cases:
- ❌ You change group.id → treated as a new consumer group
- ❌ You never commit offsets → Kafka has nothing to remember
- ❌ Offsets got expired (default offset retention = 7 days)


### Catches
- Messages are going to retry topics even without exceptions because Spring Kafka thinks delivery failed — usually due to:
  1. Long processing exceeding **max.poll.interval.ms** <br>
  2. Blocking threads causing perceived unresponsiveness


### Idempotency and Deduplication
- Lag: You are consuming a message (User user) and performing an action (updating location). If the consumer fails after the update but before the offset is committed, the message will be replayed (due to retries or Kafka rebalancing). This is a "at-least-once" scenario.
- Discussion Point: The interviewer will ask: "How do you ensure this update is idempotent?" Your answer should cover:
- External System Check: "The downstream service (database) needs to handle idempotency by checking the message ID or the user ID combined with a timestamp/version before applying the update."
- Kafka Offset: "We could store the last processed Kafka offset in the database alongside the user record, only processing the message if the incoming offset is newer than the stored one."


```
1. High Water Mark (HWM) = The highest replicated offset

Kafka is a distributed system—each partition has:

1 Leader replica

0 or more Follower replicas

Each follower replicates data from the leader.
A message is considered committed only when it is written on the leader AND replicated to the required number of followers (based on acks setting).

👉 The High Water Mark is the largest offset that has been replicated to all in-sync replicas (ISR).
```

## ISR
- ISR is the set of replicas that have almost the same data as the leader (i.e., they are "in sync").


