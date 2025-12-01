package com.example.cepsacbackend.dto.ProgramacionCurso;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.cepsacbackend.enums.ModalidadCurso;

@Data
public class ProgramacionCursoRequestDTO {
    @NotNull(message = "La modalidad no puede ser nula")
    private ModalidadCurso modalidad;

    @NotNull(message = "La duración del curso no puede ser nula")
    private BigDecimal duracionCurso;

    @NotNull(message = "La duración en meses no puede ser nula")
    private Short duracionMeses;

    @NotNull(message = "La fecha de inicio no puede ser nula")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin no puede ser nula")
    private LocalDate fechaFin;

    @NotNull(message = "El monto no puede ser nulo")
    private BigDecimal monto;

    @NotNull(message = "El ID del curso diplomado no puede ser nulo")
    private Short idCursoDiplomado;

    //campo opcional para generar cuotas automaticas
    //si tiene valor se generan cuotas al matricular, si es null el pago es completo o manual
    private Short numeroCuotas;

    // horario de la programación (ej: "Lunes 8:00 - 11:30 am")
    private String horario;

    @NotNull(message = "El Docente es Obligatorio en una programcion")
    // docente asignado a esta progrmacion
    private Integer idDocente;
}
