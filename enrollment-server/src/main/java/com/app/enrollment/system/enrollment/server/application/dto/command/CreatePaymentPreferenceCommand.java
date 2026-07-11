package com.app.enrollment.system.enrollment.server.application.dto.command;

import java.math.BigDecimal;

/**
 * Datos necesarios para crear una Preferencia de Pago (Checkout Pro) de una matrícula.
 * El amount es el precio del CareerOffering (carrera-periodo); si es null, la pasarela
 * usa la tarifa por defecto configurada.
 *
 * @author Alonso
 */
public record CreatePaymentPreferenceCommand(
  Integer enrollmentId,
  String careerName,
  String termCode,
  String studentFullName,
  String studentEmail,
  BigDecimal amount
) {

}
