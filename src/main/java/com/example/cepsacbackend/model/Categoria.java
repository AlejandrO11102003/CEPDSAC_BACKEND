package com.example.cepsacbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// categorias para organizar los cursos/diplomados en la landing page
// ej: tecnologia, administracion, marketing, diseño, idiomas
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCategoria")
    private Short idCategoria;

    @Column(name = "Nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "Descripcion", length = 100)
    private String descripcion;

    @Column(name = "Estado")
    private Boolean estado;  //false: inactivo, true: activo

}