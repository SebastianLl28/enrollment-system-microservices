package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import com.app.common.constant.ApiConstants;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateCourseCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateCourseUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllCourseUseCase;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alonso
 */
@RestController
@RequestMapping(ApiConstants.API_PREFIX)
public class CourseController {

  private final GetAllCourseUseCase getAllCourseUseCase;
  private final CreateCourseUseCase createCourseUseCase;

  public CourseController(GetAllCourseUseCase getAllCourseUseCase, CreateCourseUseCase createCourseUseCase) {
    this.getAllCourseUseCase = getAllCourseUseCase;
    this.createCourseUseCase = createCourseUseCase;
  }

  @GetMapping("/course")
  public ResponseEntity<List<CourseResponse>> findAll() {
    List<CourseResponse> courseResponseList = getAllCourseUseCase.findAll();
    return ResponseEntity.ok(courseResponseList);
  }

  @PostMapping("/course")
  public ResponseEntity<CourseResponse> createCourse(@RequestBody CreateCourseCommand createCourseCommand) {
    CourseResponse courseResponse = createCourseUseCase.createCourse(createCourseCommand);
    return ResponseEntity.ok(courseResponse);
  }

}
