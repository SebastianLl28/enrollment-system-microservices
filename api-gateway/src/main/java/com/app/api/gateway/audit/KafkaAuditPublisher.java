package com.app.api.gateway.audit;

import com.app.api.gateway.port.AuditPublisher;
import com.app.common.events.AuditEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


/**
 * @author Alonso
 */
@Component
public class KafkaAuditPublisher implements AuditPublisher {
  
  private static final Logger log = LoggerFactory.getLogger(KafkaAuditPublisher.class);
  
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;
  private final String topic;
  
  public KafkaAuditPublisher(
    KafkaTemplate<String, String> kafkaTemplate,
    ObjectMapper objectMapper,
    @Value("${topic.enrollment-events}") String topic
  ) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
    this.topic = topic;
  }
  
  @Override
  public void publish(AuditEvent event) {
    try {
      String payload = objectMapper.writeValueAsString(event);
      String key = UUID.randomUUID().toString();
      kafkaTemplate.send(topic, key, payload);
    } catch (Exception e) {
      log.error("Error enviando auditoría a Kafka", e);
    }
  }
}
