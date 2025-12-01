package com.example.cepsacbackend.dto.Matricula;

import com.example.cepsacbackend.enums.EstadoMatricula;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MatriculaAdminListDTO {
    private Integer idMatricula;
    private LocalDateTime fechaMatricula;
    private EstadoMatricula estado;
    private BigDecimal monto;
    
    private String nombreCompletoAlumno;
    private String correoAlumno;
    private String dniAlumno;

    private String tituloCurso;
    private LocalDate fechaInicioCurso;
    
    private Long totalCuotas;
    private Long cuotasPagadas;
    private BigDecimal montoPagado;
    private BigDecimal saldoPendiente;
    private LocalDate proximoVencimiento;
    
    private Boolean tieneVencidas;
}