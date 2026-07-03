package com.app.enrollment.system.enrollment.server.application.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author Alonso
 */
public record UpdateCourseCommand(
  @NotNull(message = "Career ID cannot be null")
  Integer careerId,
  @NotBlank(message = "Code cannot be blank")
  String code,
  @NotBlank(message = "Name cannot be blank")
  String name,
  String description,
  @NotNull(message = "Credits cannot be null")
  Integer credits,
  @NotNull(message = "Semester level cannot be null")
  Integer semesterLevel,
  @NotNull(message = "Active cannot be null")
  boolean active
) {
}
