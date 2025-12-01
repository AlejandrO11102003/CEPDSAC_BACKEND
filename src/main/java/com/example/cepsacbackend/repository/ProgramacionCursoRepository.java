package com.example.cepsacbackend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

import com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoListResponseDTO;
import com.example.cepsacbackend.model.ProgramacionCurso;

@Repository
public interface ProgramacionCursoRepository extends JpaRepository<ProgramacionCurso, Integer> {

       @Query("SELECT NEW com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoListResponseDTO(" +
              "pc.idProgramacionCurso, " +
              "pc.fechaInicio, " +
              "pc.fechaFin, " +
              "pc.monto, " +
              "cd.titulo, " +
              "pc.duracionMeses, " +
              "CONCAT(doc.nombre, ' ', doc.apellido), " +
              "COUNT(m)) " +
              "FROM ProgramacionCurso pc " +
              "JOIN pc.cursoDiplomado cd " +
              "LEFT JOIN pc.docente doc " +
              "LEFT JOIN Matricula m ON m.programacionCurso = pc " +
              "GROUP BY pc.idProgramacionCurso, pc.fechaInicio, pc.fechaFin, pc.monto, cd.titulo, pc.duracionMeses, doc.nombre, doc.apellido")
       List<ProgramacionCursoListResponseDTO> findAllAsDTO();

       // obtener programaciones disponibles (fecha fin > fecha actual y estado ACTIVO) como DTO
       @Query("SELECT NEW com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoListResponseDTO(" +
              "pc.idProgramacionCurso, " +
              "pc.fechaInicio, " +
              "pc.fechaFin, " +
              "pc.monto, " +
              "cd.titulo, " +
              "pc.duracionMeses, " +
              "CONCAT(doc.nombre, ' ', doc.apellido), " +
              "COUNT(m)) " +
              "FROM ProgramacionCurso pc " +
              "JOIN pc.cursoDiplomado cd " +
              "LEFT JOIN pc.docente doc " +
              "LEFT JOIN Matricula m ON m.programacionCurso = pc " +
              "WHERE pc.fechaFin > :fechaActual " +
              "AND pc.estado = 'ACTIVO' " +
              "GROUP BY pc.idProgramacionCurso, pc.fechaInicio, pc.fechaFin, pc.monto, cd.titulo, pc.duracionMeses, doc.nombre, doc.apellido")
       List<ProgramacionCursoListResponseDTO> findAllAvailableAsDTO(@Param("fechaActual") LocalDate fechaActual);

       // cargamos la programacion junto con el curso/diplomado
       @Query("SELECT pc FROM ProgramacionCurso pc " +
              "LEFT JOIN FETCH pc.cursoDiplomado " +
              "WHERE pc.fechaFin > :fechaActual " +
              "AND pc.estado = 'ACTIVO'")
       List<ProgramacionCurso> findAllAvailable(LocalDate fechaActual);

       // cargamos la programacion junto con el curso/diplomado por id
       @Query("SELECT pc FROM ProgramacionCurso pc " +
              "LEFT JOIN FETCH pc.cursoDiplomado " +
              "WHERE pc.idProgramacionCurso = :id")
       Optional<ProgramacionCurso> findByIdWithCurso(@Param("id") Integer id);
       
       // obtener programaciones disponibles por curso/diplomado
       @Query("SELECT NEW com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoSimpleDTO(" +
              "pc.idProgramacionCurso, " +
              "pc.modalidad, " +
              "pc.duracionCurso, " +
              "pc.duracionMeses, " +
              "pc.fechaInicio, " +
              "pc.fechaFin, " +
              "pc.monto, " +
              "pc.numeroCuotas, " +
              "pc.horario, " +
              "doc.idUsuario, " +
              "doc.nombre, " +
              "doc.apellido) " +
              "FROM ProgramacionCurso pc " +
              "LEFT JOIN pc.docente doc " +
              "WHERE pc.cursoDiplomado.idCursoDiplomado = :idCurso " +
              "AND pc.fechaFin > CURRENT_DATE " +
              "AND pc.estado = 'ACTIVO' " +
              "ORDER BY pc.fechaInicio ASC")
       List<com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoSimpleDTO> findAvailableByCursoId(@Param("idCurso") Short idCurso);

       // obtener programaciones disponibles por curso/diplomado y docente
       @Query("SELECT NEW com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoSimpleDTO(" +
              "pc.idProgramacionCurso, " +
              "pc.modalidad, " +
              "pc.duracionCurso, " +
              "pc.duracionMeses, " +
              "pc.fechaInicio, " +
              "pc.fechaFin, " +
              "pc.monto, " +
              "pc.numeroCuotas, " +
              "pc.horario, " +
              "doc.idUsuario, " +
              "doc.nombre, " +
              "doc.apellido) " +
              "FROM ProgramacionCurso pc " +
              "LEFT JOIN pc.docente doc " +
              "WHERE pc.cursoDiplomado.idCursoDiplomado = :idCurso " +
              "AND pc.docente.idUsuario = :idDocente " +
              "AND pc.fechaFin > CURRENT_DATE " +
              "AND pc.estado = 'ACTIVO' " +
              "ORDER BY pc.fechaInicio ASC")
       List<com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoSimpleDTO> findAvailableByCursoIdAndDocenteId(
              @Param("idCurso") Short idCurso, 
              @Param("idDocente") Integer idDocente
       );
}
