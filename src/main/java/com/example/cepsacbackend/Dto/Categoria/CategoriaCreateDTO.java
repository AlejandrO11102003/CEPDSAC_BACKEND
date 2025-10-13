package com.example.cepsacbackend.Dto.Categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoriaCreateDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String nombre;

    @Size(max = 100, message = "La descripción no puede exceder los 100 caracteres")
    private String descripcion;

    private Integer idUsuario;
}
