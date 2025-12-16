package com.app.enrollment.system.enrollment.server.application.dto.command;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;

public record CreateTermCommand(
  
  @NotEmpty
  String code,
  
  LocalDate startDate,
  
  LocalDate endDate,
  
  boolean active
  
) {

}
