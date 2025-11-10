package com.example.cepsacbackend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

import com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoResponseDTO;
import com.example.cepsacbackend.model.ProgramacionCurso;

@Repository
public interface ProgramacionCursoRepository extends JpaRepository<ProgramacionCurso, Integer> {

    @Query("SELECT NEW com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoResponseDTO(" +
           "pc.idProgramacionCurso, " +
           "pc.modalidad, " +
           "pc.duracionCurso, " +
           "pc.horasSemanales, " +
           "pc.fechaInicio, " +
           "pc.fechaFin, " +
           "pc.monto, " +
           "cd.idCursoDiplomado, " +
           "cd.titulo, " +
           "pc.duracionMeses) " +
           "FROM ProgramacionCurso pc " +
           "JOIN pc.cursoDiplomado cd")
    List<ProgramacionCursoResponseDTO> findAllAsDTO();

    // cargamos la programacion junto con el curso/diplomado
    @Query("SELECT pc FROM ProgramacionCurso pc " +
           "LEFT JOIN FETCH pc.cursoDiplomado " +
           "WHERE pc.fechaFin > :fechaActual")
    List<ProgramacionCurso> findAllAvailable(LocalDate fechaActual);

    // cargamos la programacion junto con el curso/diplomado por id
    @Query("SELECT pc FROM ProgramacionCurso pc " +
           "LEFT JOIN FETCH pc.cursoDiplomado " +
           "WHERE pc.idProgramacionCurso = :id")
    Optional<ProgramacionCurso> findByIdWithCurso(@Param("id") Integer id);
}
