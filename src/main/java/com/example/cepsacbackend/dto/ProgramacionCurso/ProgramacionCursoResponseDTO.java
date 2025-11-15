package com.example.cepsacbackend.dto.ProgramacionCurso;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.cepsacbackend.enums.ModalidadCurso;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgramacionCursoResponseDTO {
    private Integer idProgramacionCurso;
    private ModalidadCurso modalidad;
    private BigDecimal duracionCurso;
    private BigDecimal horasSemanales;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal monto;
    private Short idCursoDiplomado;
    private String nombreCursoDiplomado;
    
    // Campo opcional para sistema de cuotas automáticas
    private Short duracionMeses;
    private Integer idDocente;
    private String nombreDocente;
}
