package com.app.enrollment.system.enrollment.server.application.dto.command;

import com.app.enrollment.system.enrollment.server.domain.model.enums.EnrollmentStatus;

/**
 * @author Alonso
 */
public record UpdateEnrollmentCommand (
  EnrollmentStatus status
) {

}
