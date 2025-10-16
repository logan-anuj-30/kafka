package com.kafka.controller;


import com.kafka.constant.KafkaConstants;
import com.kafka.modal.User;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("producer")
public class ProducerController {

    Logger LOG = LoggerFactory.getLogger(ProducerController.class);

    // This is a connection to kafka cluster
    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("produce")
    public ResponseEntity<String> produce(@RequestBody() User user) {
        try {
            // From Producer, we are sending the byte array to the topic
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(KafkaConstants.UPDATE_USER_LOCATION_TOPIC, user);
            future.whenComplete((result, exception) -> {
                if (exception != null) {
                    LOG.warn("produce - Exception <{}> occurred", ExceptionUtils.getStackTrace(exception));
                    return;
                }
                LOG.info("produce - Successfully produced <{}> to offset <{}> partition <{}>",
                        result.getProducerRecord().value(), result.getRecordMetadata().offset(), result.getRecordMetadata().partition());
            });

        } catch (Exception exception) {
            LOG.error("produce - Exception <{}> occurred", ExceptionUtils.getStackTrace(exception));
        }
        return ResponseEntity.status(HttpStatus.OK).body("Message Published: " + user);
    }
}
