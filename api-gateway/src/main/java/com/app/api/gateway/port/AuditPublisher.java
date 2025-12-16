package com.app.api.gateway.port;

import com.app.common.events.AuditEvent;

/**
 * @author Alonso
 */
public interface AuditPublisher {
  void publish(AuditEvent auditEvent);
}
