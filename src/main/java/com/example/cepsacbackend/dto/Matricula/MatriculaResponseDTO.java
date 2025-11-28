package com.example.cepsacbackend.dto.Matricula;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.cepsacbackend.enums.EstadoMatricula;

public record MatriculaResponseDTO(
    Integer idMatricula,
    Integer idProgramacionCurso,
    Integer idAlumno,
    LocalDateTime fechaMatricula,
    EstadoMatricula estado,
    BigDecimal montoBase,
    BigDecimal montoDescontado,
    BigDecimal monto,
    Boolean pagoPersonalizado,
    String alumnoNombre
) {}
