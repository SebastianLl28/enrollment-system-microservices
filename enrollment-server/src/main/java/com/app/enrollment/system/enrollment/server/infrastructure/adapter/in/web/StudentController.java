package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import com.app.common.constant.ApiConstants;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateStudentCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateStudentCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.StudentResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateStudentUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllStudentUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetStudentByIdUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateStudentUseCase;
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
public class StudentController {
  
  private final GetAllStudentUseCase getAllStudentUseCase;
  private final CreateStudentUseCase createStudentUseCase;
  private final GetStudentByIdUseCase getStudentByIdUseCase;
  private final UpdateStudentUseCase updateStudentUseCase;

  public StudentController(GetAllStudentUseCase getAllStudentUseCase,
      CreateStudentUseCase createStudentUseCase, GetStudentByIdUseCase getStudentByIdUseCase,
      UpdateStudentUseCase updateStudentUseCase) {
    this.getAllStudentUseCase = getAllStudentUseCase;
    this.createStudentUseCase = createStudentUseCase;
    this.getStudentByIdUseCase = getStudentByIdUseCase;
    this.updateStudentUseCase = updateStudentUseCase;
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

  @GetMapping("/student/{id}")
  public ResponseEntity<StudentResponse> findById(@PathVariable Integer id) {
    return ResponseEntity.ok(getStudentByIdUseCase.findById(id));
  }

  @PutMapping("/student/{id}")
  public ResponseEntity<StudentResponse> updateStudent(
      @Valid @RequestBody UpdateStudentCommand command, @PathVariable Integer id) {
    return ResponseEntity.ok(updateStudentUseCase.updateStudent(command, id));
  }
}
