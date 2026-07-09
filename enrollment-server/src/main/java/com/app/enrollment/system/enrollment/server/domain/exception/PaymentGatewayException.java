package com.app.enrollment.system.enrollment.server.domain.exception;

/**
 * @author Alonso
 */
public class PaymentGatewayException extends RuntimeException {

  public PaymentGatewayException(String message) {
    super(message);
  }

  public PaymentGatewayException(String message, Throwable cause) {
    super(message, cause);
  }
}
