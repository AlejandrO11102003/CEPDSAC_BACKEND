package com.example.cepsacbackend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.cepsacbackend.model.ProgramacionCurso;

@Repository
public interface ProgramacionCursoRepository extends JpaRepository<ProgramacionCurso, Integer> {

    @Query("SELECT pc FROM ProgramacionCurso pc WHERE pc.fechaFin > :fechaActual")
    List<ProgramacionCurso> findAllAvailable(LocalDate fechaActual);

    ProgramacionCurso findByIdProgramacionCurso(int id);
}
