package com.kafka.service;

import com.kafka.constant.KafkaConstants;
import com.kafka.controller.ProducerController;
import com.kafka.modal.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.*;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    Logger LOG = LoggerFactory.getLogger(ConsumerService.class);


    @RetryableTopic(
            attempts = "4",
            exclude = {NullPointerException.class, RuntimeException.class},
            backoff = @Backoff(
                    delay = 3000,
                    multiplier = 1.5,
                    maxDelay = 15000)
    ) // Work n-1, create new topics.
    @KafkaListener(
            topics = KafkaConstants.UPDATE_USER_LOCATION_TOPIC,
            groupId = "consumer-group-1",
            topicPartitions = {@TopicPartition(
                    topic = KafkaConstants.UPDATE_USER_LOCATION_TOPIC,
                    partitions = {"0", "1", "2"})})
    public void consume1(User user,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.OFFSET) String offset) {
        LOG.info("consume1 - User <{}> Received <{}> offset <{}>", user.toString(), topic, offset);
        if (user.getId().contains("spy")) {
            LOG.info("consume1 - Spy user <{}>", user.getId());
            throw new RuntimeException("Invalid user:" + user.getId());
        }
        LOG.info("consume1 - Consumed <{}>", user.toString());
    }

    @DltHandler
    public void deadLatterTopic(User user,
                                @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                @Header(KafkaHeaders.OFFSET) String offset) {
        LOG.info("deadLatterTopic - User <{}> Received <{}> offset <{}>", user.toString(), topic, offset);


    }

//    @KafkaListener(topics = KafkaConstants.UPDATE_USER_LOCATION_TOPIC, groupId = "consumer-group-1")
//    public void consume2(Object message) {
//        LOG.info("consume - Consumed <{}>", message);
//    }
//
//    @KafkaListener(topics = KafkaConstants.UPDATE_USER_LOCATION_TOPIC, groupId = "consumer-group-1")
//    public void consume3(Object message) {
//        LOG.info("consume3 - Consumed <{}>", message);
//    }
}
