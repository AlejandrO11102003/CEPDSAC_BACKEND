package com.example.cepsacbackend.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.cepsacbackend.dto.Metrics.MetricsResponseDTO;
import com.example.cepsacbackend.enums.Rol;
import com.example.cepsacbackend.repository.CursoDiplomadoRepository;
import com.example.cepsacbackend.repository.ProgramacionCursoRepository;
import com.example.cepsacbackend.repository.MatriculaRepository;
import com.example.cepsacbackend.repository.UsuarioRepository;
import com.example.cepsacbackend.service.MetricsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MetricsServiceImpl implements MetricsService {

    private final UsuarioRepository usuarioRepository;
    private final CursoDiplomadoRepository cursoRepository;
    private final MatriculaRepository matriculaRepository;
    private final ProgramacionCursoRepository programacionCursoRepository;
    @Value("${metrics.base.unregisteredStudents:200}")
    private int baseUnregisteredStudents;
    @Value("${metrics.base.certifications:500}")
    private int baseCertifications;

    @Override
    public MetricsResponseDTO getMetrics() {
        // estudiantes: ahora representa estudiantes que tienen al menos una matrícula (sin pedestal)
        long estudiantesConMatricula = matriculaRepository.countDistinctAlumno();

        // instructores: sigue siendo el conteo de docentes registrados
        long instructores = usuarioRepository.findByRolActivoAsDTO(Rol.DOCENTE).size();

        // certificaciones: ahora reutilizado para representar programaciones de cursos activas
        long programacionesActivas = programacionCursoRepository.findAllAvailable(java.time.LocalDate.now()).size();

        long cursos = cursoRepository.count();

        return new MetricsResponseDTO(estudiantesConMatricula, programacionesActivas, instructores, cursos);
    }
}
