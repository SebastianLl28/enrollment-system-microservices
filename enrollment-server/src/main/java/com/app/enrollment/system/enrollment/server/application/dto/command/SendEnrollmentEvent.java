package com.app.enrollment.system.enrollment.server.application.dto.command;

import com.app.enrollment.system.enrollment.server.domain.event.EventType;
import com.app.enrollment.system.enrollment.server.domain.model.Course;
import com.app.enrollment.system.enrollment.server.domain.model.Student;
import com.app.enrollment.system.enrollment.server.domain.model.enums.EnrollmentStatus;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.UserID;

public record SendEnrollmentEvent(
  String aggregateType,
  String aggregateId,
  EventType eventType,
  Object payload,
  UserID userID,
  EnrollmentStatus enrollmentStatus,
  Student student,
  Course course
) {

}
