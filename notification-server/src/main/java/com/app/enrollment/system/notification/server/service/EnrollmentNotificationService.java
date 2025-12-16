package com.app.enrollment.system.notification.server.service;

import com.app.common.enums.EnrollmentStatus;
import com.app.common.events.EnrollmentAssignedEvent;
import com.app.enrollment.system.notification.server.email.EmailSender;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

/**
 * @author Alonso
 */
@Service
public class EnrollmentNotificationService {
  
  private final EmailSender emailSender;
  
  private static final DateTimeFormatter DateTimeFormatter = java.time.format.DateTimeFormatter
    .ofPattern("yyyy-MM-dd HH:mm:ss")
    .withZone(java.time.ZoneId.of("America/Lima"));
  
  
  public EnrollmentNotificationService(EmailSender emailSender) {
    this.emailSender = emailSender;
  }
  
  public void sendEnrollmentEmail(EnrollmentAssignedEvent event) {
    EmailContent content = buildEmailContent(event);
    
    // Si no hay email, no intentes mandar
    if (event.getStudentEmail() == null || event.getStudentEmail().isBlank()) {
      throw new IllegalArgumentException("Student email is empty for enrollmentId=" + event.getEnrollmentId());
    }
    
    emailSender.sendHtml(event.getStudentEmail(), content.subject(), content.body());
  }
  
  private EmailContent buildEmailContent(EnrollmentAssignedEvent event) {
    EnrollmentStatus status = event.getEnrollmentStatus();
    
    String name = safe(event.getStudentFullName());
    String course = safe(event.getCourseName());
    String enrollmentId = safe(event.getEnrollmentId());
    
    String occurredAt = (event.getOccurredAt() != null)
      ? DateTimeFormatter.format(event.getOccurredAt())
      : "N/A";
    
    return switch (status) {
      case PENDING -> new EmailContent(
        "Inscripción registrada (pago pendiente) - " + course,
        buildHtmlTemplate(
          "⏳ Inscripción Registrada",
          "#FFA500",
          name,
          "Tu inscripción al curso <strong>\"" + course + "\"</strong> ya fue registrada correctamente.",
          "Estado: <span style='color: #FFA500; font-weight: bold;'>PENDIENTE DE PAGO</span>",
          "Cuando se confirme el pago, te avisaremos inmediatamente por este medio.",
          enrollmentId,
          occurredAt
        )
      );
      
      case PAID -> new EmailContent(
        "Pago confirmado - " + course,
        buildHtmlTemplate(
          "✅ Pago Confirmado",
          "#10B981",
          name,
          "¡Excelentes noticias! Tu pago ha sido confirmado.",
          "Tu inscripción al curso <strong>\"" + course + "\"</strong> está activa.",
          "Ya puedes acceder a todo el contenido del curso. ¡Éxitos en tu aprendizaje!",
          enrollmentId,
          occurredAt
        )
      );
      
      case CANCELLED -> new EmailContent(
        "Inscripción cancelada - " + course,
        buildHtmlTemplate(
          "❌ Inscripción Cancelada",
          "#EF4444",
          name,
          "Te confirmamos que tu inscripción al curso <strong>\"" + course + "\"</strong> ha sido cancelada.",
          "",
          "Esperamos verte pronto en nuestros cursos.",
          enrollmentId,
          occurredAt
        )
      );
      
      case COMPLETED -> new EmailContent(
        "Curso completado - " + course,
        buildHtmlTemplate(
          "🎉 ¡Felicitaciones!",
          "#8B5CF6",
          name,
          "Has completado exitosamente el curso <strong>\"" + course + "\"</strong>.",
          "Tu dedicación y esfuerzo han dado sus frutos.",
          "Gracias por aprender con nosotros. ¡Sigue creciendo!",
          enrollmentId,
          occurredAt
        )
      );
    };
  }
  
