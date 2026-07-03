package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import com.app.common.constant.ApiConstants;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateFacultyCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateFacultyCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.FacultyResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateFacultyUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllFacultyUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetFacultyByIdUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateFacultyUseCase;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alonso
 */
@RestController
@RequestMapping(ApiConstants.API_PREFIX)
public class FacultyController {
  
  private final GetAllFacultyUseCase getAllFacultyUseCase;
  private final GetFacultyByIdUseCase getFacultyByIdUseCase;
  private final CreateFacultyUseCase createFacultyUseCase;
  private final UpdateFacultyUseCase updateFacultyUseCase;

  public FacultyController(GetAllFacultyUseCase getAllFacultyUseCase,
      GetFacultyByIdUseCase getFacultyByIdUseCase,
      CreateFacultyUseCase createFacultyUseCase, UpdateFacultyUseCase updateFacultyUseCase) {
    this.getAllFacultyUseCase = getAllFacultyUseCase;
    this.getFacultyByIdUseCase = getFacultyByIdUseCase;
    this.createFacultyUseCase = createFacultyUseCase;
    this.updateFacultyUseCase = updateFacultyUseCase;
  }
  
  @GetMapping("/faculty")
  public ResponseEntity<List<FacultyResponse>> findAll(
      @RequestParam(defaultValue = "false") Boolean includeInactive) {
    List<FacultyResponse> facultyResponseList = getAllFacultyUseCase.findAll(includeInactive);
    return ResponseEntity.ok(facultyResponseList);
  }

  @GetMapping("/faculty/{id}")
  public ResponseEntity<FacultyResponse> findById(@PathVariable Integer id) {
    FacultyResponse facultyResponse = getFacultyByIdUseCase.findById(id);
    return ResponseEntity.ok(facultyResponse);
  }
  
  @PostMapping("/faculty")
  public ResponseEntity<FacultyResponse> createFaculty(
      @RequestBody CreateFacultyCommand createFacultyCommand) {
    FacultyResponse facultyResponse = createFacultyUseCase.createFaculty(createFacultyCommand);
    return ResponseEntity.ok(facultyResponse);
  }
  
  @PutMapping("/faculty/{id}")
  public ResponseEntity<FacultyResponse> updateFaculty(
      @Valid @RequestBody UpdateFacultyCommand updateFacultyCommand, @PathVariable Integer id) {
    FacultyResponse facultyResponse = updateFacultyUseCase.updateFaculty(updateFacultyCommand, id);
    return ResponseEntity.ok(facultyResponse);
  }
  
}
