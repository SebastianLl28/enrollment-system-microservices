package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import com.app.common.constant.ApiConstants;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateCourseOfferingCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateCourseOfferingCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.CourseOfferingResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateCourseOfferingUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllCourseOfferingUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateCourseOfferingUseCase;
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
public class CourseOfferingController {
  
  private final CreateCourseOfferingUseCase createCourseOfferingUseCase;
  private final GetAllCourseOfferingUseCase getAllCourseOfferingUseCase;
  private final UpdateCourseOfferingUseCase updateCourseOfferingUseCase;

  public CourseOfferingController(CreateCourseOfferingUseCase createCourseOfferingUseCase,
    GetAllCourseOfferingUseCase getAllCourseOfferingUseCase,
    UpdateCourseOfferingUseCase updateCourseOfferingUseCase) {
    this.createCourseOfferingUseCase = createCourseOfferingUseCase;
    this.getAllCourseOfferingUseCase = getAllCourseOfferingUseCase;
    this.updateCourseOfferingUseCase = updateCourseOfferingUseCase;
  }
  
  @PostMapping("/course-offering")
  public ResponseEntity<CourseOfferingResponse> createCourseOffering(@Valid @RequestBody
    CreateCourseOfferingCommand command) {
    CourseOfferingResponse response = createCourseOfferingUseCase.createCourseOffering(command);
    return ResponseEntity.ok(response);
  }
  
  @GetMapping("/course-offering")
  public ResponseEntity<List<CourseOfferingResponse>> getAllCourseOfferings() {
    List<CourseOfferingResponse> responses = getAllCourseOfferingUseCase.getAllCourseOfferings();
    return ResponseEntity.ok(responses);
  }

  @PutMapping("/course-offering/{id}")
  public ResponseEntity<CourseOfferingResponse> updateCourseOffering(
    @Valid @RequestBody UpdateCourseOfferingCommand command, @PathVariable Integer id) {
    CourseOfferingResponse response =
      updateCourseOfferingUseCase.updateCourseOffering(command, id);
    return ResponseEntity.ok(response);
  }

}
