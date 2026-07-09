package com.app.enrollment.system.enrollment.server.domain.exception;

/**
 * El pago consultado no existe en Mercado Pago (p. ej. notificaciones de prueba
 * del simulador o ids corruptos). Reintentar no lo va a resolver.
 *
 * @author Alonso
 */
public class PaymentNotFoundException extends RuntimeException {

  public PaymentNotFoundException(String message) {
    super(message);
  }
}
