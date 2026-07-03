package com.app.enrollment.system.enrollment.server.application.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author Alonso
 */
public record UpdateCareerCommand(
  @NotNull(message = "Faculty ID cannot be null")
  Integer facultyId,
  @NotBlank(message = "Name cannot be blank")
  String name,
  String description,
  @NotNull(message = "Semester length cannot be null")
  int semesterLength,
  @NotBlank(message = "Degree awarded cannot be blank")
  String degreeAwarded,
  @NotNull(message = "Active cannot be null")
  boolean active
) {
}
