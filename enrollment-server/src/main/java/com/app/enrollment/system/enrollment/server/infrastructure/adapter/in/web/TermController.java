package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import com.app.common.constant.ApiConstants;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateTermCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.TermResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateTermUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllTermUserCase;
import jakarta.validation.Valid;
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
public class TermController {
  
  private final GetAllTermUserCase getAllTermUserCase;
  private final CreateTermUseCase createTermUseCase;
  
  public TermController(GetAllTermUserCase getAllTermUserCase, CreateTermUseCase createTermUseCase) {
    this.getAllTermUserCase = getAllTermUserCase;
    this.createTermUseCase = createTermUseCase;
  }
  
  @GetMapping("/term")
  public ResponseEntity<List<TermResponse>> findAll() {
    List<TermResponse> termList = getAllTermUserCase.getAllTerms();
    return ResponseEntity.ok(termList);
  }
  
  
  @PostMapping("/term")
  public ResponseEntity<TermResponse> createTerm(@Valid @RequestBody CreateTermCommand createTermCommand) {
    TermResponse termResponse = createTermUseCase.createTerm(createTermCommand);
    return ResponseEntity.ok(termResponse);
  }
  
}
