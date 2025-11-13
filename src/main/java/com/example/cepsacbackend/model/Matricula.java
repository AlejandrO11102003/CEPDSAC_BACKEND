package com.example.cepsacbackend.model;

import com.example.cepsacbackend.auditory.AuditoriaListener;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.cepsacbackend.enums.EstadoMatricula;

import lombok.Getter;
import lombok.Setter;

// inscripcion de un alumno a una programacion especifica de un curso
// registra el proceso de matricula, aplicacion de descuentos y generacion de cuotas/pagos
// una matricula puede estar pendiente, aprobada, rechazada o cancelada
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditoriaListener.class)
@Table(name = "matricula", 
    indexes = {
        @Index(name = "idx_matricula_alumno", columnList = "IdAlumno"),
        @Index(name = "idx_matricula_programacion", columnList = "IdProgramacionCurso"),
        @Index(name = "idx_matricula_estado", columnList = "Estado")
    },
    uniqueConstraints = {
        // previene matriculas duplicadas: un alumno solo puede tener 1 matricula activa por programacion
        @UniqueConstraint(name = "uk_mat_alumno_prog_activa", 
                         columnNames = {"IdAlumno", "IdProgramacionCurso", "Estado"})
    }
)
public class Matricula extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdMatricula", columnDefinition = "SMALLINT UNSIGNED")
    private Integer idMatricula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdProgramacionCurso", nullable = false)
    private ProgramacionCurso programacionCurso; // fk a programacioncurso

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdAlumno", nullable = false)
    private Usuario alumno; // fk a usuario (alumno)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdAdministrador")
    private Usuario administradorAprobador;

    @Column(name = "FechaMatricula", updatable = false)
    private LocalDateTime fechaMatricula = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado", nullable = false)
    private EstadoMatricula estado = EstadoMatricula.PENDIENTE;

    @Column(name = "MontoBase", precision = 10, scale = 2)
    private BigDecimal montoBase;

    @Column(name = "MontoDescontado", precision = 10, scale = 2)
    private BigDecimal montoDescontado;

    @Column(name = "Monto", precision = 10, scale = 2)
    private BigDecimal monto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdDescuento")
    private Descuento descuento;

    @Column(name = "PagoPersonalizado")
    private Boolean pagoPersonalizado = false; //true = pagos manuales, false = cuotas automáticas
}
