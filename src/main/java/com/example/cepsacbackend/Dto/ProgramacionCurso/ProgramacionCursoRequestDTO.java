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

    @NotNull(message = "Las horas semanales no pueden ser nulas")
    private BigDecimal horasSemanales;

    @NotNull(message = "La fecha de inicio no puede ser nula")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin no puede ser nula")
    private LocalDate fechaFin;

    @NotNull(message = "El monto no puede ser nulo")
    private BigDecimal monto;

    @NotNull(message = "El ID del curso diplomado no puede ser nulo")
    private Short idCursoDiplomado;

    //campo opcional para generar cuotas automaticas segun los meses del curso
    //si tiene valor se generan cuotas al matricular si es null funciona como antes
    private Short duracionMeses;
}