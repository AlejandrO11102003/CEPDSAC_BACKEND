package com.example.cepsacbackend.Dto.CursoDiplomado;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CursoDiplomadoCreateDTO {
    private Short idCategoria;

    private Boolean tipo = false; // 0: CURSO

    private Boolean otorgaCertificado = false;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100)
    private String titulo;

    private String urlCurso;
    private String objetivo;
    private Integer idUsuario;
}
