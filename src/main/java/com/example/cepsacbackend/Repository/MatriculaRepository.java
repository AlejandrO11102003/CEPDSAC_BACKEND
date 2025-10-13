package com.example.cepsacbackend.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.cepsacbackend.Entity.Matricula;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {

    // verificar si ya existe una matrícula para el mismo alumno y programación de curso
    Optional<Matricula> findByAlumnoIdUsuarioAndProgramacionCursoIdProgramacionCurso(Integer alumnoId, Integer idProgramacionCurso);

    // listar matrículas pendientes de aprobación por el administrador
    @Query("SELECT m FROM Matricula m WHERE m.estado IN ('PENDIENTE', 'EN_PROCESO')")
    List<Matricula> findPendientesDeAprobacion();
}