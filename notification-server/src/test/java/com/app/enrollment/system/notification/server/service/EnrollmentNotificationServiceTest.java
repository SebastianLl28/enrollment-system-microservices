package com.app.enrollment.system.notification.server.service;

import com.app.common.enums.EnrollmentStatus;
import com.app.common.events.EnrollmentAssignedEvent;
import com.app.enrollment.system.notification.server.email.EmailSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EnrollmentNotificationServiceTest {

    @Mock
    private EmailSender emailSender;

    private EnrollmentNotificationService service;

    @BeforeEach
    void setUp() {
        service = new EnrollmentNotificationService(emailSender);
    }

    @Test
    void pending_email_contains_career_and_term() {
        EnrollmentAssignedEvent event = event(EnrollmentStatus.PENDING, "http://pay.me");

        service.sendEnrollmentEmail(event);

        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailSender).sendHtml(eq("student@test.com"), anyString(), bodyCaptor.capture());
        String body = bodyCaptor.getValue();
        assertThat(body).contains("Ingeniería de Sistemas").contains("2026-I");
    }

    @Test
    void pending_email_with_payment_url_includes_button() {
        EnrollmentAssignedEvent event = event(EnrollmentStatus.PENDING, "http://pay.me");

        service.sendEnrollmentEmail(event);

        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailSender).sendHtml(anyString(), anyString(), bodyCaptor.capture());
        assertThat(bodyCaptor.getValue()).contains("Pagar matrícula").contains("http://pay.me");
    }

    @Test
    void pending_email_without_payment_url_omits_button() {
        EnrollmentAssignedEvent event = event(EnrollmentStatus.PENDING, null);

        service.sendEnrollmentEmail(event);

        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailSender).sendHtml(anyString(), anyString(), bodyCaptor.capture());
        assertThat(bodyCaptor.getValue()).doesNotContain("Pagar matrícula");
    }

    @Test
    void paid_email_subject_confirms_payment() {
        EnrollmentAssignedEvent event = event(EnrollmentStatus.PAID, null);

        service.sendEnrollmentEmail(event);

        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailSender).sendHtml(anyString(), subjectCaptor.capture(), anyString());
        assertThat(subjectCaptor.getValue()).contains("Pago confirmado");
    }

    @Test
    void cancelled_email_subject_signals_cancellation() {
        EnrollmentAssignedEvent event = event(EnrollmentStatus.CANCELLED, null);

        service.sendEnrollmentEmail(event);

        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailSender).sendHtml(anyString(), subjectCaptor.capture(), anyString());
        assertThat(subjectCaptor.getValue()).contains("cancelada");
    }

    @Test
    void completed_email_subject_signals_completion() {
        EnrollmentAssignedEvent event = event(EnrollmentStatus.COMPLETED, null);

        service.sendEnrollmentEmail(event);

        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailSender).sendHtml(anyString(), subjectCaptor.capture(), anyString());
        assertThat(subjectCaptor.getValue()).contains("completado");
    }

    @Test
    void null_email_throws_and_does_not_send() {
        EnrollmentAssignedEvent event = event(EnrollmentStatus.PENDING, null);
        event.setStudentEmail(null);

        assertThatThrownBy(() -> service.sendEnrollmentEmail(event))
                .isInstanceOf(IllegalArgumentException.class);
        verify(emailSender, never()).sendHtml(anyString(), anyString(), anyString());
    }

    @Test
    void blank_email_throws_and_does_not_send() {
        EnrollmentAssignedEvent event = event(EnrollmentStatus.PENDING, null);
        event.setStudentEmail("   ");

        assertThatThrownBy(() -> service.sendEnrollmentEmail(event))
                .isInstanceOf(IllegalArgumentException.class);
        verify(emailSender, never()).sendHtml(anyString(), anyString(), anyString());
    }

    private EnrollmentAssignedEvent event(EnrollmentStatus status, String paymentUrl) {
        EnrollmentAssignedEvent e = new EnrollmentAssignedEvent(
                "enroll-001", Instant.now(), "Juan Pérez", "student@test.com",
                status.name(), 1, "Ingeniería de Sistemas", "2026-I");
        e.setPaymentUrl(paymentUrl);
        return e;
    }
}
