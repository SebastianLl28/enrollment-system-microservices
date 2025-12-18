package com.app.api.gateway.config;

import com.app.api.gateway.filters.AuthFilter;
import com.app.api.gateway.filters.EventFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouterConfig {
  
  @Bean
  public RouteLocator routes(RouteLocatorBuilder builder, AuthFilter authFilter,
    EventFilter eventFilter) {
    return builder.routes()
      
      .route("auth-service-route", r -> r.path("/auth/**")
        .filters(f -> f.stripPrefix(1).filter(eventFilter.apply(new EventFilter.Config())))
        .uri("lb://authorization-server"))
      
      
      .route("auth-service-docs", r -> r.path("/v3/api-docs/auth-service")
        .filters(f -> f.rewritePath("/v3/api-docs/auth-service", "/v3/api-docs"))
        .uri("lb://authorization-server"))
      
      .route("enrollment-service-docs", r -> r.path("/v3/api-docs/enrollment-service")
        .filters(f -> f.rewritePath("/v3/api-docs/enrollment-service", "/v3/api-docs"))
        .uri("lb://enrollment-server"))
      
      .route("enrollment-service",
        r -> r.path("/api/**").filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
          .uri("lb://enrollment-server")).build();
    
  }
}
