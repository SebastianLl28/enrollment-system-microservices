package com.app.enrollment.system.enrollment.server.application.dto.response;

import java.time.Instant;

/**
 * Detalle de un pago consultado en la API de Mercado Pago.
 *
 * @author Alonso
 */
public record PaymentDetailsResponse(
  String paymentId,
  String status,
  String externalReference,
  Instant dateApproved
) {

}
