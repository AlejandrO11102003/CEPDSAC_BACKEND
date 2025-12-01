package com.example.cepsacbackend.dto.CursoDiplomado;

import lombok.Data;
import lombok.NonNull;


@Data
@NonNull
public class CursoDiplomadoResponseDTO {
    private Short idCursoDiplomado;
    private Short idCategoria;
    private String nombreCategoria;
    private String tipo; // "CURSO" o "DIPLOMADO"
    private Boolean otorgaCertificado;
    private String titulo;
    private String urlCurso;
    private String objetivo;

    public CursoDiplomadoResponseDTO(Short idCursoDiplomado, Short idCategoria, String nombreCategoria, Boolean tipo, Boolean otorgaCertificado, String titulo, String urlCurso, String objetivo) {
        this.idCursoDiplomado = idCursoDiplomado;
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
        this.tipo = Boolean.TRUE.equals(tipo) ? "DIPLOMADO" : "CURSO";
        this.otorgaCertificado = otorgaCertificado;
        this.titulo = titulo;
        this.urlCurso = urlCurso;
        this.objetivo = objetivo;
    }
}
