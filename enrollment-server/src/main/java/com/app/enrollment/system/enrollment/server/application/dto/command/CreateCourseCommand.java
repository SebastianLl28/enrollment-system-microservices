package com.app.enrollment.system.enrollment.server.application.dto.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateCourseCommand(
  @NotBlank(message = "Code cannot be blank")
  String code,
  @NotBlank(message = "Name cannot be blank")
  String name,
  String description,
  @NotNull(message = "Credits cannot be null")
  Integer credits,
  @NotEmpty(message = "At least one career must be assigned")
  List<@Valid CareerCourseAssignmentCommand> careers
) {
}
