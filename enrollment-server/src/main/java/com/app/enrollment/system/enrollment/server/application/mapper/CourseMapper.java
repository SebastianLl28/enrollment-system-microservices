package com.app.enrollment.system.enrollment.server.application.mapper;

import com.app.enrollment.system.enrollment.server.application.dto.response.CareerAssignmentResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseSummaryResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseWithLevelResponse;
import com.app.enrollment.system.enrollment.server.domain.model.Course;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

  public CourseResponse toResponse(Course course, List<CareerAssignmentResponse> careers) {
    CourseResponse response = new CourseResponse();
    response.setId(course.getId().getValue());
    response.setCode(course.getCode().getValue());
    response.setName(course.getName());
    response.setDescription(course.getDescription());
    response.setCredits(course.getCredits().getValue());
    response.setActive(course.isActive());
    response.setCareers(careers);
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

  public CourseWithLevelResponse toWithLevelResponse(Course course, Integer semesterLevel) {
    return new CourseWithLevelResponse(
      course.getId().getValue(),
      course.getName(),
      course.getCode().getValue(),
      course.isActive(),
      semesterLevel
    );
  }

}
