package com.app.enrollment.system.event.store.server.service;

import com.app.enrollment.system.event.store.server.domain.DomainEvent;
import com.app.enrollment.system.event.store.server.persistence.DomainEventEntity;
import com.app.enrollment.system.event.store.server.persistence.DomainEventRepository;
import org.springframework.stereotype.Service;

/**
 * @author Alonso
 */
@Service
public class DomainEventService {
  
  private final DomainEventRepository repository;
  
  public DomainEventService(DomainEventRepository repository) {
    this.repository = repository;
  }
  
  public void save(DomainEvent event) {
    DomainEventEntity entity = new DomainEventEntity(
      event.getServiceName(),
      event.getPath(),
      event.getMethod(),
      event.getUri(),
      event.getUserId(),
      event.getStatus(),
      event.getOccurredAt(),
      event.getReceivedAt(),
      event.getPayloadJson()
    );
    
    repository.save(entity);
  }
}
