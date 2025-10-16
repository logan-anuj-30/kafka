
### Kafka View UI
- Install Docker 
- https://github.com/redpanda-data/console
- docker run --network=host -p 8080:8080 -e KAFKA_BROKERS=localhost:9092 docker.redpanda.com/redpandadata/console:latest

### Kafka Command
- KAFKA_CLUSTER_ID="$(bin/kafka-storage.sh random-uuid)"

### Create Logs path
- bin/kafka-storage.sh format --standalone -t $KAFKA_CLUSTER_ID -c config/server.properties

### Start the server
- bin/kafka-server-start.sh config/server.properties

### Create a Topic:
- bin/kafka-topics.sh --create --topic update-user-location-topic --bootstrap-server localhost:9092 

### Description of topic: <br>
- bin/kafka-topics.sh --describe --topic update-user-location-topic --bootstrap-server localhost:9092

### Write to topic:
- bin/kafka-console-producer.sh --topic user-location-events --bootstrap-server localhost:9092

### Read From Topic:
- bin/kafka-console-consumer.sh --topic update-user-location-topic --from-beginning --bootstrap-server localhost:9092
