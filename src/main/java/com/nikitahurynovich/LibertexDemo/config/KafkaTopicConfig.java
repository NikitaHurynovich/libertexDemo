package com.nikitahurynovich.LibertexDemo.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {

  @Value(value = "${spring.kafka.streams.bootstrap-servers}")
  private String bootstrapAddress;

  @Value(value = "${kafka.transactions.topic.name}")
  private String transactionTopicName;

  @Value(value = "${kafka.transactions.dlq.topic.name}")
  private String transactionDLQTopicName;

  @Value(value = "${kafka.wallet.topic.name}")
  private String walletTopicName;

  @Value(value = "${kafka.transactions.partitions}")
  private Integer transactionsPartitions;
  private final static Integer DEFAULT_PARTITION = 1;

  @Bean
  public KafkaAdmin kafkaAdmin() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    return new KafkaAdmin(configs);
  }

  @Bean
  public NewTopic transactionsTopic() {
    return new NewTopic(transactionTopicName, transactionsPartitions, (short) 1);
  }

  @Bean
  public NewTopic transactionDLQTopic() {
    return new NewTopic(transactionDLQTopicName, DEFAULT_PARTITION, (short) 1);
  }

  @Bean
  public NewTopic walletTopic() {
    return new NewTopic(walletTopicName, DEFAULT_PARTITION, (short) 1);
  }

}
