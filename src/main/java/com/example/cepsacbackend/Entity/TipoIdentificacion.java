package com.example.cepsacbackend.Entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoIdentificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTipoIdentificacion", columnDefinition = "TINYINT UNSIGNED")
    private Short idTipoIdentificacion;

    @Column(name = "Nombre", nullable = false, length = 50)
    private String nombre;

}