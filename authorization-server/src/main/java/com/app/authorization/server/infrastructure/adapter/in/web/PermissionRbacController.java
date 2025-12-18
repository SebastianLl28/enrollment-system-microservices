package com.app.authorization.server.infrastructure.adapter.in.web;

import com.app.authorization.server.application.dto.command.CreatePermissionCommand;
import com.app.authorization.server.application.dto.command.UpdatePermissionCommand;
import com.app.authorization.server.application.dto.response.PermissionResponse;
import com.app.authorization.server.application.port.in.RbacPermissionUseCase;
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
@RequestMapping("/rbac/permissions")
public class PermissionRbacController {
  
  private final RbacPermissionUseCase permissionUseCase;
  
  public PermissionRbacController(RbacPermissionUseCase permissionUseCase) {
    this.permissionUseCase = permissionUseCase;
  }
  
  @PostMapping
  public ResponseEntity<PermissionResponse> create(@Valid @RequestBody CreatePermissionCommand command) {
    PermissionResponse response = permissionUseCase.createPermission(command);
    return ResponseEntity.ok(response);
  }
  
  @PutMapping("/{id}")
  public ResponseEntity<PermissionResponse> update(
    @PathVariable Integer id,
    @Valid @RequestBody UpdatePermissionCommand body
  ) {
    // forzamos que el id del path sea el que manda
    UpdatePermissionCommand command = new UpdatePermissionCommand(
      id,
      body.resource(),
      body.operation(),
      body.scope(),
      body.description()
    );
    PermissionResponse response = permissionUseCase.updatePermission(command);
    return ResponseEntity.ok(response);
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    permissionUseCase.deletePermission(id);
    return ResponseEntity.noContent().build();
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<PermissionResponse> getById(@PathVariable Integer id) {
    PermissionResponse response = permissionUseCase.getPermission(id);
    return ResponseEntity.ok(response);
  }
  
  @GetMapping
  public ResponseEntity<List<PermissionResponse>> list() {
    List<PermissionResponse> responses = permissionUseCase.listPermissions();
    return ResponseEntity.ok(responses);
  }
}
