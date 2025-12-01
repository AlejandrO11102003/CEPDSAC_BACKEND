package com.example.cepsacbackend.dto.Dashboard;

import java.util.List;
import java.util.Map;

import com.example.cepsacbackend.dto.Matricula.MatriculaListResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsDTO {
    private long totalAlumnos;
    private long totalCursos;
    private double ingresosTotales;
    private long matriculasActivas;
    private List<Map<String, Object>> ingresosPorMes;
    private Map<String, Long> matriculasPorEstado;
    private List<MatriculaListResponseDTO> ultimasMatriculas;
}
