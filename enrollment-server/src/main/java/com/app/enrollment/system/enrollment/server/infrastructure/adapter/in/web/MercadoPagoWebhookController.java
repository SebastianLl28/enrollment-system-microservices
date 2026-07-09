package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import com.app.enrollment.system.enrollment.server.application.port.in.ProcessPaymentNotificationUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.EnrollmentNotFoundException;
import com.app.enrollment.system.enrollment.server.domain.exception.PaymentNotFoundException;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web.security.MercadoPagoSignatureValidator;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Webhook de Mercado Pago. Vive fuera de /api/v1 a propósito: Mercado Pago no puede
 * autenticarse con JWT, así que el gateway enruta /webhooks/** sin AuthFilter y la
 * autenticidad se verifica con la firma HMAC del header x-signature.
 *
 * @author Alonso
 */
@RestController
@RequestMapping("/webhooks")
public class MercadoPagoWebhookController {

  private static final Logger log = LoggerFactory.getLogger(MercadoPagoWebhookController.class);

  private static final Set<String> PAYMENT_TOPICS = Set.of("payment");

  private final ProcessPaymentNotificationUseCase processPaymentNotificationUseCase;
  private final MercadoPagoSignatureValidator signatureValidator;

  public MercadoPagoWebhookController(
    ProcessPaymentNotificationUseCase processPaymentNotificationUseCase,
    MercadoPagoSignatureValidator signatureValidator) {
    this.processPaymentNotificationUseCase = processPaymentNotificationUseCase;
    this.signatureValidator = signatureValidator;
  }

  @PostMapping("/mercadopago")
  public ResponseEntity<Void> handleNotification(
    @RequestParam(name = "type", required = false) String type,
    @RequestParam(name = "topic", required = false) String topic,
    @RequestParam(name = "data.id", required = false) String dataId,
    @RequestParam(name = "id", required = false) String id,
    @RequestHeader(name = "x-signature", required = false) String xSignature,
    @RequestHeader(name = "x-request-id", required = false) String xRequestId,
    @RequestBody(required = false) JsonNode body) {

    String notificationType = firstNonBlank(type, topic,
      body != null && body.hasNonNull("type") ? body.get("type").asText() : null);

    String paymentId = firstNonBlank(dataId, id,
      body != null && body.has("data") && body.get("data").hasNonNull("id")
        ? body.get("data").get("id").asText() : null);

    log.info("Mercado Pago webhook received: type={}, paymentId={}", notificationType, paymentId);

    // Mercado Pago también notifica merchant_order, chargebacks, etc. Solo procesamos pagos.
    if (notificationType == null || !PAYMENT_TOPICS.contains(notificationType.toLowerCase())) {
      return ResponseEntity.ok().build();
    }

    if (paymentId == null) {
      log.warn("Payment notification without an id, ignoring");
      return ResponseEntity.ok().build();
    }

    if (!signatureValidator.isValid(xSignature, xRequestId, dataId)) {
      log.warn("Mercado Pago webhook rejected: invalid x-signature (paymentId={})", paymentId);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    try {
      processPaymentNotificationUseCase.processPaymentNotification(paymentId);
    } catch (EnrollmentNotFoundException e) {
      // Reintentar no lo va a arreglar: respondemos 200 para que MP no reenvíe eternamente
      log.error("Webhook for payment {} references a missing enrollment: {}", paymentId,
        e.getMessage());
    } catch (PaymentNotFoundException e) {
      // Pago inexistente en MP (p. ej. "Simular notificación" del panel): 200 y a otra cosa
      log.warn("Webhook for a payment that does not exist in Mercado Pago: {}", e.getMessage());
    }

    return ResponseEntity.ok().build();
  }

  private String firstNonBlank(String... values) {
    for (String value : values) {
      if (value != null && !value.isBlank()) {
        return value;
      }
    }
    return null;
  }
}
