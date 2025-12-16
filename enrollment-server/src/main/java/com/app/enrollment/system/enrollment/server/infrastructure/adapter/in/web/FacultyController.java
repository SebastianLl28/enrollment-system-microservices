package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import com.app.common.constant.ApiConstants;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateFacultyCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.FacultyResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateFacultyUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllFacultyUseCase;
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
public class FacultyController {

  private final GetAllFacultyUseCase getAllFacultyUseCase;
  private final CreateFacultyUseCase createFacultyUseCase;
//  private final UpdateFacultyUseCase updateFacultyUseCase;
//  private final CurrentUserFactory currentUserFactory;
  
  public FacultyController(GetAllFacultyUseCase getAllFacultyUseCase, CreateFacultyUseCase createFacultyUseCase) {
    this.getAllFacultyUseCase = getAllFacultyUseCase;
    this.createFacultyUseCase = createFacultyUseCase;
//    this.updateFacultyUseCase = updateFacultyUseCase;
//    this.currentUserFactory = currentUserFactory;
  }
  
  @GetMapping("/faculty")
  public ResponseEntity<List<FacultyResponse>> findAll() {
    List<FacultyResponse> facultyResponseList = getAllFacultyUseCase.findAll();
    return ResponseEntity.ok(facultyResponseList);
  }

  @PostMapping("/faculty")
  public ResponseEntity<FacultyResponse> createFaculty(@RequestBody CreateFacultyCommand createFacultyCommand) {
    FacultyResponse facultyResponse = createFacultyUseCase.createFaculty(createFacultyCommand);
    return ResponseEntity.ok(facultyResponse);
  }

//  @PutMapping("/faculty/{id}")
//  public ResponseEntity<FacultyResponse> updateFaculty(@Valid @RequestBody UpdateFacultyCommand updateFacultyCommand, @PathVariable Integer id) {
//    FacultyResponse facultyResponse = updateFacultyUseCase.updateFaculty(updateFacultyCommand, id);
//    return ResponseEntity.ok(facultyResponse);
//  }

}
