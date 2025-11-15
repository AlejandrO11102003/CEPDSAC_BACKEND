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
}
