package com.app.enrollment.system.enrollment.server.application.mapper;

import com.app.enrollment.system.enrollment.server.application.dto.response.StudentResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.StudentSummaryResponse;
import com.app.enrollment.system.enrollment.server.domain.model.Student;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class StudentMapper {

  public StudentResponse toStudentResponse(Student student) {
    StudentResponse studentResponse = new StudentResponse();
    String birthDate = student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : null;

    studentResponse.setId(student.getId().getValue());
    studentResponse.setName(student.getName());
    studentResponse.setLastName(student.getLastName());
    studentResponse.setDocumentNumber(student.getDocumentNumber().getValue());
    studentResponse.setEmail(student.getEmail().getValue());
    studentResponse.setPhoneNumber(student.getPhoneNumber());
    studentResponse.setBirthDate(birthDate);
    studentResponse.setAddress(student.getAddress());
    studentResponse.setActive(student.isActive());
    studentResponse.setCreatedAt(student.getCreatedAt());

    return studentResponse;
  }

  public StudentSummaryResponse toStudentSummaryResponse(Student student) {
    StudentSummaryResponse summaryResponse = new StudentSummaryResponse();
    summaryResponse.setId(student.getId().getValue());
    summaryResponse.setFirstName(student.getName());
    summaryResponse.setLastName(student.getLastName());
    return summaryResponse;
  }

}
