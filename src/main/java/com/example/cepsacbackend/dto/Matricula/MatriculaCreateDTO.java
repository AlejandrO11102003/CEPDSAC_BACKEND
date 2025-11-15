package com.example.cepsacbackend.dto.Matricula;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NonNull
public class MatriculaCreateDTO{

    @NotNull 
    private Integer idProgramacionCurso;
    private Integer idAlumno;
    
    private Boolean pagoPersonalizado; //opcional: true = sin cuotas automáticas, pagos manuales
}