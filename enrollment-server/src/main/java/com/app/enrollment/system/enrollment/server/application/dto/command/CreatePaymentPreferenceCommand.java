package com.app.enrollment.system.enrollment.server.application.dto.command;

import java.math.BigDecimal;

/**
 * Datos necesarios para crear una Preferencia de Pago (Checkout Pro) de una inscripción.
 * El amount es el precio del CourseOffering (curso-periodo); si es null, la pasarela
 * usa la tarifa por defecto configurada.
 *
 * @author Alonso
 */
public record CreatePaymentPreferenceCommand(
  Integer enrollmentId,
  String courseName,
  String studentFullName,
  String studentEmail,
  BigDecimal amount
) {

}
