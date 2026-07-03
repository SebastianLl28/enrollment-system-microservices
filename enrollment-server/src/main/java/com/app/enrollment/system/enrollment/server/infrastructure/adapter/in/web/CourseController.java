package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import com.app.common.constant.ApiConstants;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateCourseCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateCourseCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateCourseUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllCourseUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateCourseUseCase;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
  private final UpdateCourseUseCase updateCourseUseCase;

  public CourseController(GetAllCourseUseCase getAllCourseUseCase,
      CreateCourseUseCase createCourseUseCase, UpdateCourseUseCase updateCourseUseCase) {
    this.getAllCourseUseCase = getAllCourseUseCase;
    this.createCourseUseCase = createCourseUseCase;
    this.updateCourseUseCase = updateCourseUseCase;
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

  @PutMapping("/course/{id}")
  public ResponseEntity<CourseResponse> updateCourse(
      @Valid @RequestBody UpdateCourseCommand updateCourseCommand, @PathVariable Integer id) {
    CourseResponse courseResponse = updateCourseUseCase.updateCourse(updateCourseCommand, id);
    return ResponseEntity.ok(courseResponse);
  }

}
