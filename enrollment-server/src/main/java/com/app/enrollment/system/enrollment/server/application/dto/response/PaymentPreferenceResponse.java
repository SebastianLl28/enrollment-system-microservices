package com.app.enrollment.system.enrollment.server.application.dto.response;

/**
 * Preferencia de pago creada en Mercado Pago (Checkout Pro).
 *
 * @author Alonso
 */
public record PaymentPreferenceResponse(
  String preferenceId,
  String initPoint
) {

}
