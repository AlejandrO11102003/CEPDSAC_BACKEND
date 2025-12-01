package com.example.cepsacbackend.dto.CursoDiplomado;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para actualizar un Curso o Diplomado existente.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CursoDiplomadoUpdateDTO {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100, message = "El título no puede tener más de 100 caracteres")
    private String titulo;

    @NotNull(message = "El ID de la categoría es requerido")
    private Short idCategoria;

    @NotNull(message = "El tipo es requerido (true para Diplomado, false para Curso)")
    private Boolean tipo;

    private Boolean otorgaCertificado;

    @Size(max = 255, message = "La URL de la imagen no puede tener más de 255 caracteres")
    private String urlCurso;

    @Size(max = 500, message = "El objetivo no puede tener más de 500 caracteres")
    private String objetivo;
    
    private String materialesIncluidos;
    
    private String requisitos;
}
