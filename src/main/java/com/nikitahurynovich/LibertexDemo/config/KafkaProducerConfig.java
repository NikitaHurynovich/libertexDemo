package com.nikitahurynovich.LibertexDemo.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import com.nikitahurynovich.LibertexDemo.entity.transaction.TransactionInput;
import com.nikitahurynovich.LibertexDemo.entity.transaction.TransactionOutput;

@Configuration
public class KafkaProducerConfig {
  @Value(value = "${spring.kafka.streams.bootstrap-servers}")
  private String bootstrapAddress;

  @Bean
  public ProducerFactory<String, TransactionInput> transactionProducerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, TransactionInput> transactionKafkaTemplate() {
    return new KafkaTemplate<>(transactionProducerFactory());
  }

  @Bean
  public ProducerFactory<String, TransactionOutput> walletProducerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, TransactionOutput> walletKafkaTemplate() {
    return new KafkaTemplate<>(walletProducerFactory());
  }

}
