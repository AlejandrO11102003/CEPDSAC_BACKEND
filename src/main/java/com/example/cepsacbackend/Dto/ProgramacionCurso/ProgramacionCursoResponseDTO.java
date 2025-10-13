package com.example.cepsacbackend.Dto.ProgramacionCurso;

import com.example.cepsacbackend.Enums.ModalidadCurso;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProgramacionCursoResponseDTO {
    private Integer idProgramacionCurso;
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