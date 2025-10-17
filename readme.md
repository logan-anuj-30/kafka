
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
``` 
  Kafka may call your partition(...) method more than once per record.

✅ Why?

Kafka first calls partition() to select a partition.

Then, internally Kafka may call it again during record serialization / batching to validate or re-evaluate the partition before send.

✅ So 2 calls per message is expected behavior.
It does NOT send the record twice — it only picks the partition twice.
```
 - 


