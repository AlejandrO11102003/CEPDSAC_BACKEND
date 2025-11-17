package com.example.cepsacbackend.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.cepsacbackend.dto.Metrics.MetricsResponseDTO;
import com.example.cepsacbackend.enums.Rol;
import com.example.cepsacbackend.enums.EstadoMatricula;
import com.example.cepsacbackend.repository.CursoDiplomadoRepository;
import com.example.cepsacbackend.repository.MatriculaRepository;
import com.example.cepsacbackend.repository.PagoRepository;
import com.example.cepsacbackend.repository.UsuarioRepository;
import com.example.cepsacbackend.service.MetricsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MetricsServiceImpl implements MetricsService {

    private final UsuarioRepository usuarioRepository;
    private final PagoRepository pagoRepository;
    private final CursoDiplomadoRepository cursoRepository;
    private final MatriculaRepository matriculaRepository;
    @Value("${metrics.base.unregisteredStudents:200}")
    private int baseUnregisteredStudents;
    @Value("${metrics.base.certifications:500}")
    private int baseCertifications;

    @Override
    public MetricsResponseDTO getMetrics() {
        long estudiantesRegistrados = usuarioRepository.findByRolActivoAsDTO(Rol.ALUMNO).size();
        long estudiantes = estudiantesRegistrados + (long) baseUnregisteredStudents;
        long instructores = usuarioRepository.findByRolActivoAsDTO(Rol.DOCENTE).size();
        long certificacionesRegistradas = matriculaRepository.countDistinctAlumnoByEstado(EstadoMatricula.PAGADO);
        long certificaciones = certificacionesRegistradas + (long) baseCertifications;
        long cursos = cursoRepository.count();

        return new MetricsResponseDTO(estudiantes, certificaciones, instructores, cursos);
    }
}
