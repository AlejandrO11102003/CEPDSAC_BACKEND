package com.example.cepsacbackend.dto.CursoDiplomado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoIndexResponseDTO {
    private Short idCursoDiplomado;
    private String urlCurso;
    private String titulo;
    private CategoriaSimpleDTO categoria;

    public CursoIndexResponseDTO(Short idCursoDiplomado, String urlCurso, String titulo, 
                                  Short idCategoria, String nombreCategoria, Boolean estadoCategoria) {
        this.idCursoDiplomado = idCursoDiplomado;
        this.urlCurso = urlCurso;
        this.titulo = titulo;
        this.categoria = new CategoriaSimpleDTO(idCategoria, nombreCategoria, estadoCategoria);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoriaSimpleDTO {
        private Short idCategoria;
        private String nombre;
        private Boolean estado;
    }
}
