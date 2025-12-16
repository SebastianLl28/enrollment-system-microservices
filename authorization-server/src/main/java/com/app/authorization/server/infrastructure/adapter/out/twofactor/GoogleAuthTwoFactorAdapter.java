package com.app.authorization.server.infrastructure.adapter.out.twofactor;

import com.app.authorization.server.domain.repository.TwoFactorPort;
import com.app.common.annotation.Adapter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import java.net.URLEncoder;

/**
 * @author Alonso
 */
@Adapter
public class GoogleAuthTwoFactorAdapter implements TwoFactorPort {
  
  private final GoogleAuthenticator googleAuthenticator;
  
  @Value("${twofactor.issuer}")
  private String issuer;
  
  public GoogleAuthTwoFactorAdapter() {
    this.googleAuthenticator = new GoogleAuthenticator();
  }
  
  @Override
  public String generateSecret(String username) {
    // Usa createCredentials() SIN parámetros
    GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
    return key.getKey();
  }
  
  @Override
  public String buildOtpAuthUrl(String username, String secret) {
    return String.format(
      "otpauth://totp/%s:%s?secret=%s&issuer=%s&digits=6&period=30",
      urlEncode(issuer),
      urlEncode(username),
      secret,
      urlEncode(issuer)
    );
  }
  
  @Override
  public boolean verifyCode(String secret, String code) {
    int numCode = Integer.parseInt(code);
    return googleAuthenticator.authorize(secret, numCode);
  }
  
  private String urlEncode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }
}
