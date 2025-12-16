package com.app.enrollment.system.event.store.server.config;

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
