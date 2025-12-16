package com.app.enrollment.system.enrollment.server.domain.event;

import java.time.Instant;

/**
 * @author Alonso
 */
public interface DomainEvent {
  String aggregateType();
  String aggregateId();
  Instant occurredAt();
  EventType eventType();
}
