package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.messaging;

import com.app.common.events.EnrollmentAssignedEvent;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.OutboxEventJpaEntity;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.enums.OutboxStatusType;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository.OutboxEventJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Clock;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alonso
 */

@Component
public class OutboxProcessor {
  
  private static final Logger log = LoggerFactory.getLogger(OutboxProcessor.class);
  
  private final OutboxEventJpaRepository repository;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final Clock clock;
  private final ObjectMapper objectMapper;
  
  private final String ENROLLMENT_ASSIGNED_TOPIC;
  
  public OutboxProcessor(OutboxEventJpaRepository repository,
    KafkaTemplate<String, String> kafkaTemplate, Clock clock, ObjectMapper objectMapper,
    @Value("${topic.enrollment-events}")
    String enrollmentAssignedTopic) {
    this.repository = repository;
    this.kafkaTemplate = kafkaTemplate;
    this.clock = clock;
    this.objectMapper = objectMapper;
    ENROLLMENT_ASSIGNED_TOPIC = enrollmentAssignedTopic;
  }
  
  @Scheduled(fixedDelay = 5000)
  @Transactional
  public void processPendingEvents() {
    
    List<OutboxEventJpaEntity> events =
      repository.findTop100ByStatusOrderByCreatedAtAsc(OutboxStatusType.PENDING);
    
    if (!events.isEmpty()) {
      log.info("Processing {} pending outbox events", events.size());
    }
    
    for (OutboxEventJpaEntity event : events) {
      try {
        String topic = ENROLLMENT_ASSIGNED_TOPIC;
        
        EnrollmentAssignedEvent payloadEvent = objectMapper.readValue(event.getPayload(), EnrollmentAssignedEvent.class);
        
        String integrationPayload = objectMapper.writeValueAsString(payloadEvent);
        
        kafkaTemplate.send(topic, event.getAggregateId(), integrationPayload)
          .get();
        
        event.setStatus(OutboxStatusType.SENT);
        
        event.setProcessedAt(clock.instant());
        
        repository.save(event);
        
      } catch (Exception ex) {
        event.setStatus(OutboxStatusType.FAILED);
        event.setProcessedAt(clock.instant());
        repository.save(event);
        log.error("Failed to publish outbox event {}: {}", event.getId(), ex.getMessage());
      }
    }
  }
  
  @Scheduled(fixedDelay = 20000)
  @Transactional
  public void retryFailedEvents() {
    List<OutboxEventJpaEntity> failedEvents =
      repository.findTop100ByStatusOrderByCreatedAtAsc(OutboxStatusType.FAILED);
    
    for (OutboxEventJpaEntity event : failedEvents) {
      try {
        kafkaTemplate.send(ENROLLMENT_ASSIGNED_TOPIC, event.getAggregateId(), event.getPayload())
          .get();
        
        event.setStatus(OutboxStatusType.SENT);
        event.setProcessedAt(clock.instant());
        repository.save(event);
        
      } catch (Exception e) {
        log.error("Send message Failed {}", e.getMessage());
      }
    }
  }
}
