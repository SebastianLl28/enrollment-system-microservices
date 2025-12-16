package com.app.authorization.server.infrastructure.adapter.out.token;

import com.app.common.annotation.Adapter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Alonso
 */
@Adapter
public class JwtTokenService {
  
  private static final Logger logger = LoggerFactory.getLogger(JwtTokenService.class);
  
  private final SecretKey signingKey;
  private final long accessExpirationMillis;
  private final long twoFactorExpirationMillis;
  
  public JwtTokenService(
    @Value("${jwt.secret}") String secret,
    @Value("${jwt.expiration}") long accessExpirationMillis,
    @Value("${jwt.two-factor-expiration}") long twoFactorExpirationMillis) {
    
    this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.accessExpirationMillis = accessExpirationMillis;
    this.twoFactorExpirationMillis = twoFactorExpirationMillis;
  }
  
  public SecretKey getSigningKey() {
    return signingKey;
  }
  
  public Date accessExpiryFromNow() {
    return new Date(System.currentTimeMillis() + accessExpirationMillis);
  }
  
  public Date twoFactorExpiryFromNow() {
    return new Date(System.currentTimeMillis() + twoFactorExpirationMillis);
  }
  
  public Jws<Claims> parseClaims(String token) {
    return Jwts.parser()
      .verifyWith(signingKey)
      .build()
      .parseSignedClaims(token);
  }
  
  public boolean isValid(String token) {
    try {
      parseClaims(token);
      return true;
    } catch (MalformedJwtException ex) {
      logger.error("Invalid JWT token: {}", ex.getMessage());
    } catch (ExpiredJwtException ex) {
      logger.error("Expired JWT token: {}", ex.getMessage());
    } catch (UnsupportedJwtException ex) {
      logger.error("Unsupported JWT token: {}", ex.getMessage());
    } catch (IllegalArgumentException ex) {
      logger.error("JWT claims string is empty: {}", ex.getMessage());
    }
    return false;
  }

}
