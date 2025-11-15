package com.example.cepsacbackend.dto.CursoDiplomado;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import jakarta.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@NonNull
public class CursoDiplomadoCreateDTO {
    @NotNull
    private Short idCategoria;

    private Boolean tipo = false; // 0: CURSO

    private Boolean otorgaCertificado = false;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100)
    private String titulo;
    private String urlCurso;
    private String objetivo;
}
