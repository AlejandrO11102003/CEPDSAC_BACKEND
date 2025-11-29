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
public class ProgramacionCursoSimpleDTO {
    private Integer idProgramacionCurso;
    private ModalidadCurso modalidad;
    private BigDecimal duracionCurso;
    private Short duracionMeses;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal monto;
    private Short numeroCuotas;
    private String horario;
    
    // Información del docente
    private Integer idDocente;
    private String nombreDocente;
    private String apellidoDocente;
}
