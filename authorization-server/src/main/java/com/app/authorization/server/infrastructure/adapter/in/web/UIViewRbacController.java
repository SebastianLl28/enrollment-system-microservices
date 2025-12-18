package com.app.authorization.server.infrastructure.adapter.in.web;

import com.app.authorization.server.application.dto.command.CreateUIViewCommand;
import com.app.authorization.server.application.dto.command.UpdateUIViewCommand;
import com.app.authorization.server.application.dto.response.UIViewResponse;
import com.app.authorization.server.application.port.in.RbacViewUseCase;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/rbac/views")
public class UIViewRbacController {
  
  private final RbacViewUseCase viewUseCase;
  
  public UIViewRbacController(RbacViewUseCase viewUseCase) {
    this.viewUseCase = viewUseCase;
  }
  
  @PostMapping
  public ResponseEntity<UIViewResponse> create(@Valid @RequestBody CreateUIViewCommand command) {
    UIViewResponse response = viewUseCase.createView(command);
    return ResponseEntity.ok(response);
  }
  
  @PutMapping("/{code}")
  public ResponseEntity<UIViewResponse> update(
    @PathVariable String code,
    @Valid @RequestBody UpdateUIViewCommand body
  ) {
    UpdateUIViewCommand command = new UpdateUIViewCommand(
      code,
      body.route(),
      body.label(),
      body.module(),
      body.sortOrder(),
      body.active()
    );
    UIViewResponse response = viewUseCase.updateView(command);
    return ResponseEntity.ok(response);
  }
  
  @DeleteMapping("/{code}")
  public ResponseEntity<Void> delete(@PathVariable String code) {
    viewUseCase.deleteView(code);
    return ResponseEntity.noContent().build();
  }
  
  @GetMapping("/{code}")
  public ResponseEntity<UIViewResponse> getByCode(@PathVariable String code) {
    UIViewResponse response = viewUseCase.getView(code);
    return ResponseEntity.ok(response);
  }
  
  @GetMapping
  public ResponseEntity<List<UIViewResponse>> list() {
    List<UIViewResponse> responses = viewUseCase.listViews();
    return ResponseEntity.ok(responses);
  }
}
