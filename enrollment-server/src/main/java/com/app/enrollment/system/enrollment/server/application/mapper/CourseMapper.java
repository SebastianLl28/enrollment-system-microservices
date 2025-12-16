package com.app.enrollment.system.enrollment.server.application.mapper;

import com.app.enrollment.system.enrollment.server.application.dto.response.CourseResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseSummaryResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.StudentSummaryResponse;
import com.app.enrollment.system.enrollment.server.domain.model.Course;
import com.app.enrollment.system.enrollment.server.domain.model.Student;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

  private final StudentMapper studentMapper;

  public CourseMapper(StudentMapper studentMapper) {
    this.studentMapper = studentMapper;
  }

  public CourseResponse toResponse(Course course, List<Student> studentList) {
    CourseResponse response = new CourseResponse();
    response.setId(course.getId().getValue());
    response.setCode(course.getCode().getValue());
    response.setName(course.getName());
    response.setDescription(course.getDescription());
    response.setSemesterLevel(course.getSemesterLevel().getValue());
    response.setCredits(course.getCredits().getValue());
    response.setActive(course.isActive());

    List<StudentSummaryResponse> studentSummaries = studentList.stream()
        .map(studentMapper::toStudentSummaryResponse)
        .toList();

    response.setEnrolledStudentList(studentSummaries);

    return response;
  }

  public CourseSummaryResponse toSummaryResponse(Course course) {
    CourseSummaryResponse response = new CourseSummaryResponse();
    response.setId(course.getId().getValue());
    response.setCode(course.getCode().getValue());
    response.setName(course.getName());
    response.setActive(course.isActive());
    return response;
  }
  
  
}
