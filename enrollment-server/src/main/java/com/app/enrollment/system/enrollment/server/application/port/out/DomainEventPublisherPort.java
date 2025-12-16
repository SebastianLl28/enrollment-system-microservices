package com.app.enrollment.system.enrollment.server.application.port.out;

import com.app.enrollment.system.enrollment.server.domain.event.DomainEvent;

/**
 * @author Alonso
 */
public interface DomainEventPublisherPort {
  void publish(DomainEvent event);
}
