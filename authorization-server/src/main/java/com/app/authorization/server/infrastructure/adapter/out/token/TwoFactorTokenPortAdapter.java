package com.app.authorization.server.infrastructure.adapter.out.token;

import com.app.authorization.server.domain.repository.TwoFactorTokenPort;
import com.app.common.annotation.Adapter;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.Set;

/**
 * @author Alonso
 */
@Adapter
public class TwoFactorTokenPortAdapter implements TwoFactorTokenPort {
  
  private final JwtTokenService jwt;
  
  public TwoFactorTokenPortAdapter(JwtTokenService jwt) {
    this.jwt = jwt;
  }
  
  @Override
  public String generateTwoFactorToken(String username, Set<String> permissions) {
    Date now = new Date();
    Date expiryDate = jwt.twoFactorExpiryFromNow();
    
    return Jwts.builder()
      .subject(username)
      .issuedAt(now)
      .expiration(expiryDate)
      .signWith(jwt.getSigningKey())
      .claim("type", "2FA")
      .claim("permissions", permissions)
      .compact();
  }
  
  @Override
  public boolean isTwoFactorToken(String token) {
    if (!jwt.isValid(token)) return false;
    
    String type = jwt.parseClaims(token)
      .getPayload()
      .get("type", String.class);
    
    return "2FA".equals(type);
  }
  
}
