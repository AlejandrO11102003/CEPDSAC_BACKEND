package com.example.cepsacbackend.dto.Matricula;

import com.example.cepsacbackend.enums.EstadoMatricula;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO optimizado para listado de matrículas.
 * Solo incluye los campos esenciales para reducir transferencia de datos.
 * Usado con Constructor Expression en JPQL para evitar carga de entidades completas.
 */
@Data
@AllArgsConstructor
public class MatriculaListResponseDTO {
    private Integer idMatricula;
    private LocalDateTime fechaMatricula;
    private EstadoMatricula estado;
    private BigDecimal monto;
    
    // Datos del alumno (solo lo necesario)
    private String nombreAlumno;
    private String apellidoAlumno;
    private String correoAlumno;
    
    // Datos del curso
    private String tituloCurso;
    private String nombreCategoria;
    
    // Datos del descuento aplicado
    private BigDecimal valorDescuento;
    private BigDecimal montoDescontado;
    
    // Información adicional
    private Boolean pagoPersonalizado;

    public String getNombreCompletoAlumno() {
        return this.nombreAlumno + " " + this.apellidoAlumno;
    }
}
