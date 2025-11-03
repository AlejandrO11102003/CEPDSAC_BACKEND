package com.example.cepsacbackend.dto.ProgramacionCurso;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.cepsacbackend.enums.ModalidadCurso;

@Data
public class ProgramacionCursoResponseDTO {
    private Short idProgramacionCurso;
    private ModalidadCurso modalidad;
    private BigDecimal duracionCurso;
    private BigDecimal horasSemanales;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal monto;
    private Integer idUsuario;
    private String nombreUsuario;
    private Integer idCursoDiplomado;
    private String nombreCursoDiplomado;
}