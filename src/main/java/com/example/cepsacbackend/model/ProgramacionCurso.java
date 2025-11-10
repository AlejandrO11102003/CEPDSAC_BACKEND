package com.example.cepsacbackend.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.cepsacbackend.auditory.AuditoriaListener;
import com.example.cepsacbackend.enums.ModalidadCurso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditoriaListener.class)
public class ProgramacionCurso extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdProgramacionCurso")
    private Integer idProgramacionCurso;

    @Enumerated(EnumType.STRING)
    @Column(name = "Modalidad")
    private ModalidadCurso modalidad;

    @Column(name = "DuracionCurso", precision = 6, scale = 2)
    private BigDecimal duracionCurso;

    @Column(name = "HorasSemanales", precision = 6, scale = 2)
    private BigDecimal horasSemanales;

    @Column(name = "FechaInicio")
    private LocalDate fechaInicio;

    @Column(name = "FechaFin")
    private LocalDate fechaFin;

    // precio base del curso programado
    @Column(name = "Monto", precision = 10, scale = 2)
    private BigDecimal monto;

    //campo opcional para fraccionar el pago en cuotas mensuales (3 4 6 meses etc)
    //si es null o 0 no se generan cuotas automaticas y funciona como antes
    @Column(name = "DuracionMeses", columnDefinition = "TINYINT UNSIGNED")
    private Short duracionMeses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCursoDiplomado", nullable = false)
    private CursoDiplomado cursoDiplomado;
}