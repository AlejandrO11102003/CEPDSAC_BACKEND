package com.example.cepsacbackend.service;

public interface EmailService {
    
    void enviarEmailRestablecerPassword(String destinatario, String token);
    void enviarEmailNotificacionPago(String destinatario, Integer idMatricula);
    void enviarEmailCancelacionMasiva(java.util.List<String> correosAlumnos, String tituloCurso, String motivoCancelacion);
}
