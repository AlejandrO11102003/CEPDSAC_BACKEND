package com.example.cepsacbackend.dto.CursoDiplomado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoSimpleDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoDetalleResponseDTO {
    private Short idCursoDiplomado;
    private Short idCategoria;
    private String nombreCategoria;
    private Boolean tipo; // false: CURSO, true: DIPLOMADO
    private Boolean otorgaCertificado;
    private String titulo;
    private String urlCurso;
    private String objetivo;
    private String materialesIncluidos;
    private String requisitos;
    
    // programaciones disponibles
    private List<ProgramacionCursoSimpleDTO> programaciones;
}
