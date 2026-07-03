package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import com.app.common.constant.ApiConstants;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateCareerCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateCareerCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.CareerResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateCareerUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllCareerUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateCareerUseCase;
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
public class CareerController {
  
  private final GetAllCareerUseCase getAllCareerUseCase;
  private final CreateCareerUseCase createCareerUseCase;
  private final UpdateCareerUseCase updateCareerUseCase;

  public CareerController(GetAllCareerUseCase getAllCareerUseCase,
      CreateCareerUseCase createCareerUseCase, UpdateCareerUseCase updateCareerUseCase) {
    this.getAllCareerUseCase = getAllCareerUseCase;
    this.createCareerUseCase = createCareerUseCase;
    this.updateCareerUseCase = updateCareerUseCase;
  }
  
  @GetMapping("/career")
  public ResponseEntity<List<CareerResponse>> findAll(
      @RequestParam(defaultValue = "false") Boolean includeInactive) {
    List<CareerResponse> careerList = getAllCareerUseCase.findAll(includeInactive);
    return ResponseEntity.ok(careerList);
  }
  
  @PostMapping("/career")
  public ResponseEntity<CareerResponse> createCareer(@RequestBody CreateCareerCommand createCareerCommand) {
    CareerResponse careerResponse = createCareerUseCase.createCareer(createCareerCommand);
    return ResponseEntity.ok(careerResponse);
  }

  @PutMapping("/career/{id}")
  public ResponseEntity<CareerResponse> updateCareer(
      @Valid @RequestBody UpdateCareerCommand updateCareerCommand, @PathVariable Integer id) {
    CareerResponse careerResponse = updateCareerUseCase.updateCareer(updateCareerCommand, id);
    return ResponseEntity.ok(careerResponse);
  }

}
