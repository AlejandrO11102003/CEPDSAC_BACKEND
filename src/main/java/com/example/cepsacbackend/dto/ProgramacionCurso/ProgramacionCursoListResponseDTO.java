package com.example.cepsacbackend.dto.ProgramacionCurso;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramacionCursoListResponseDTO {
    private Integer idProgramacionCurso;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal monto;
    private String nombreCursoDiplomado;
    private Short duracionMeses;
    private String nombreDocente;
    private Long cantidadInscritos;

    public ProgramacionCursoListResponseDTO(Integer idProgramacionCurso, LocalDate fechaInicio, LocalDate fechaFin, BigDecimal monto, String nombreCursoDiplomado, Short duracionMeses, String nombreDocente) {
        this.idProgramacionCurso = idProgramacionCurso;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.monto = monto;
        this.nombreCursoDiplomado = nombreCursoDiplomado;
        this.duracionMeses = duracionMeses;
        this.nombreDocente = nombreDocente;
        this.cantidadInscritos = 0L;
    }
}
