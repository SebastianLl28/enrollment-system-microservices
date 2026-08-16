package com.app.api.gateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Alonso
 */
@Configuration
public class WebClientConfig {

  @Configuration
  @Profile("!k8s")
  static class LoadBalancedWebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
      return WebClient.builder();
    }
  }
  
  @Configuration
  @Profile("k8s")
  static class PlainWebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
      return WebClient.builder();
    }
  }
}
