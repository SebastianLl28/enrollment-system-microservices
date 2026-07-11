package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import com.app.common.constant.ApiConstants;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreateCareerOfferingCommand;
import com.app.enrollment.system.enrollment.server.application.dto.command.UpdateCareerOfferingCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.CareerOfferingResponse;
import com.app.enrollment.system.enrollment.server.application.port.in.CreateCareerOfferingUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.GetAllCareerOfferingUseCase;
import com.app.enrollment.system.enrollment.server.application.port.in.UpdateCareerOfferingUseCase;
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
public class CareerOfferingController {

  private final CreateCareerOfferingUseCase createCareerOfferingUseCase;
  private final GetAllCareerOfferingUseCase getAllCareerOfferingUseCase;
  private final UpdateCareerOfferingUseCase updateCareerOfferingUseCase;

  public CareerOfferingController(CreateCareerOfferingUseCase createCareerOfferingUseCase,
    GetAllCareerOfferingUseCase getAllCareerOfferingUseCase,
    UpdateCareerOfferingUseCase updateCareerOfferingUseCase) {
    this.createCareerOfferingUseCase = createCareerOfferingUseCase;
    this.getAllCareerOfferingUseCase = getAllCareerOfferingUseCase;
    this.updateCareerOfferingUseCase = updateCareerOfferingUseCase;
  }

  @PostMapping("/career-offering")
  public ResponseEntity<CareerOfferingResponse> createCareerOffering(@Valid @RequestBody
    CreateCareerOfferingCommand command) {
    CareerOfferingResponse response = createCareerOfferingUseCase.createCareerOffering(command);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/career-offering")
  public ResponseEntity<List<CareerOfferingResponse>> getAllCareerOfferings() {
    List<CareerOfferingResponse> responses = getAllCareerOfferingUseCase.getAllCareerOfferings();
    return ResponseEntity.ok(responses);
  }

  @PutMapping("/career-offering/{id}")
  public ResponseEntity<CareerOfferingResponse> updateCareerOffering(
    @Valid @RequestBody UpdateCareerOfferingCommand command, @PathVariable Integer id) {
    CareerOfferingResponse response =
      updateCareerOfferingUseCase.updateCareerOffering(command, id);
    return ResponseEntity.ok(response);
  }

}
