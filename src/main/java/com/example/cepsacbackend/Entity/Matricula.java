package com.example.cepsacbackend.Entity;

import com.example.cepsacbackend.Enums.EstadoMatricula;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Matricula {

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
}
