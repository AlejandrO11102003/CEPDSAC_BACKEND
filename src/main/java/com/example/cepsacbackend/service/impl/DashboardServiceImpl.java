package com.example.cepsacbackend.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.cepsacbackend.dto.Dashboard.DashboardStatsDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaListResponseDTO;
import com.example.cepsacbackend.enums.EstadoMatricula;
import com.example.cepsacbackend.repository.CursoDiplomadoRepository;
import com.example.cepsacbackend.repository.MatriculaRepository;
import com.example.cepsacbackend.repository.PagoRepository;
import com.example.cepsacbackend.repository.UsuarioRepository;
import com.example.cepsacbackend.service.DashboardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UsuarioRepository usuarioRepository;
    private final CursoDiplomadoRepository cursoRepository;
    private final PagoRepository pagoRepository;
    private final MatriculaRepository matriculaRepository;

    @Override
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        stats.setTotalAlumnos(usuarioRepository.countByRol(com.example.cepsacbackend.enums.Rol.ALUMNO));
        stats.setTotalCursos(cursoRepository.count());
        Double ingresos = pagoRepository.sumTotalIngresos();
        stats.setIngresosTotales(ingresos != null ? ingresos : 0.0);
        long matriculasActivas = matriculaRepository.countByEstado(EstadoMatricula.PENDIENTE) +
                                 matriculaRepository.countByEstado(EstadoMatricula.EN_PROCESO) +
                                 matriculaRepository.countByEstado(EstadoMatricula.PAGADO);
        stats.setMatriculasActivas(matriculasActivas);
        List<Object[]> ingresosData = pagoRepository.findIngresosPorMes();
        List<Map<String, Object>> ingresosPorMes = new ArrayList<>();
        for (Object[] row : ingresosData) {
            Map<String, Object> mesData = new HashMap<>();
            mesData.put("mes", row[0]);
            mesData.put("total", row[1]);
            ingresosPorMes.add(mesData);
        }
        stats.setIngresosPorMes(ingresosPorMes);

        Map<String, Long> matriculasPorEstado = new HashMap<>();
        matriculasPorEstado.put("PENDIENTE", matriculaRepository.countByEstado(EstadoMatricula.PENDIENTE));
        matriculasPorEstado.put("EN_PROCESO", matriculaRepository.countByEstado(EstadoMatricula.EN_PROCESO));
        matriculasPorEstado.put("PAGADO", matriculaRepository.countByEstado(EstadoMatricula.PAGADO));
        matriculasPorEstado.put("CANCELADO", matriculaRepository.countByEstado(EstadoMatricula.CANCELADO));
        stats.setMatriculasPorEstado(matriculasPorEstado);

        List<MatriculaListResponseDTO> todasMatriculas = matriculaRepository.findAllAsListDTO();
        stats.setUltimasMatriculas(todasMatriculas.stream().limit(5).toList());

        return stats;
    }
}
