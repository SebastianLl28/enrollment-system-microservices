package com.app.authorization.server.infrastructure.adapter.in.web;

import com.app.authorization.server.application.dto.command.CreateRoleCommand;
import com.app.authorization.server.application.dto.command.UpdateRoleCommand;
import com.app.authorization.server.application.dto.response.RoleResponse;
import com.app.authorization.server.application.port.in.RbacRoleUseCase;
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
@RequestMapping("/rbac/roles")
public class RoleRbacController {
  
  private final RbacRoleUseCase roleUseCase;
  
  public RoleRbacController(RbacRoleUseCase roleUseCase) {
    this.roleUseCase = roleUseCase;
  }
  
  @PostMapping
  public ResponseEntity<RoleResponse> create(@Valid @RequestBody CreateRoleCommand command) {
    RoleResponse response = roleUseCase.createRole(command);
    return ResponseEntity.ok(response);
  }
  
  @PutMapping("/{id}")
  public ResponseEntity<RoleResponse> update(
    @PathVariable Integer id,
    @Valid @RequestBody UpdateRoleCommand body
  ) {
    UpdateRoleCommand command = new UpdateRoleCommand(
      id,
      body.name(),
      body.description(),
      body.permissionIds(),
      body.viewCodes()
    );
    RoleResponse response = roleUseCase.updateRole(command);
    return ResponseEntity.ok(response);
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    roleUseCase.deleteRole(id);
    return ResponseEntity.noContent().build();
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<RoleResponse> getById(@PathVariable Integer id) {
    RoleResponse response = roleUseCase.getRole(id);
    return ResponseEntity.ok(response);
  }
  
  @GetMapping
  public ResponseEntity<List<RoleResponse>> list() {
    List<RoleResponse> responses = roleUseCase.listRoles();
    return ResponseEntity.ok(responses);
  }
}
