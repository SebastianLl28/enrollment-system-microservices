package com.app.enrollment.system.event.store.server.kafka;

import com.app.common.events.AuditEvent;
import com.app.enrollment.system.event.store.server.domain.DomainEvent;
import com.app.enrollment.system.event.store.server.service.DomainEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Clock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class AuditEventListener {
  
  private static final Logger log = LoggerFactory.getLogger(AuditEventListener.class);
  
  private final DomainEventService domainEventService;
  private final Clock clock;
  private final ObjectMapper objectMapper;
  
  public AuditEventListener(DomainEventService domainEventService, Clock clock,
    ObjectMapper objectMapper) {
    this.domainEventService = domainEventService;
    this.clock = clock;
    this.objectMapper = objectMapper;
  }
  
  @KafkaListener(topics = "${topic.audit-logs}", groupId = "${spring.kafka.consumer.group-id}")
  public void onAuditEvent(String message) {
    try {
      log.info("Received audit event message: {}", message);
      
      AuditEvent event = objectMapper.readValue(message, AuditEvent.class);
      
      String path = event.getPath() != null ? event.getPath() : "UNKNOWN_PATH";
      String method = event.getMethod() != null ? event.getMethod() : "UNKNOWN";
      
      String query = event.getQuery();
      String uri = (query == null || query.isBlank()) ? path : path + "?" + query;
      
      String serviceName =
        (event.getServiceId() == null || event.getServiceId().isBlank()) ? "api-gateway"
          : event.getServiceId();
      
      String payloadJson = event.getBody();
      
      DomainEvent domainEvent = new DomainEvent(serviceName, path, method, uri, event.getUserId(),
        event.getStatus(), event.getTimestamp() != null ? event.getTimestamp() : clock.instant(),
        clock.instant(), payloadJson);
      
      log.info("=========================================");
      log.info("PAYLOAD JSON: {}", payloadJson);
      log.info("=========================================");
      
      domainEventService.save(domainEvent);
      
    } catch (Exception ex) {
      log.error("Error procesando evento de auditoría", ex);
    }
  }
}
