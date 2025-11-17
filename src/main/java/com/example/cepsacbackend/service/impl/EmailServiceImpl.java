package com.example.cepsacbackend.service.impl;

import com.example.cepsacbackend.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    private String cargarTemplate(String nombreArchivo) {
        try {
            ClassPathResource resource = new ClassPathResource("static/email-templates/" + nombreArchivo);
            return Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Error al cargar template: {}", nombreArchivo, e);
            throw new RuntimeException("Error al cargar template de email", e);
        }
    }

    @Async
    @Override
    public void enviarEmailRestablecerPassword(String destinatario, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(destinatario);
            helper.setSubject("Restablecer Contraseña - CEPSAC");
            String resetUrl = frontendUrl + "/reset-password?token=" + token;
            String htmlContent = cargarTemplate("RecuperarPass.html")
                    .replace("{{RESET_URL}}", resetUrl);

            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info("Email de restablecimiento de contraseña enviado a: {}", destinatario);
        } catch (MessagingException e) {
            log.error("Error al enviar email de restablecimiento a {}: {}", destinatario, e.getMessage());
            throw new RuntimeException("Error al enviar email de restablecimiento", e);
        }
    }

    @Async
    @Override
    public void enviarEmailNotificacionPago(String destinatario, Integer idMatricula) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            // si no se especifica destinatario, enviamos al correo configurado como remitente (administrador)
            String to = destinatario;
            if (to == null || to.isBlank()) {
                to = fromEmail;
            }
            helper.setTo(to);
            helper.setSubject("Notificación de pago - Nueva matrícula");

            String adminUrl = frontendUrl + "/admin/matriculas/" + idMatricula;
            String htmlContent = cargarTemplate("NotificacionPago.html")
                    .replace("{{MATRICULA_ID}}", idMatricula == null ? "N/A" : idMatricula.toString())
                    .replace("{{ADMIN_URL}}", adminUrl);

            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info("Email de notificación de pago enviado a: {} para matricula {}", to, idMatricula);
        } catch (MessagingException e) {
            log.error("Error al enviar email de notificación de pago a {}: {}", destinatario, e.getMessage());
            throw new RuntimeException("Error al enviar email de notificación de pago", e);
        }
    }

}