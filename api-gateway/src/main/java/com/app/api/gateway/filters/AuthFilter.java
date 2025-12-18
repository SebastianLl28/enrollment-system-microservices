package com.app.api.gateway.filters;

import com.app.api.gateway.config.RouterValidator;
import com.app.api.gateway.filters.AuthFilter.Config;
import com.app.common.constant.ApiConstants;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Alonso
 */
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<Config> {
  
  
  private final Logger log = LoggerFactory.getLogger(AuthFilter.class);
  
  private final WebClient webClient;
  
  private final RouterValidator routerValidator;
  
  
  public AuthFilter(WebClient.Builder webClientBuilder, RouterValidator routerValidator) {
    super(Config.class);
    this.webClient = webClientBuilder.baseUrl("lb://authorization-server")
      .build();
    this.routerValidator = routerValidator;
  }
  
  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      
      var request = exchange.getRequest();
      
      if (!routerValidator.isSecured.test(request)) {
        return chain.filter(exchange);
      }
      
      String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
      
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED,
          "Missing or invalid Authorization header"));
      }
      
      
      
      return webClient.get().uri("/validateToken")
        .header(HttpHeaders.AUTHORIZATION, authHeader).retrieve()
        .bodyToMono(ValidationResponse.class).flatMap(response -> {
          
          if (!response.isValid()) {
            return Mono.error(
              new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token"));
          }
          
          ServerWebExchange mutatedExchange = exchange.mutate()
            .request(builder -> builder.headers(headers -> {
              headers.remove(ApiConstants.HEADER_USER_ID);
              headers.remove(ApiConstants.HEADER_USER_NAME);
              headers.remove("X-User-Permissions");
              headers.add(ApiConstants.HEADER_USER_ID, String.valueOf(response.getUserId()));
              headers.add(ApiConstants.HEADER_USER_NAME, response.getUsername());
              if (response.getPermissions() != null && !response.getPermissions().isEmpty()) {
                headers.add("X-User-Permissions", String.join(",", response.getPermissions()));
              }
              headers.set(HttpHeaders.AUTHORIZATION, authHeader);
            })).build();
          
          return chain.filter(mutatedExchange);
          
        }).onErrorResume(e -> {
          log.error("❌ [Auth Filter] Token validation error: {}", e.getMessage());
          
          if (e instanceof ResponseStatusException) {
            return Mono.error(e);
          }
          return Mono.error(
            new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token validation failed", e));
        });
    };
  }
  
  public static class Config {
  
  }
  
  public static class ValidationResponse {
    
    private String username;
    private boolean valid;
    private Integer userId;
    private Set<String> permissions;
    
    public ValidationResponse() {
    }
    
    public String getUsername() {
      return username;
    }
    
    public void setUsername(String username) {
      this.username = username;
    }
    
    public boolean isValid() {
      return valid;
    }
    
    public void setValid(boolean valid) {
      this.valid = valid;
    }
    
    public Integer getUserId() {
      return userId;
    }
    
    public void setUserId(Integer userId) {
      this.userId = userId;
    }
    
    public Set<String> getPermissions() {
      return permissions;
    }
    
    public void setPermissions(Set<String> permissions) {
      this.permissions = permissions;
    }
    
    @Override
    public String toString() {
      return "ValidationResponse{" +
        "username='" + username + '\'' +
        ", valid=" + valid +
        ", userId=" + userId +
        ", permissions=" + permissions +
        '}';
    }
  }
}
