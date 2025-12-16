package com.app.authorization.server.application.dto.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * @author Alonso
 */
public record RegisterUserCommand(
  @NotBlank String username,
  @NotBlank String password,
  @Email String email,
  @NotBlank String fullName
) {

}
