package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import com.app.common.constant.ApiConstants;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateClassroomCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateClassroomCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.ClassroomResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateClassroomUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllClassroomUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateClassroomUseCase;
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
public class ClassroomController {

  private final CreateClassroomUseCase createClassroomUseCase;
  private final GetAllClassroomUseCase getAllClassroomUseCase;
  private final UpdateClassroomUseCase updateClassroomUseCase;

  public ClassroomController(CreateClassroomUseCase createClassroomUseCase,
    GetAllClassroomUseCase getAllClassroomUseCase,
    UpdateClassroomUseCase updateClassroomUseCase) {
    this.createClassroomUseCase = createClassroomUseCase;
    this.getAllClassroomUseCase = getAllClassroomUseCase;
    this.updateClassroomUseCase = updateClassroomUseCase;
  }

  @PostMapping("/classroom")
  public ResponseEntity<ClassroomResponse> createClassroom(
    @Valid @RequestBody CreateClassroomCommand command) {
    ClassroomResponse response = createClassroomUseCase.createClassroom(command);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/classroom")
  public ResponseEntity<List<ClassroomResponse>> getAllClassrooms() {
    List<ClassroomResponse> responses = getAllClassroomUseCase.getAllClassrooms();
    return ResponseEntity.ok(responses);
  }

  @PutMapping("/classroom/{id}")
  public ResponseEntity<ClassroomResponse> updateClassroom(
    @Valid @RequestBody UpdateClassroomCommand command, @PathVariable Integer id) {
    ClassroomResponse response = updateClassroomUseCase.updateClassroom(command, id);
    return ResponseEntity.ok(response);
  }

}
