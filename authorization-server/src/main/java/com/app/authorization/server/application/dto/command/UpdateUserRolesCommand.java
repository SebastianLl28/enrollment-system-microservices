package com.app.authorization.server.application.dto.command;

import java.util.Set;

public record UpdateUserRolesCommand(
  Integer userId,
  Set<Integer> roleIds
) { }
