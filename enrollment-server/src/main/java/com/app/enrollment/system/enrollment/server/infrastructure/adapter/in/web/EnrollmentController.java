package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import com.app.common.constant.ApiConstants;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateEnrollmentCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UnenrollStudentCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateEnrollmentCommand;
import com.app.enrollment.system.enrollment.server.application.dto.query.EnrollmentQuery;
import com.app.enrollment.system.enrollment.server.application.dto.response.EnrollmentResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateEnrollmentUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllEnrollmentCourseUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetEnrollmentByIdUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UnenrollStudentUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateEnrollmentUseCase;
import com.app.enrollment.system.enrollment.server.domain.model.valueobject.UserID;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alonso
 */
@RestController
@RequestMapping(ApiConstants.API_PREFIX)
public class EnrollmentController {
  
  private final UnenrollStudentUseCase unenrollStudentUseCase;
  private final CreateEnrollmentUseCase createEnrollmentUseCase;
  private final GetAllEnrollmentCourseUseCase getAllEnrollmentCourseUseCase;
  private final GetEnrollmentByIdUseCase getEnrollmentByIdUseCase;
  private final UpdateEnrollmentUseCase updateEnrollmentUseCase;
  
  public EnrollmentController(UnenrollStudentUseCase unenrollStudentUseCase,
    CreateEnrollmentUseCase createEnrollmentUseCase,
    GetAllEnrollmentCourseUseCase getAllEnrollmentCourseUseCase,
    GetEnrollmentByIdUseCase getEnrollmentByIdUseCase,
    UpdateEnrollmentUseCase updateEnrollmentUseCase) {
    this.unenrollStudentUseCase = unenrollStudentUseCase;
    this.createEnrollmentUseCase = createEnrollmentUseCase;
    this.getAllEnrollmentCourseUseCase = getAllEnrollmentCourseUseCase;
    this.getEnrollmentByIdUseCase = getEnrollmentByIdUseCase;
    this.updateEnrollmentUseCase = updateEnrollmentUseCase;
  }
  
  @PostMapping("/enrollment")
  public ResponseEntity<EnrollmentResponse> createEnrollment(
    @RequestHeader(ApiConstants.HEADER_USER_ID) Integer userId,
    @RequestBody CreateEnrollmentCommand command) {
    EnrollmentResponse response = createEnrollmentUseCase.createEnrollment(command, userId);
    return ResponseEntity.ok(response);
  }
  
  @DeleteMapping("/enrollment")
  public ResponseEntity<EnrollmentResponse> unenrollStudent(
    @RequestBody UnenrollStudentCommand command, @RequestHeader(ApiConstants.HEADER_USER_ID) Integer userId) {
    UserID userID = new UserID(userId);
    EnrollmentResponse response = unenrollStudentUseCase.unenrollStudent(command, userID);
    return ResponseEntity.ok(response);
  }
  
  @GetMapping("/enrollment")
  public ResponseEntity<List<EnrollmentResponse>> getAllEnrollments(
    @RequestParam(required = false) Integer studentId,
    @RequestParam(required = false) Integer termId,
    @RequestParam(required = false) Integer courseId) {
    EnrollmentQuery query = new EnrollmentQuery(studentId, termId, courseId);
    List<EnrollmentResponse> responses = getAllEnrollmentCourseUseCase.getAllEnrollmentCourses(
      query);
    return ResponseEntity.ok(responses);
  }
  
  
  @GetMapping("/enrollment/{enrollmentId}")
  public ResponseEntity<EnrollmentResponse> getEnrollmentById(@PathVariable Integer enrollmentId,
    @RequestHeader(ApiConstants.HEADER_USER_ID) Integer userId) {
    UserID userID = new UserID(userId);
    EnrollmentResponse response = getEnrollmentByIdUseCase.getEnrollmentById(enrollmentId, userID);
    return ResponseEntity.ok(response);
  }
  
  @PutMapping("/enrollment/{enrollmentId}")
  public ResponseEntity<EnrollmentResponse> updateEnrollment(@PathVariable Integer enrollmentId,
    @RequestBody UpdateEnrollmentCommand command,
    @RequestHeader(ApiConstants.HEADER_USER_ID) Integer userId
    ) {
    UserID userID = new UserID(userId);
    EnrollmentResponse response = updateEnrollmentUseCase.updateEnrollment(enrollmentId, command, userID);
    return ResponseEntity.ok(response);
  }
  
}
