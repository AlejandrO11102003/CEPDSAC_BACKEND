package com.example.cepsacbackend.Entity;

import com.example.cepsacbackend.Enums.TipoMetodo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdMetodoPago", columnDefinition = "TINYINT UNSIGNED")
    private Short idMetodoPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "TipoMetodo", nullable = false)
    private TipoMetodo tipoMetodo;

    @Column(name = "Descripcion", length = 100)
    private String descripcion;

    @Column(name = "Requisitos", length = 500)
    private String requisitos;

    @Column(name = "Activo", nullable = false)
    private Boolean activo = true; // deff true

}