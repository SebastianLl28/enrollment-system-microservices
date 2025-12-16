package com.app.enrollment.system.notification.server.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * @author Alonso
 */
@Component
public class EmailSender {
  
  private final JavaMailSender mailSender;
  private final String from;
  
  public EmailSender(JavaMailSender mailSender,
    @Value("${notification.mail.from}") String from) {
    this.mailSender = mailSender;
    this.from = from;
  }
  
  public void sendText(String to, String subject, String body) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(from);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(body);
    mailSender.send(message);
  }
  
  public void sendHtml(String to, String subject, String htmlBody) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper =
        new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
      
      helper.setFrom(from);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(htmlBody, true);
      
      mailSender.send(message);
    } catch (MessagingException e) {
      throw new IllegalStateException("Error sending HTML email", e);
    }
  }
}
