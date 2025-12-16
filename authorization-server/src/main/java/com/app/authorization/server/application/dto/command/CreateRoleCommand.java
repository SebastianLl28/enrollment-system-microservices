package com.app.authorization.server.application.dto.command;

import java.util.Set;

/**
 * @author Alonso
 */
public record CreateRoleCommand(
  String name,
  String description,
  Set<Integer> permissionIds,
  Set<String> viewCodes
) { }
