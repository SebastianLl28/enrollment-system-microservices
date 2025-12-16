package com.app.enrollment.system.enrollment.server.application.dto.command;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateCourseOfferingCommand(
  @NotNull
  Integer courseId,
  @NotNull
  Integer termId,
  @NotEmpty
  String sectionCode,
  @NotNull
  Integer capacity
) {

}
