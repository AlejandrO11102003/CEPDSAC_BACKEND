package com.example.cepsacbackend.Dto.Matricula;

import com.example.cepsacbackend.Enums.EstadoMatricula;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MatriculaResponseDTO(
    Integer idMatricula,
    Integer idProgramacionCurso,
    Integer idAlumno,
    Integer idAdministradorAprobador,
    LocalDateTime fechaMatricula,
    EstadoMatricula estado,
    BigDecimal montoBase,
    BigDecimal montoDescontado,
    BigDecimal monto
) {}
