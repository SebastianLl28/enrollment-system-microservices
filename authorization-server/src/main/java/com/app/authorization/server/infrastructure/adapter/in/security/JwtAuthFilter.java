package com.app.authorization.server.infrastructure.adapter.in.security;

import com.app.authorization.server.domain.repository.AccessTokenPort;
import com.app.authorization.server.infrastructure.adapter.out.token.TwoFactorTokenPortAdapter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author Alonso
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
  
  private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
  
  
  private final AccessTokenPort accessTokenPort;
  private final TwoFactorTokenPortAdapter twoFactorTokenPortAdapter;
  
  public JwtAuthFilter(AccessTokenPort accessTokenPort, TwoFactorTokenPortAdapter twoFactorTokenPortAdapter) {
    this.accessTokenPort = accessTokenPort;
    this.twoFactorTokenPortAdapter = twoFactorTokenPortAdapter;
  }
  
  @Override
  protected void doFilterInternal(HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain)
    throws ServletException, IOException {

    final String authHeader = request.getHeader("Authorization");
    final String token;
    final String username;

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    token = authHeader.substring(7);

    try {
      if (!accessTokenPort.validateToken(token)) {
        filterChain.doFilter(request, response);
        return;
      }

      if (twoFactorTokenPortAdapter.isTwoFactorToken(token)) {
        filterChain.doFilter(request, response);
        return;
      }

      username = accessTokenPort.extractUsername(token);

      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UsernamePasswordAuthenticationToken authToken =
          new UsernamePasswordAuthenticationToken(
            username,
            null,
            Collections.emptyList()
          );

        SecurityContextHolder.getContext().setAuthentication(authToken);
      }

    } catch (Exception e) {
      logger.warn("Invalid JWT Token: " + e.getMessage());
    }

    filterChain.doFilter(request, response);
  }

}

