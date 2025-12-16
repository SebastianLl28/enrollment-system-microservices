package com.app.api.gateway.config;

import static com.app.common.constant.ApiConstants.PUBLIC_ENDPOINTS;

import java.util.List;
import java.util.function.Predicate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class RouterValidator {
  
  public static final List<String> publicEndpoints = List.of(
    PUBLIC_ENDPOINTS
  );
  
  public Predicate<ServerHttpRequest> isSecured =
    request -> publicEndpoints.stream()
      .noneMatch(uri -> request.getURI().getPath().contains(uri));
  
}

