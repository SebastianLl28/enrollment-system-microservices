package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.event;

import com.app.common.annotation.Adapter;
import com.app.enrollment.system.enrollment.server.application.dto.command.SendEnrollmentEvent;
import com.app.enrollment.system.enrollment.server.application.port.out.OutboxEventPort;
import com.app.enrollment.system.enrollment.server.domain.event.EventType;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.UserID;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.entity.OutboxEventJpaEntity;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.persistence.jpa.repository.OutboxEventJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Alonso
 */
@Adapter
public class OutboxEventJpaAdapter implements OutboxEventPort {
  
  private final OutboxEventJpaRepository outboxEventJpaRepository;
  private final ObjectMapper objectMapper;
  
  public OutboxEventJpaAdapter(OutboxEventJpaRepository outboxEventJpaRepository,
    ObjectMapper objectMapper) {
    this.outboxEventJpaRepository = outboxEventJpaRepository;
    this.objectMapper = objectMapper;
  }
  
  @Override
  public void saveEvent(SendEnrollmentEvent event) {
    try {
      String json = objectMapper.writeValueAsString(event.payload());
      OutboxEventJpaEntity entity = new OutboxEventJpaEntity();
      
      entity.setAggregateType(event.aggregateType());
      entity.setAggregateId(event.aggregateId());
      entity.setEventType(event.eventType());
      entity.setPayload(json);
      entity.setUserId(event.userID().getValue());
      entity.setEnrollmentStatus(event.enrollmentStatus());
      entity.setCourseId(event.course().getId().getValue());
      entity.setStudentId(event.student().getId().getValue());
      
      entity.setRetryCount(entity.getRetryCount() + 1);
      
      outboxEventJpaRepository.save(entity);
    } catch (Exception e) {
      throw new RuntimeException("Error serializing outbox payload", e);
    }
  }
}
