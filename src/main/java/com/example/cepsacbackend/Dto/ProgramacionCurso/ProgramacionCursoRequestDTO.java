package com.example.cepsacbackend.Dto.ProgramacionCurso;

import com.example.cepsacbackend.Enums.ModalidadCurso;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Integer idUsuario;

    @NotNull(message = "El ID del curso diplomado no puede ser nulo")
    private Integer idCursoDiplomado;
}