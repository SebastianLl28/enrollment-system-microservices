package com.app.authorization.server.application.dto.command;

import jakarta.validation.constraints.NotBlank;

public record LoginCommand(
    @NotBlank String username,
    @NotBlank String password
) {

}
