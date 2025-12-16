package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import com.app.common.constant.ApiConstants;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateStudentCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.StudentResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateStudentUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllStudentUseCase;
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
public class StudentController {
  
  private final GetAllStudentUseCase getAllStudentUseCase;
  private final CreateStudentUseCase createStudentUseCase;
  
  public StudentController(GetAllStudentUseCase getAllStudentUseCase,
    CreateStudentUseCase createStudentUseCase) {
    this.getAllStudentUseCase = getAllStudentUseCase;
    this.createStudentUseCase = createStudentUseCase;
  }
  
  @GetMapping("/student")
  public ResponseEntity<List<StudentResponse>> findAll() {
    List<StudentResponse> studentResponseList = getAllStudentUseCase.findAll();
    return ResponseEntity.ok(studentResponseList);
  }
  
  @PostMapping("/student")
  public ResponseEntity<StudentResponse> createStudent(
    @RequestBody CreateStudentCommand createStudentCommand) {
    StudentResponse createdStudent = createStudentUseCase.createStudent(createStudentCommand);
    return ResponseEntity.ok(createdStudent);
  }
}
