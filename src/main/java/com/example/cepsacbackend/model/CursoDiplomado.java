package com.example.cepsacbackend.model;

import com.example.cepsacbackend.auditory.AuditoriaListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditoriaListener.class)
@Table(name = "CursoDiplomado")
public class CursoDiplomado extends AuditableEntity {
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

}