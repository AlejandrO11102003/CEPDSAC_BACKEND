package com.example.cepsacbackend.model;

import com.example.cepsacbackend.auditory.AuditoriaListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

// entidad principal para cursos y diplomados que se ofrecen en la plataforma
// contiene toda la info descriptiva y de marketing del curso/diplomado
// se relaciona con programacioncurso para las fechas y horarios específicos
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

    // categoria del curso para organizarlo en el frontend (ej: tecnologia, administracion, marketing)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCategoria")
    private Categoria categoria;

    // false = curso (duracion corta), true = diplomado (duracion larga y mas completo)
    @Column(name = "Tipo")
    private Boolean tipo;

    // indica si al finalizar se entrega certificado oficial
    @Column(name = "OtorgaCertificado")
    private Boolean otorgaCertificado;

    // nombre del curso/diplomado que se muestra en el landing y detalle
    @Column(name = "Titulo", nullable = false, length = 100)
    private String titulo;

    // url de la imagen de portada del curso para mostrarlo en las cards
    @Column(name = "UrlCurso", length = 255)
    private String urlCurso;

    // descripcion de lo que el alumno aprendera en el curso
    @Column(name = "Objetivo", length = 500)
    private String objetivo;

    // lista de materiales separados por | (ej: "videos|pdfs|certificado|acceso de por vida")
    @Column(name = "MaterialesIncluidos", columnDefinition = "TEXT")
    private String materialesIncluidos;

    // lista de requisitos previos separados por | (ej: "conocimientos de java|pc con 8gb ram")
    @Column(name = "Requisitos", columnDefinition = "TEXT")
    private String requisitos;

}