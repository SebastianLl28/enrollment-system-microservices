package com.app.enrollment.system.enrollment.server.application.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author Alonso
 */
public record CreateFacultyCommand(
  @NotBlank(message = "Name cannot be blank")
  String name,
  String description,
  @NotNull(message = "Location cannot be null")
  String location,
  @NotBlank(message = "Dean cannot be blank")
  String dean
) {
}
