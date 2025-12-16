package com.app.enrollment.system.event.store.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alonso
 */
@Configuration
public class KafkaAuditSettingsConfig {
  
  @Bean(name = "auditTopic")
  public String auditTopic() {
    return env("KAFKA_AUDIT_LOGS_TOPIC", "audit-logs");
  }
  
  @Bean(name = "auditConsumerGroupId")
  public String auditConsumerGroupId() {
    return env("KAFKA_CONSUMER_GROUP_ID", "event-store-consumer");
  }
  
  @Bean(name = "kafkaBootstrapServers")
  public String kafkaBootstrapServers() {
    return env("KAFKA_BOOTSTRAP_SERVERS", "kafka:9092");
  }
  
  private static String env(String key, String def) {
    String v = System.getenv(key);
    return (v == null || v.isBlank()) ? def : v;
  }
}
