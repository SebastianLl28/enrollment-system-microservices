package com.app.enrollment.system.enrollment.server.application.dto.command;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateCareerOfferingCommand(
  @NotNull
  Integer careerId,
  @NotNull
  Integer termId,
  @NotNull
  Integer capacity,
  @DecimalMin(value = "0.00", message = "Price cannot be negative")
  BigDecimal price
) {

}
