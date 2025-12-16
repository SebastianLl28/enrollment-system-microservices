package com.app.authorization.server.application.dto.command;

public record CreateUIViewCommand(
  String code,
  String route,
  String label,
  String module,
  Integer sortOrder
) { }
