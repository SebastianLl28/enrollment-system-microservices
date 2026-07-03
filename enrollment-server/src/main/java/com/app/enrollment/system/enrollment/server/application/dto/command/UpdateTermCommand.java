package com.app.enrollment.system.enrollment.server.application.dto.command;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author Alonso
 */
public record UpdateTermCommand(

  @NotEmpty(message = "Code cannot be empty")
  String code,

  @NotNull(message = "Start date cannot be null")
  LocalDate startDate,

  @NotNull(message = "End date cannot be null")
  LocalDate endDate,

  @NotNull(message = "Active cannot be null")
  boolean active

) {
}
