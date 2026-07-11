package com.app.enrollment.system.enrollment.server.application.dto.command;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Alonso
 */
public record UpdateCareerOfferingCommand(
  @NotNull(message = "Career ID cannot be null")
  Integer careerId,
  @NotNull(message = "Term ID cannot be null")
  Integer termId,
  @NotNull(message = "Capacity cannot be null")
  Integer capacity,
  @NotNull(message = "Active cannot be null")
  Boolean active,
  @DecimalMin(value = "0.00", message = "Price cannot be negative")
  BigDecimal price
) {
}
