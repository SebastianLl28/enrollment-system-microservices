package com.app.enrollment.system.enrollment.server.application.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author Alonso
 */
public record CreateSectionCommand(
  @NotNull(message = "Course ID cannot be null")
  Integer courseId,
  @NotNull(message = "Term ID cannot be null")
  Integer termId,
  @NotNull(message = "Classroom ID cannot be null")
  Integer classroomId,
  @NotBlank(message = "Section code cannot be blank")
  String sectionCode
) {
}
