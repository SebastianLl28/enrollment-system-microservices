package com.app.enrollment.system.enrollment.server.application.port.in;

/**
 * Procesa una notificación (webhook) de pago de Mercado Pago.
 *
 * @author Alonso
 */
public interface ProcessPaymentNotificationUseCase {

  void processPaymentNotification(String paymentId);

}
