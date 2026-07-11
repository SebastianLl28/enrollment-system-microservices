package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.app.enrollment.system.enrollment.server.infrastructure.server.config.MercadoPagoProperties;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.Test;

class MercadoPagoSignatureValidatorTest {

  private static final String SECRET = "test-webhook-secret";
  private static final String TS = "1752230000";
  private static final String REQUEST_ID = "req-abc-123";
  private static final String DATA_ID = "12345";

  private MercadoPagoSignatureValidator validatorWithSecret(String secret) {
    return new MercadoPagoSignatureValidator(
      new MercadoPagoProperties("token", secret, null, null, null, "PEN"));
  }

  /** Firma el manifest igual que Mercado Pago: id:[data.id];request-id:[rid];ts:[ts]; */
  private String sign(String secret, String dataId, String requestId, String ts)
    throws Exception {
    String manifest = "id:" + dataId + ";request-id:" + requestId + ";ts:" + ts + ";";
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
    return HexFormat.of().formatHex(mac.doFinal(manifest.getBytes(StandardCharsets.UTF_8)));
  }

  @Test
  void acceptsValidSignature() throws Exception {
    String v1 = sign(SECRET, DATA_ID, REQUEST_ID, TS);
    String header = "ts=" + TS + ",v1=" + v1;

    assertThat(validatorWithSecret(SECRET).isValid(header, REQUEST_ID, DATA_ID)).isTrue();
  }

  @Test
  void rejectsTamperedSignature() throws Exception {
    String v1 = sign("otro-secreto", DATA_ID, REQUEST_ID, TS);
    String header = "ts=" + TS + ",v1=" + v1;

    assertThat(validatorWithSecret(SECRET).isValid(header, REQUEST_ID, DATA_ID)).isFalse();
  }

  @Test
  void rejectsMissingHeaderWhenSecretConfigured() {
    assertThat(validatorWithSecret(SECRET).isValid(null, REQUEST_ID, DATA_ID)).isFalse();
    assertThat(validatorWithSecret(SECRET).isValid("", REQUEST_ID, DATA_ID)).isFalse();
  }

  @Test
  void rejectsHeaderWithoutTsOrV1() {
    assertThat(validatorWithSecret(SECRET).isValid("v1=abc", REQUEST_ID, DATA_ID)).isFalse();
    assertThat(validatorWithSecret(SECRET).isValid("ts=123", REQUEST_ID, DATA_ID)).isFalse();
  }

  @Test
  void skipsValidationWhenSecretNotConfigured() {
    // Sin MP_WEBHOOK_SECRET se acepta con warning (comportamiento documentado).
    assertThat(validatorWithSecret(null).isValid(null, REQUEST_ID, DATA_ID)).isTrue();
    assertThat(validatorWithSecret("").isValid("cualquier-cosa", REQUEST_ID, DATA_ID)).isTrue();
  }
}
