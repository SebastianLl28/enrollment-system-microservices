package com.app.enrollment.system.enrollment.server.infrastructure.server.config;

import java.math.BigDecimal;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuración de Mercado Pago (Checkout Pro).
 *
 * @author Alonso
 */
@ConfigurationProperties(prefix = "mercadopago")
public record MercadoPagoProperties(
  String accessToken,
  String webhookSecret,
  String notificationUrl,
  String backUrlsBase,
  BigDecimal enrollmentFee,
  String currencyId
) {

}
