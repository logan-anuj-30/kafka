package com.kafka.service;

import com.kafka.constant.KafkaConstants;
import com.kafka.controller.ProducerController;
import com.kafka.modal.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    Logger LOG = LoggerFactory.getLogger(ConsumerService.class);

    @KafkaListener(topics = KafkaConstants.UPDATE_USER_LOCATION_TOPIC, groupId = "consumer-group-1")
    public void consume1(User user) {
        LOG.info("consume1 - Consumed <{}>", user.toString());
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
