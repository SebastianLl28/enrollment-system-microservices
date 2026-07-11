package com.app.enrollment.system.enrollment.server.application.dto.command;

import jakarta.validation.constraints.NotNull;

/**
 * Asignación de un curso a una carrera dentro de la malla, con el ciclo en que se dicta.
 *
 * @author Alonso
 */
public record CareerCourseAssignmentCommand(
  @NotNull(message = "Career ID cannot be null")
  Integer careerId,
  @NotNull(message = "Semester level cannot be null")
  Integer semesterLevel
) {
}
