package com.example.cepsacbackend.dto.Metrics;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetricsResponseDTO {
    private long estudiantes;
    private long certificaciones;
    private long instructores;
    private long cursos;

}
