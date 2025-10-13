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
public class Pais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdPais" , columnDefinition = "TINYINT UNSIGNED")
    private Short idPais;

    @Column(name = "Nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "Codigo", length = 2)
    private String codigo;

    @Column(name = "CodigoTelefono", length = 5)
    private String codigotelefono;

}