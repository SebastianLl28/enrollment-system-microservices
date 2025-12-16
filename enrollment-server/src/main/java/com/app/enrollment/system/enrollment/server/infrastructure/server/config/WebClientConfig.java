package com.app.enrollment.system.enrollment.server.infrastructure.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Alonso
 */
@Configuration
public class WebClientConfig {
  
  @Bean
  public WebClient.Builder webClientBuilder() {
    return WebClient.builder();
  }
  
}

