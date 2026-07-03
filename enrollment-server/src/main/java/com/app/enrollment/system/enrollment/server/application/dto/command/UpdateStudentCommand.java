package com.app.enrollment.system.enrollment.server.application.dto.command;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Alonso
 */
public record UpdateStudentCommand(
  @NotEmpty(message = "Name cannot be empty")
  String name,

  @NotEmpty(message = "Last name cannot be empty")
  String lastName,

  @jakarta.validation.constraints.Email
  @NotEmpty(message = "Email cannot be empty")
  String email,

  @NotEmpty(message = "Document number cannot be empty")
  String documentNumber,

  String phoneNumber,

  @NotEmpty(message = "Birth date cannot be empty")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  String birthDate,

  String address,

  @NotNull(message = "Active cannot be null")
  Boolean active
) {
}