  private String buildHtmlTemplate(
    String title,
    String accentColor,
    String studentName,
    String mainMessage,
    String statusMessage,
    String additionalInfo,
    String enrollmentId,
    String occurredAt
  ) {
    return """
    <!DOCTYPE html>
    <html lang="es">
    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>%s</title>
    </head>
    <body style="margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; background-color: #f3f4f6;">
      <table role="presentation" style="width: 100%%; border-collapse: collapse;">
        <tr>
          <td align="center" style="padding: 40px 20px;">
            
            <!-- Contenedor principal -->
            <table role="presentation" style="width: 100%%; max-width: 600px; border-collapse: collapse; background-color: #ffffff; border-radius: 12px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);">
              
              <!-- Header con color de acento -->
              <tr>
                <td style="padding: 0;">
                  <div style="background: linear-gradient(135deg, %s 0%%, %s 100%%); height: 8px; border-radius: 12px 12px 0 0;"></div>
                </td>
              </tr>
              
              <!-- Logo/Título -->
              <tr>
                <td style="padding: 40px 40px 20px 40px; text-align: center;">
                  <h1 style="margin: 0; font-size: 28px; font-weight: 700; color: #111827;">
                    %s
                  </h1>
                </td>
              </tr>
              
              <!-- Saludo -->
              <tr>
                <td style="padding: 0 40px 20px 40px;">
                  <p style="margin: 0; font-size: 16px; color: #374151; line-height: 1.6;">
                    Hola <strong>%s</strong>,
                  </p>
                </td>
              </tr>
              
              <!-- Mensaje principal -->
              <tr>
                <td style="padding: 0 40px 20px 40px;">
                  <p style="margin: 0; font-size: 16px; color: #374151; line-height: 1.6;">
                    %s
                  </p>
                </td>
              </tr>
              
              <!-- Status message -->
              <tr>
                <td style="padding: 0 40px 20px 40px;">
                  <p style="margin: 0; font-size: 16px; color: #374151; line-height: 1.6;">
                    %s
                  </p>
                </td>
              </tr>
              
              <!-- Additional info -->
              <tr>
                <td style="padding: 0 40px 30px 40px;">
                  <p style="margin: 0; font-size: 14px; color: #6B7280; line-height: 1.6;">
                    %s
                  </p>
                </td>
              </tr>
              
              <!-- Detalles en caja -->
              <tr>
                <td style="padding: 0 40px 40px 40px;">
                  <table role="presentation" style="width: 100%%; border-collapse: collapse; background-color: #F9FAFB; border-radius: 8px; border: 1px solid #E5E7EB;">
                    <tr>
                      <td style="padding: 20px;">
                        <table role="presentation" style="width: 100%%; border-collapse: collapse;">
                          <tr>
                            <td style="padding: 8px 0;">
                              <span style="font-size: 14px; color: #6B7280; font-weight: 600;">ID de inscripción:</span>
                            </td>
                            <td style="padding: 8px 0; text-align: right;">
                              <span style="font-size: 14px; color: #111827; font-family: 'Courier New', monospace;">%s</span>
                            </td>
                          </tr>
                          <tr>
                            <td style="padding: 8px 0; border-top: 1px solid #E5E7EB;">
                              <span style="font-size: 14px; color: #6B7280; font-weight: 600;">Fecha:</span>
                            </td>
                            <td style="padding: 8px 0; text-align: right; border-top: 1px solid #E5E7EB;">
                              <span style="font-size: 14px; color: #111827;">%s</span>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
              
              <!-- Footer -->
              <tr>
                <td style="padding: 30px 40px 40px 40px; border-top: 1px solid #E5E7EB;">
                  <p style="margin: 0 0 10px 0; font-size: 14px; color: #6B7280; line-height: 1.6;">
                    Saludos cordiales,<br>
                    <strong style="color: #111827;">Equipo de Enrollment</strong>
                  </p>
                  <p style="margin: 10px 0 0 0; font-size: 12px; color: #9CA3AF; line-height: 1.5;">
                    Este es un mensaje automático, por favor no respondas a este correo.
                  </p>
                </td>
              </tr>
              
            </table>
            
            <!-- Footer externo -->
            <table role="presentation" style="width: 100%%; max-width: 600px; margin-top: 20px;">
              <tr>
                <td style="text-align: center; padding: 0 20px;">
                  <p style="margin: 0; font-size: 12px; color: #9CA3AF; line-height: 1.5;">
                    © 2024 Sistema de Enrollment. Todos los derechos reservados.
                  </p>
                </td>
              </tr>
            </table>
            
          </td>
        </tr>
      </table>
    </body>
    </html>
    """.formatted(
      title,
      accentColor, adjustColorBrightness(accentColor, -20),
      title,
      studentName,
      mainMessage,
      statusMessage,
      additionalInfo,
      enrollmentId,
      occurredAt
    );
  }
  
  private String adjustColorBrightness(String hexColor, int percent) {
    // Simple darken/lighten - for gradient effect
    try {
      int r = Integer.parseInt(hexColor.substring(1, 3), 16);
      int g = Integer.parseInt(hexColor.substring(3, 5), 16);
      int b = Integer.parseInt(hexColor.substring(5, 7), 16);
      
      r = Math.max(0, Math.min(255, r + (r * percent / 100)));
      g = Math.max(0, Math.min(255, g + (g * percent / 100)));
      b = Math.max(0, Math.min(255, b + (b * percent / 100)));
      
      return String.format("#%02X%02X%02X", r, g, b);
    } catch (Exception e) {
      return hexColor;
    }
  }
  
  private String safe(String v) {
    return (v == null || v.isBlank()) ? "—" : v;
  }
  
  private record EmailContent(String subject, String body) {}
}
