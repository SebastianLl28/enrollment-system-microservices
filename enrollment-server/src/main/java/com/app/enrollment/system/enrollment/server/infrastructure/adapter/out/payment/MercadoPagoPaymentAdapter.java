package com.app.enrollment.system.enrollment.server.infrastructure.adapter.out.payment;

import com.app.common.annotation.Adapter;
import com.app.enrollment.system.enrollment.server.application.dto.command.CreatePaymentPreferenceCommand;
import com.app.enrollment.system.enrollment.server.application.dto.response.PaymentDetailsResponse;
import com.app.enrollment.system.enrollment.server.application.dto.response.PaymentPreferenceResponse;
import com.app.enrollment.system.enrollment.server.application.port.out.PaymentGatewayPort;
import com.app.enrollment.system.enrollment.server.domain.exception.PaymentGatewayException;
import com.app.enrollment.system.enrollment.server.domain.exception.PaymentNotFoundException;
import com.app.enrollment.system.enrollment.server.infrastructure.server.config.MercadoPagoProperties;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Adaptador hacia Mercado Pago usando el SDK oficial (Checkout Pro).
 *
 * @author Alonso
 */
@Adapter
public class MercadoPagoPaymentAdapter implements PaymentGatewayPort {

  private final MercadoPagoProperties properties;

  public MercadoPagoPaymentAdapter(MercadoPagoProperties properties) {
    this.properties = properties;
  }

  @Override
  public PaymentPreferenceResponse createPreference(CreatePaymentPreferenceCommand command) {

    // Precio del CareerOffering (carrera-periodo); ofertas sin precio usan la tarifa por defecto
    BigDecimal unitPrice = command.amount() != null ? command.amount() : properties.enrollmentFee();

    PreferenceItemRequest item = PreferenceItemRequest.builder()
      .id(command.enrollmentId().toString())
      .title("Matrícula - " + command.careerName() + " (" + command.termCode() + ")")
      .description("Matrícula de " + command.studentFullName()
        + " en la carrera " + command.careerName() + ", periodo " + command.termCode())
      .quantity(1)
      .currencyId(properties.currencyId())
      .unitPrice(unitPrice)
      .build();

    PreferencePayerRequest payer = PreferencePayerRequest.builder()
      .name(command.studentFullName())
      .email(command.studentEmail())
      .build();

    String backUrlsBase = trimTrailingSlash(properties.backUrlsBase());

    PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
      .success(backUrlsBase + "/success")
      .pending(backUrlsBase + "/pending")
      .failure(backUrlsBase + "/failure")
      .build();

    PreferenceRequest.PreferenceRequestBuilder requestBuilder = PreferenceRequest.builder()
      .items(List.of(item))
      .payer(payer)
      .externalReference(command.enrollmentId().toString())
      .notificationUrl(properties.notificationUrl())
      .backUrls(backUrls);

    // auto_return solo es aceptado por Mercado Pago con back_urls públicas (https)
    if (backUrlsBase.startsWith("https://")) {
      requestBuilder.autoReturn("approved");
    }

    try {
      Preference preference = new PreferenceClient().create(requestBuilder.build());
      return new PaymentPreferenceResponse(preference.getId(), preference.getInitPoint());
    } catch (MPApiException e) {
      throw new PaymentGatewayException(
        "Mercado Pago rejected the preference for enrollment " + command.enrollmentId()
          + ": " + e.getApiResponse().getContent(), e);
    } catch (MPException e) {
      throw new PaymentGatewayException(
        "Error creating Mercado Pago preference for enrollment " + command.enrollmentId(), e);
    }
  }

  @Override
  public PaymentDetailsResponse getPayment(String paymentId) {
    try {
      Payment payment = new PaymentClient().get(Long.valueOf(paymentId));

      Instant dateApproved =
        payment.getDateApproved() != null ? payment.getDateApproved().toInstant() : null;

      return new PaymentDetailsResponse(
        payment.getId().toString(),
        payment.getStatus(),
        payment.getExternalReference(),
        dateApproved
      );
    } catch (MPApiException e) {
      if (e.getApiResponse() != null && e.getApiResponse().getStatusCode() == 404) {
        throw new PaymentNotFoundException(
          "Payment " + paymentId + " does not exist in Mercado Pago");
      }
      throw new PaymentGatewayException(
        "Mercado Pago rejected the payment lookup " + paymentId + ": "
          + e.getApiResponse().getContent(), e);
    } catch (MPException e) {
      throw new PaymentGatewayException("Error fetching Mercado Pago payment " + paymentId, e);
    } catch (NumberFormatException e) {
      throw new PaymentGatewayException("Invalid Mercado Pago payment id: " + paymentId, e);
    }
  }

  private String trimTrailingSlash(String url) {
    return url != null && url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
  }
}
