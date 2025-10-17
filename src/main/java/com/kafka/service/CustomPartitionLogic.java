package com.kafka.service;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.logging.Logger;

public class CustomPartitionLogic implements Partitioner {

    org.slf4j.Logger LOG = LoggerFactory.getLogger(CustomPartitionLogic.class);

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        int partitions = cluster.availablePartitionsForTopic(topic).size();
        if(((String) key).contains("VIP")) {
            LOG.info("partition - producing to zero partition as user is VIP");
            return 0;
        }
        int p=  Math.abs(key.hashCode()) % partitions;
        LOG.info("partition - producing to partition <{}>", p);
        return p;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
