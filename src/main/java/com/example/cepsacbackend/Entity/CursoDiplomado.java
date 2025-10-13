package com.example.cepsacbackend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CursoDiplomado")
public class CursoDiplomado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCursoDiplomado")
    private Short idCursoDiplomado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCategoria")
    private Categoria categoria;

    @Column(name = "Tipo")
    private Boolean tipo; // 0: CURSO, 1: DIPLOMADO

    @Column(name = "OtorgaCertificado")
    private Boolean otorgaCertificado;

    @Column(name = "Titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "UrlCurso", length = 255)
    private String urlCurso;

    @Column(name = "Objetivo", length = 255)
    private String objetivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdUsuario")
    private Usuario usuario;
}