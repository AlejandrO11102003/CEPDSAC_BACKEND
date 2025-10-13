package com.example.cepsacbackend.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.cepsacbackend.Enums.ModalidadCurso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class ProgramacionCurso {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdUsuario")
    private Usuario usuario;

    // precio base del curso programado
    @Column(name = "Monto", precision = 10, scale = 2)
    private BigDecimal monto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCursoDiplomado", nullable = false)
    private CursoDiplomado cursoDiplomado;
}