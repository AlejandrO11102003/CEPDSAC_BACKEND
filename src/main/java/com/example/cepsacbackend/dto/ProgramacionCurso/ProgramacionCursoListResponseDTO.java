package com.example.cepsacbackend.dto.ProgramacionCurso;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ProgramacionCursoListResponseDTO {
    Integer getIdProgramacionCurso();
    LocalDate getFechaInicio();
    LocalDate getFechaFin();
    BigDecimal getMonto();
    String getNombreCursoDiplomado();
    Short getNumeroCuotas();
    String getNombreDocente();
    Long getCantidadInscritos();
}
