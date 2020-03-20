package com.softserve.identityservice.configuration;

import com.softserve.identityservice.model.EmailVerificationDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfiguration {
    @Value("${kafka.server}")
    private String kafkaServer;
    @Value("${kafka.producer.id}")
    private String kafkaProducerId;

    @Bean
    public Map<String, Object> props(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProducerId);
        return props;
    }

    @Bean
    public ProducerFactory<UUID, EmailVerificationDto> producerFactory(){
        return new DefaultKafkaProducerFactory<>(props());
    }

    @Bean
    public KafkaTemplate<UUID, EmailVerificationDto> kafkaTemplate(){
        KafkaTemplate<UUID,EmailVerificationDto> emailVerificationTemplate =
                new KafkaTemplate<>(producerFactory());
        emailVerificationTemplate.setMessageConverter(new StringJsonMessageConverter());
        return emailVerificationTemplate;
    }

    @Bean
    public StringJsonMessageConverter messageConverter(){
        return new StringJsonMessageConverter();
    }
}
