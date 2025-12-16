package com.app.authorization.server.infrastructure.adapter.out.hashing;

import com.app.authorization.server.domain.repository.PasswordHasher;
import com.app.common.annotation.Adapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Adapter
public class PasswordHasherAdapter implements PasswordHasher {
  
  private final PasswordEncoder passwordEncoder;
  
  public PasswordHasherAdapter(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }
  
  @Override
  public String hash(String plainPassword) {
    return passwordEncoder.encode(plainPassword);
  }
  
  @Override
  public Boolean verify(String plainPassword, String hashedPassword) {
    return passwordEncoder.matches(plainPassword, hashedPassword);
  }
}
