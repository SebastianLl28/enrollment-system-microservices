package com.app.enrollment.system.enrollment.server.application.port.out;

import com.app.enrollment.system.enrollment.server.application.dto.command.SendEnrollmentEvent;

/**
 * @author Alonso
 */
public interface OutboxEventPort {
  
  void saveEvent(SendEnrollmentEvent event);

}
