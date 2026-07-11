package com.app.enrollment.system.enrollment.server.application.mapper;

import com.app.enrollment.system.enrollment.server.application.dto.response.CareerResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.CareerSummaryResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseWithLevelResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.FacultySummaryResponse;
import com.app.enrollment.system.enrollment.server.domain.model.Career;
import com.app.enrollment.system.enrollment.server.domain.model.Faculty;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class CareerMapper {

  public CareerResponse toResponse(Career career, Faculty faculty,
    List<CourseWithLevelResponse> courseList) {
    CareerResponse response = new CareerResponse();
    response.setId(career.getId().getValue());
    response.setName(career.getName());
    response.setDescription(career.getDescription());
    response.setSemesterLength(career.getSemesterLength());
    response.setDegreeAwarded(career.getDegreeAwarded().getValue());
    response.setRegistrationDate(career.getRegistrationDate());
    response.setActive(career.isActive());

    FacultySummaryResponse facultySummaryResponse = new FacultySummaryResponse();
    facultySummaryResponse.setId(faculty.getId().getValue());
    facultySummaryResponse.setName(faculty.getName());

    response.setFaculty(facultySummaryResponse);

    response.setCourseList(courseList);

    return response;
  }

  public CareerSummaryResponse toSummaryResponse(Career career) {
    CareerSummaryResponse response = new CareerSummaryResponse();
    response.setId(career.getId().getValue());
    response.setName(career.getName());
    response.setActive(career.isActive());
    return response;
  }

}
