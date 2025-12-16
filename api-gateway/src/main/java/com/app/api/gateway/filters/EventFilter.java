package com.app.api.gateway.filters;

import com.app.api.gateway.filters.EventFilter.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author Alonso
 */
@Component
public class EventFilter extends AbstractGatewayFilterFactory<Config> {
  
  private final Logger logger = LoggerFactory.getLogger(EventFilter.class);
  
  public EventFilter() {
    super(Config.class);
  }
  
  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      long startTime = System.currentTimeMillis();
      logger.info("📡 [Event Filter] Iniciando evento...");
      
      return chain.filter(exchange).then(Mono.fromRunnable(() -> {
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("🏁 [Event Filter] Respuesta recibida: "
          + exchange.getResponse().getStatusCode()
          + " (Tiempo: " + duration + "ms)");
      }));
    };
  }
  
  public static class Config {}
}
