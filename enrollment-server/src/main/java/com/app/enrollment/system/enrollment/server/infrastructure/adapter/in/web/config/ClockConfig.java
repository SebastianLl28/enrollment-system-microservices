package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alonso
 */
@Configuration
public class ClockConfig {
  
  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }
  
}
