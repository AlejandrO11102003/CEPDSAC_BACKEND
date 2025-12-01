package com.example.cepsacbackend.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.cepsacbackend.auditory.AuditoriaListener;
import com.example.cepsacbackend.enums.ModalidadCurso;
import com.example.cepsacbackend.enums.EstadoProgramacion;

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

// programacion concreta de un curso/diplomado con fechas, horarios y docente asignado
// un mismo curso puede tener multiples programaciones en diferentes fechas/horarios
// aqui se define cuando se dicta, quien enseña, el precio y las modalidades de pago
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

    // como se dicta: presencial, virtual o virtual 24/7 (asincrono)
    @Enumerated(EnumType.STRING)
    @Column(name = "Modalidad")
    private ModalidadCurso modalidad;

    // total de horas del curso completo (ej: 40 horas)
    @Column(name = "DuracionCurso", precision = 6, scale = 2)
    private BigDecimal duracionCurso;

    // duracion academica del curso en meses (ej: 4 meses)
    @Column(name = "DuracionMeses", columnDefinition = "TINYINT UNSIGNED")
    private Short duracionMeses;

    // fecha en que empieza esta programacion del curso
    @Column(name = "FechaInicio")
    private LocalDate fechaInicio;

    // fecha en que termina, se usa para filtrar programaciones disponibles (fechafin > hoy)
    @Column(name = "FechaFin")
    private LocalDate fechaFin;

    // precio total de esta programacion del curso
    @Column(name = "Monto", precision = 10, scale = 2)
    private BigDecimal monto;

    // numero de cuotas de pago (ej: 3 = 3 cuotas)
    // si tiene valor genera cuotas automaticas al matricular
    // si es null el alumno paga el monto completo o el admin crea cuotas manuales
    @Column(name = "NumeroCuotas", columnDefinition = "TINYINT UNSIGNED")
    private Short numeroCuotas;

    // dias y horas de clase (ej: "lunes y miercoles 8:00 - 11:30 am")
    @Column(name = "Horario", length = 100)
    private String horario;

    // curso o diplomado al que pertenece esta programacion
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCursoDiplomado", nullable = false)
    private CursoDiplomado cursoDiplomado;

    // docente/profesor asignado para dictar esta programacion especifica
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdDocente")
    private Usuario docente;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado", nullable = false)
    private EstadoProgramacion estado = EstadoProgramacion.ACTIVO;
}

