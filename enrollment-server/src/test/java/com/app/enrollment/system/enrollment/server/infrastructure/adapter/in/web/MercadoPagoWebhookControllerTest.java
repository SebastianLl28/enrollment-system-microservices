package com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.enrollment.system.enrollment.server.application.port.in.ProcessPaymentNotificationUseCase;
import com.app.enrollment.system.enrollment.server.domain.exception.PaymentNotFoundException;
import com.app.enrollment.system.enrollment.server.infrastructure.adapter.in.web.security.MercadoPagoSignatureValidator;
import com.app.enrollment.system.enrollment.server.infrastructure.server.security.PermissionInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MercadoPagoWebhookController.class)
class MercadoPagoWebhookControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProcessPaymentNotificationUseCase processPaymentNotificationUseCase;
  @MockBean
  private MercadoPagoSignatureValidator signatureValidator;
  @MockBean
  private PermissionInterceptor permissionInterceptor;

  @BeforeEach
  void allowAllRequests() throws Exception {
    when(permissionInterceptor.preHandle(any(), any(), any())).thenReturn(true);
  }

  @Test
  void paymentNotificationWithValidSignatureIsProcessed() throws Exception {
    when(signatureValidator.isValid(any(), any(), any())).thenReturn(true);

    mockMvc.perform(post("/webhooks/mercadopago")
        .param("type", "payment")
        .param("data.id", "123"))
      .andExpect(status().isOk());

    verify(processPaymentNotificationUseCase).processPaymentNotification("123");
  }

  @Test
  void invalidSignatureIsRejectedWith401() throws Exception {
    when(signatureValidator.isValid(any(), any(), any())).thenReturn(false);

    mockMvc.perform(post("/webhooks/mercadopago")
        .param("type", "payment")
        .param("data.id", "123"))
      .andExpect(status().isUnauthorized());

    verify(processPaymentNotificationUseCase, never()).processPaymentNotification(any());
  }

  @Test
  void nonPaymentTopicsAreIgnoredWith200() throws Exception {
    mockMvc.perform(post("/webhooks/mercadopago")
        .param("topic", "merchant_order")
        .param("id", "123"))
      .andExpect(status().isOk());

    verify(processPaymentNotificationUseCase, never()).processPaymentNotification(any());
  }

  @Test
  void missingPaymentInMercadoPagoStillReturns200SoMpStopsRetrying() throws Exception {
    when(signatureValidator.isValid(any(), any(), any())).thenReturn(true);
    doThrow(new PaymentNotFoundException("payment 123 does not exist"))
      .when(processPaymentNotificationUseCase).processPaymentNotification("123");

    mockMvc.perform(post("/webhooks/mercadopago")
        .param("type", "payment")
        .param("data.id", "123"))
      .andExpect(status().isOk());
  }
}
