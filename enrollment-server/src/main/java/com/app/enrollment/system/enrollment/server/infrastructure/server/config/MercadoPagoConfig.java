package com.app.enrollment.system.enrollment.server.infrastructure.server.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Inicializa el SDK oficial de Mercado Pago con el access token.
 *
 * @author Alonso
 */
@Configuration
@EnableConfigurationProperties(MercadoPagoProperties.class)
public class MercadoPagoConfig {

  private static final Logger log = LoggerFactory.getLogger(MercadoPagoConfig.class);

  private final MercadoPagoProperties properties;

  public MercadoPagoConfig(MercadoPagoProperties properties) {
    this.properties = properties;
  }

  @PostConstruct
  public void init() {
    if (properties.accessToken() == null || properties.accessToken().isBlank()) {
      log.warn("MP_ACCESS_TOKEN is not configured: Mercado Pago integration is disabled");
      return;
    }
    com.mercadopago.MercadoPagoConfig.setAccessToken(properties.accessToken());
    log.info("Mercado Pago SDK initialized");
  }
}
