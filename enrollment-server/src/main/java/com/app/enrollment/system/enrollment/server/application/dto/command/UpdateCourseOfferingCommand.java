package com.app.enrollment.system.enrollment.server.application.dto.command;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Alonso
 */
public record UpdateCourseOfferingCommand(
  @NotNull(message = "Course ID cannot be null")
  Integer courseId,
  @NotNull(message = "Term ID cannot be null")
  Integer termId,
  @NotEmpty(message = "Section code cannot be empty")
  String sectionCode,
  @NotNull(message = "Capacity cannot be null")
  Integer capacity,
  @NotNull(message = "Active cannot be null")
  Boolean active,
  @DecimalMin(value = "0.00", message = "Price cannot be negative")
  BigDecimal price
) {
}
