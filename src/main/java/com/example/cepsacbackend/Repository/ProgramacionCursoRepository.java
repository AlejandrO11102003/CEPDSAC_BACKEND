package com.example.cepsacbackend.Repository;

import com.example.cepsacbackend.Entity.ProgramacionCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramacionCursoRepository extends JpaRepository<ProgramacionCurso, Integer> {
}
