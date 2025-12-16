package com.app.authorization.server.application.dto.command;

public record UpdateUIViewCommand(
  String code,
  String route,
  String label,
  String module,
  Integer sortOrder,
  boolean active
) { }
