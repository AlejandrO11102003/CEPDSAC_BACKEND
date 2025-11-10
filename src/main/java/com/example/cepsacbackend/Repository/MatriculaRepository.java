package com.example.cepsacbackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.cepsacbackend.dto.Matricula.MatriculaListResponseDTO;
import com.example.cepsacbackend.model.Matricula;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {

    //verifico si ya existe una matricula para el mismo alumno y programacion de curso
    Optional<Matricula> findByAlumnoIdUsuarioAndProgramacionCursoIdProgramacionCurso(Integer alumnoId, Integer idProgramacionCurso);

    //listo matriculas pendientes de aprobacion por el administrador
    @Query("SELECT m FROM Matricula m WHERE m.estado IN ('PENDIENTE', 'EN_PROCESO')")
    List<Matricula> findPendientesDeAprobacion();
    
    //cargo todas las relaciones necesarias en una sola consulta para evitar n+1 queries
    @Query("SELECT m FROM Matricula m " +
           "LEFT JOIN FETCH m.alumno " +
           "LEFT JOIN FETCH m.programacionCurso pc " +
           "LEFT JOIN FETCH pc.cursoDiplomado " +
           "LEFT JOIN FETCH m.descuento " +
           "WHERE m.idMatricula = :id")
    Optional<Matricula> findByIdWithDetails(@Param("id") Integer id);
    
    //uso dto projection para optimizar la consulta y traer solo los campos necesarios
    @Query("SELECT NEW com.example.cepsacbackend.dto.Matricula.MatriculaListResponseDTO(" +
           "m.idMatricula, " +
           "m.fechaMatricula, " +
           "m.estado, " +
           "m.monto, " +
           "a.nombre, " +
           "a.apellido, " +
           "a.correo, " +
           "cd.titulo, " +
           "c.nombre, " +
           "COALESCE(d.valor, 0), " +
           "COALESCE(m.montoDescontado, 0), " +
           "m.pagoPersonalizado) " +
           "FROM Matricula m " +
           "JOIN m.alumno a " +
           "JOIN m.programacionCurso pc " +
           "JOIN pc.cursoDiplomado cd " +
           "LEFT JOIN cd.categoria c " +
           "LEFT JOIN m.descuento d " +
           "ORDER BY m.fechaMatricula DESC")
    List<MatriculaListResponseDTO> findAllAsListDTO();
    
    //uso dto projection para optimizar la consulta y traer solo los campos necesarios
    @Query("SELECT NEW com.example.cepsacbackend.dto.Matricula.MatriculaListResponseDTO(" +
           "m.idMatricula, " +
           "m.fechaMatricula, " +
           "m.estado, " +
           "m.monto, " +
           "a.nombre, " +
           "a.apellido, " +
           "a.correo, " +
           "cd.titulo, " +
           "c.nombre, " +
           "COALESCE(d.valor, 0), " +
           "COALESCE(m.montoDescontado, 0), " +
           "m.pagoPersonalizado) " +
           "FROM Matricula m " +
           "JOIN m.alumno a " +
           "JOIN m.programacionCurso pc " +
           "JOIN pc.cursoDiplomado cd " +
           "LEFT JOIN cd.categoria c " +
           "LEFT JOIN m.descuento d " +
           "WHERE a.idUsuario = :idAlumno " +
           "ORDER BY m.fechaMatricula DESC")
    List<MatriculaListResponseDTO> findByAlumnoIdAsListDTO(@Param("idAlumno") Integer idAlumno);
}