package com.app.enrollment.system.enrollment.server.application.port.out;

import com.app.enrollment.system.enrollment.server.application.dto.command.CreatePaymentPreferenceCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.PaymentDetailsResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.PaymentPreferenceResponse;

/**
 * Puerto de salida hacia la pasarela de pagos (Mercado Pago).
 *
 * @author Alonso
 */
public interface PaymentGatewayPort {

  PaymentPreferenceResponse createPreference(CreatePaymentPreferenceCommand command);

  PaymentDetailsResponse getPayment(String paymentId);

}
