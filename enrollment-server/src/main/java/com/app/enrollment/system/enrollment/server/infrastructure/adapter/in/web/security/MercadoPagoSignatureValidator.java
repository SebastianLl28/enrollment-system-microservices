package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web.security;

import com.app.enrollment.system.enrollment.server.infrastructure.server.config.MercadoPagoProperties;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Locale;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Valida el header x-signature que Mercado Pago firma con la clave secreta del webhook.
 * El manifest firmado es: "id:[data.id];request-id:[x-request-id];ts:[ts];"
 * (ver https://www.mercadopago.com/developers → Webhooks → Validación de origen).
 *
 * @author Alonso
 */
@Component
public class MercadoPagoSignatureValidator {

  private static final Logger log = LoggerFactory.getLogger(MercadoPagoSignatureValidator.class);

  private final MercadoPagoProperties properties;

  public MercadoPagoSignatureValidator(MercadoPagoProperties properties) {
    this.properties = properties;
  }

  public boolean isValid(String xSignature, String xRequestId, String dataId) {

    String secret = properties.webhookSecret();
    if (secret == null || secret.isBlank()) {
      log.warn("MP_WEBHOOK_SECRET is not configured: skipping webhook signature validation");
      return true;
    }

    if (xSignature == null || xSignature.isBlank()) {
      return false;
    }

    String ts = null;
    String v1 = null;
    for (String part : xSignature.split(",")) {
      String[] keyValue = part.split("=", 2);
      if (keyValue.length != 2) {
        continue;
      }
      switch (keyValue[0].trim()) {
        case "ts" -> ts = keyValue[1].trim();
        case "v1" -> v1 = keyValue[1].trim();
        default -> { }
      }
    }

    if (ts == null || v1 == null) {
      return false;
    }

    StringBuilder manifest = new StringBuilder();
    if (dataId != null && !dataId.isBlank()) {
      // Mercado Pago exige el data.id en minúsculas dentro del manifest
      manifest.append("id:").append(dataId.toLowerCase(Locale.ROOT)).append(";");
    }
    if (xRequestId != null && !xRequestId.isBlank()) {
      manifest.append("request-id:").append(xRequestId).append(";");
    }
    manifest.append("ts:").append(ts).append(";");

    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
      byte[] hash = mac.doFinal(manifest.toString().getBytes(StandardCharsets.UTF_8));
      String expected = HexFormat.of().formatHex(hash);
      return MessageDigest.isEqual(
        expected.getBytes(StandardCharsets.UTF_8),
        v1.toLowerCase(Locale.ROOT).getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      log.error("Error validating Mercado Pago signature: {}", e.getMessage());
      return false;
    }
  }
}
