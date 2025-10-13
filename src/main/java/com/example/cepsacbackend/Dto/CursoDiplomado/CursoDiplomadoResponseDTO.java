package com.example.cepsacbackend.Dto.CursoDiplomado;

import lombok.Data;

@Data
public class CursoDiplomadoResponseDTO {
    private Short idCursoDiplomado;
    private Short idCategoria;
    private String nombreCategoria;
    private String tipo; // "CURSO" o "DIPLOMADO"
    private Boolean otorgaCertificado;
    private String titulo;
    private String urlCurso;
    private String objetivo;
    private Integer idUsuario;
    private String nombreUsuario;
}
