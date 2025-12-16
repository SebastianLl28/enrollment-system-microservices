package com.app.authorization.server.infrastructure.adapter.out.token;

import com.app.authorization.server.domain.repository.AccessTokenPort;
import com.app.common.annotation.Adapter;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Adapter
public class AccessTokenPortAdapter implements AccessTokenPort {
  
  
  private static final Logger logger = LoggerFactory.getLogger(AccessTokenPortAdapter.class);
  
  private final JwtTokenService jwt;
  
  public AccessTokenPortAdapter(JwtTokenService jwt) {
    this.jwt = jwt;
  }
  
  @Override
  public String generateToken(String username, Set<String> permissions) {
    Date now = new Date();
    Date expiryDate = jwt.accessExpiryFromNow();
    
    return Jwts.builder()
      .subject(username)
      .issuedAt(now)
      .expiration(expiryDate)
      .claim("type", "ACCESS")
      .claim("permissions", permissions)
      .signWith(jwt.getSigningKey())
      .compact();
  }
  
  @Override
  public String extractUsername(String token) {
    try {
      return jwt.parseClaims(token)
        .getPayload()
        .getSubject();
    } catch (Exception ex) {
      logger.error("Error extracting username from token: {}", ex.getMessage());
      return null;
    }
  }
  
  @Override
  public boolean validateToken(String token) {
    return jwt.isValid(token);
  }
}
