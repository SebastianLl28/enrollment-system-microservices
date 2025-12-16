package com.app.authorization.server.application.dto.command;

public record VerifyTwoFactorCommand(
  String code,
  String tempToken
) {

}
