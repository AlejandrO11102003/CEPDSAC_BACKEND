package com.example.cepsacbackend.dto.Matricula;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class CancelacionProgramacionResponseDTO {
    
    private Integer cantidadCanceladas;
    private String tituloCurso;
    private List<Integer> idsMatriculasCanceladas;
}
