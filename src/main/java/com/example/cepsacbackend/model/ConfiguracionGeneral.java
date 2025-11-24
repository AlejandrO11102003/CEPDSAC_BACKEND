package com.example.cepsacbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "configuracion_general")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionGeneral {

    @Id
    @Column(name = "IdConfiguracion")
    private Integer idConfiguracion;

    @Column(name = "NumeroEstudiantes")
    private Integer numeroEstudiantes;

    @Column(name = "NumeroCertificaciones")
    private Integer numeroCertificaciones;

    @Column(name = "NumeroInstructores")
    private Integer numeroInstructores;

    @Column(name = "NumeroCursos")
    private Integer numeroCursos;
}
