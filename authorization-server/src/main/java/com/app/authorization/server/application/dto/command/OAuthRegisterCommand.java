package com.app.authorization.server.application.dto.command;

public record OAuthRegisterCommand(
  String provider,
  String providerUserId,
  String username,
  String name,
  String email
) {

}
