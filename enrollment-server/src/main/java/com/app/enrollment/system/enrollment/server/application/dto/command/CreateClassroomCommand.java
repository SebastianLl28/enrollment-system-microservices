package com.app.enrollment.system.enrollment.server.application.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author Alonso
 */
public record CreateClassroomCommand(
  @NotBlank(message = "Code cannot be blank")
  String code,
  String name,
  Integer capacity, // obligatoria solo para aulas físicas (se valida en el servicio)
  @NotNull(message = "Virtual flag cannot be null")
  Boolean virtual
) {
}
