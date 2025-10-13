package com.example.cepsacbackend.Dto.Categoria;

import lombok.Data;

@Data
public class CategoriaResponseDTO {
    private Short idCategoria;
    private String nombre;
    private String descripcion;
    private Integer idUsuario;
}
