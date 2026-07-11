package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import com.app.common.constant.ApiConstants;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateSectionCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateSectionCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.SectionResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateSectionUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllSectionUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateSectionUseCase;
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
public class SectionController {

  private final CreateSectionUseCase createSectionUseCase;
  private final GetAllSectionUseCase getAllSectionUseCase;
  private final UpdateSectionUseCase updateSectionUseCase;

  public SectionController(CreateSectionUseCase createSectionUseCase,
    GetAllSectionUseCase getAllSectionUseCase, UpdateSectionUseCase updateSectionUseCase) {
    this.createSectionUseCase = createSectionUseCase;
    this.getAllSectionUseCase = getAllSectionUseCase;
    this.updateSectionUseCase = updateSectionUseCase;
  }

  @PostMapping("/section")
  public ResponseEntity<SectionResponse> createSection(
    @Valid @RequestBody CreateSectionCommand command) {
    SectionResponse response = createSectionUseCase.createSection(command);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/section")
  public ResponseEntity<List<SectionResponse>> getAllSections() {
    List<SectionResponse> responses = getAllSectionUseCase.getAllSections();
    return ResponseEntity.ok(responses);
  }

  @PutMapping("/section/{id}")
  public ResponseEntity<SectionResponse> updateSection(
    @Valid @RequestBody UpdateSectionCommand command, @PathVariable Integer id) {
    SectionResponse response = updateSectionUseCase.updateSection(command, id);
    return ResponseEntity.ok(response);
  }

}
