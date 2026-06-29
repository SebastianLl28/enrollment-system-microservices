package com.app.enrollment.system.enrollment.server.application.dto.command;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateFacultyCommand(
    @NotEmpty
    String name,
    @NotEmpty
    String dean,
    @NotEmpty
    String location,
    @NotNull
    Boolean active
) {
}
