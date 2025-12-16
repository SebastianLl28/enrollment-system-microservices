package com.app.enrollment.system.enrollment.server.application.dto.command;

import jakarta.validation.constraints.NotNull;

public record CreateEnrollmentCommand(
  @NotNull(message = "studentId must not be null")
  Integer studentId,
  
  @NotNull(message = "courseOfferingId must not be null")
  Integer courseOfferingId
) {

}
