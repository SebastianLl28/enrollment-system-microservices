package com.app.authorization.server.application.dto.command;

import com.app.authorization.server.domain.model.enums.OperationType;
import com.app.authorization.server.domain.model.enums.ResourceType;
import com.app.authorization.server.domain.model.enums.ScopeType;

/**
 * @author Alonso
 */
public record CreatePermissionCommand(
  ResourceType resource,
  OperationType operation,
  ScopeType scope,
  String description
) { }
