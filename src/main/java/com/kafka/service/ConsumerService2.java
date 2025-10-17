package com.kafka.service;

import com.kafka.constant.KafkaConstants;
import com.kafka.modal.User;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService2 implements ConsumerSeekAware {

    Logger LOG = LoggerFactory.getLogger(ConsumerService2.class);


    @RetryableTopic(
            attempts = "4", // total number of tries including the first attempt
            backoff = @Backoff(
                    delay = 3000,
                    multiplier = 1.5,
                    maxDelay = 15000),
            exclude = {NullPointerException.class}
    )
    @KafkaListener(
            topics = KafkaConstants.UPDATE_USER_LOCATION_TOPIC,
            groupId = KafkaConstants.UPDATE_USER_LOCATION_CONSUMER_GROUP,
            topicPartitions = {
                    @TopicPartition(
                            topic = KafkaConstants.UPDATE_USER_LOCATION_TOPIC,
                            partitions = {"0", "1", "2"}
                    )
            },
            concurrency = "3"   // Spring Boot will create 3 consumer threads, each assigned to one partition:
    )
    public void consume1(User user,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.OFFSET) String offset) {
        try {

            String threadName = Thread.currentThread().getName();
        LOG.info("consume1 - Started User <{}> at topic <{}> with offset <{}> by consumer <{}>",
                user.toString(), topic, offset, threadName);
        if (user.getId().contains("spy")) {
            LOG.info("consume1 - Spy user <{}>", user.getId());
            throw new RuntimeException("Invalid user:" + user.getId());
        }
//            Thread.currentThread().sleep(10000);
        } catch (Exception e) {
            LOG.warn("consume1 - Exception <{}> occurred", ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }
        LOG.info("consume1 - Finished user <{}> by thread <{}>", user.toString());
    }
}
