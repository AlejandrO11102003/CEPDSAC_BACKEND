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
    private Short numeroCuotas;
    private String nombreDocente;
    private Long cantidadInscritos;

}
