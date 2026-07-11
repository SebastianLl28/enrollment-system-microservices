package com.app.enrollment.system.enrollment.server.application.dto.command;

import jakarta.validation.constraints.NotNull;

public record CreateEnrollmentCommand(
  @NotNull(message = "studentId must not be null")
  Integer studentId,

  @NotNull(message = "careerOfferingId must not be null")
  Integer careerOfferingId
) {

}
