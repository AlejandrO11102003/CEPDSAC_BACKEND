package com.example.cepsacbackend.dto.Matricula;

import com.example.cepsacbackend.enums.EstadoMatricula;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AlumnoMatriculadoDTO {
    private Integer idMatricula;
    private Integer idAlumno;
    private String nombre;
    private String apellido;
    private String correo;
    private String numeroIdentificacion;
    private LocalDate fechaMatricula;
    private String estadoMatricula;

    public AlumnoMatriculadoDTO(Integer idMatricula, Integer idAlumno, String nombre, 
                                String apellido, String correo, String numeroIdentificacion, 
                                LocalDateTime fechaMatricula, EstadoMatricula estado) {
        this.idMatricula = idMatricula;
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.numeroIdentificacion = numeroIdentificacion;
        this.fechaMatricula = fechaMatricula != null ? fechaMatricula.toLocalDate() : null;
        this.estadoMatricula = estado != null ? estado.name() : null;
    }

    public AlumnoMatriculadoDTO(Integer idMatricula, Integer idAlumno, String nombre, 
                                String apellido, String correo, String numeroIdentificacion, 
                                LocalDateTime fechaMatricula, String estadoMatricula) {
        this.idMatricula = idMatricula;
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.numeroIdentificacion = numeroIdentificacion;
        this.fechaMatricula = fechaMatricula != null ? fechaMatricula.toLocalDate() : null;
        this.estadoMatricula = estadoMatricula;
    }
}
