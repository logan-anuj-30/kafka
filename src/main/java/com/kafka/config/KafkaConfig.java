package com.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic newTopic() {
        NewTopic topic = new NewTopic("rider-location-update-topic", 1, (short) 1);
        topic.configs(Map.of());
        return topic;
    }
}
