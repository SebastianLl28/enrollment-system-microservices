package com.app.authorization.server.application.dto.command;

import java.util.Set;

public record UpdateRoleCommand(
  Integer id,
  String name,
  String description,
  Set<Integer> permissionIds,
  Set<String> viewCodes
) { }
