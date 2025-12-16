package com.app.enrollment.system.event.store.server.config;

import com.netflix.discovery.shared.transport.jersey3.Jersey3TransportClientFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alonso
 */
@Configuration
public class EurekaJerseyTransportConfig {
  
  @Bean
  public Jersey3TransportClientFactories jersey3TransportClientFactories() {
    return new Jersey3TransportClientFactories();
  }
}
