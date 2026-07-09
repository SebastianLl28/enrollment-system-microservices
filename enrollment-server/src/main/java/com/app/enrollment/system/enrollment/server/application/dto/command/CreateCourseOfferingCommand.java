package com.app.enrollment.system.enrollment.server.application.dto.command;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateCourseOfferingCommand(
  @NotNull
  Integer courseId,
  @NotNull
  Integer termId,
  @NotEmpty
  String sectionCode,
  @NotNull
  Integer capacity,
  @DecimalMin(value = "0.00", message = "Price cannot be negative")
  BigDecimal price
) {

}
