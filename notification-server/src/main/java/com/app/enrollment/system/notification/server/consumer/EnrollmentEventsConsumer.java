package com.app.enrollment.system.notification.server.consumer;

import com.app.common.events.EnrollmentAssignedEvent;
import com.app.enrollment.system.notification.server.service.EnrollmentNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class EnrollmentEventsConsumer {
  
  private static final Logger log = LoggerFactory.getLogger(EnrollmentEventsConsumer.class);
  
  private final ObjectMapper objectMapper;
  private final EnrollmentNotificationService notificationService;
  
  public EnrollmentEventsConsumer(ObjectMapper objectMapper,
    EnrollmentNotificationService notificationService) {
    this.objectMapper = objectMapper;
    this.notificationService = notificationService;
  }
  
  @KafkaListener(topics = "${topic.enrollment-events}", groupId = "notification-service")
  public void handleEnrollmentAssigned(ConsumerRecord<String, String> record) {
    try {
      EnrollmentAssignedEvent event = objectMapper.readValue(record.value(),
        EnrollmentAssignedEvent.class);
      
      log.info("Received enrollment event: {}", event.toString());
      
//      notificationService.sendEnrollmentEmail(event);
    
    } catch (Exception e) {
      log.error("Error processing enrollment event", e);
    }
  }
}
