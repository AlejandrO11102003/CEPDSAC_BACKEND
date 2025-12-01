package com.example.cepsacbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.cepsacbackend.dto.CursoDiplomado.CursoIndexResponseDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoResponseDTO;
import com.example.cepsacbackend.model.CursoDiplomado;

import java.util.Optional;
import java.util.List;

@Repository
public interface CursoDiplomadoRepository extends JpaRepository<CursoDiplomado, Short> {
    
    // cargamos la categoria junto con el curso/diplomado
    @Query("SELECT c FROM CursoDiplomado c LEFT JOIN FETCH c.categoria")
    List<CursoDiplomado> findAllWithCategoria();

    @Query("SELECT new com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoResponseDTO(" +
           "c.idCursoDiplomado, " +
           "cat.idCategoria, " +
           "cat.nombre, " +
           "c.tipo, " +
           "c.otorgaCertificado, " +
           "c.titulo, " +
           "c.urlCurso, " +
           "c.objetivo) " +
           "FROM CursoDiplomado c " +
           "LEFT JOIN c.categoria cat " +
           "WHERE c.tipo = false")
    List<CursoDiplomadoResponseDTO> findAllCursosAdmin();

    @Query("SELECT new com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoResponseDTO(" +
           "c.idCursoDiplomado, " +
           "cat.idCategoria, " +
           "cat.nombre, " +
           "c.tipo, " +
           "c.otorgaCertificado, " +
           "c.titulo, " +
           "c.urlCurso, " +
           "c.objetivo) " +
           "FROM CursoDiplomado c " +
           "LEFT JOIN c.categoria cat " +
           "WHERE c.tipo = true")
    List<CursoDiplomadoResponseDTO> findAllDiplomadosAdmin();
    
    // cargamos la categoria junto con el curso/diplomado por id
    @Query("SELECT c FROM CursoDiplomado c LEFT JOIN FETCH c.categoria WHERE c.idCursoDiplomado = :id")
    Optional<CursoDiplomado> findByIdWithCategoria(@Param("id") Short id);
    
    // obtener curso/diplomado con categoria y programaciones disponibles
    @Query("SELECT c FROM CursoDiplomado c " +
           "LEFT JOIN FETCH c.categoria " +
           "WHERE c.idCursoDiplomado = :id")
    Optional<CursoDiplomado> findByIdWithCategoriaForDetalle(@Param("id") Short id);
    
    // obtener cursos/diplomados con programaciones disponibles usando DTO projection
    @Query("SELECT new com.example.cepsacbackend.dto.CursoDiplomado.CursoIndexResponseDTO(" +
           "c.idCursoDiplomado, " +
           "c.urlCurso, " +
           "c.titulo, " +
           "cat.idCategoria, " +
           "cat.nombre) " +
           "FROM CursoDiplomado c " +
           "LEFT JOIN c.categoria cat " +
           "JOIN ProgramacionCurso pc ON pc.cursoDiplomado.idCursoDiplomado = c.idCursoDiplomado " +
           "WHERE pc.fechaFin > CURRENT_DATE " +
           "GROUP BY c.idCursoDiplomado, c.urlCurso, c.titulo, cat.idCategoria, cat.nombre")
    List<CursoIndexResponseDTO> findAllWithAvailableProgramacionDTO();
    
    // obtener SOLO CURSOS (tipo=false) con programaciones disponibles
    @Query("SELECT new com.example.cepsacbackend.dto.CursoDiplomado.CursoIndexResponseDTO(" +
           "c.idCursoDiplomado, " +
           "c.urlCurso, " +
           "c.titulo, " +
           "cat.idCategoria, " +
           "cat.nombre) " +
           "FROM CursoDiplomado c " +
           "LEFT JOIN c.categoria cat " +
           "JOIN ProgramacionCurso pc ON pc.cursoDiplomado.idCursoDiplomado = c.idCursoDiplomado " +
           "WHERE pc.fechaFin > CURRENT_DATE AND c.tipo = false " +
           "GROUP BY c.idCursoDiplomado, c.urlCurso, c.titulo, cat.idCategoria, cat.nombre")
    List<CursoIndexResponseDTO> findCursosWithAvailableProgramacionDTO();
    
    // obtener SOLO DIPLOMADOS (tipo=true) con programaciones disponibles
    @Query("SELECT new com.example.cepsacbackend.dto.CursoDiplomado.CursoIndexResponseDTO(" +
           "c.idCursoDiplomado, " +
           "c.urlCurso, " +
           "c.titulo, " +
           "cat.idCategoria, " +
           "cat.nombre) " +
           "FROM CursoDiplomado c " +
           "LEFT JOIN c.categoria cat " +
           "JOIN ProgramacionCurso pc ON pc.cursoDiplomado.idCursoDiplomado = c.idCursoDiplomado " +
           "WHERE pc.fechaFin > CURRENT_DATE AND c.tipo = true " +
           "GROUP BY c.idCursoDiplomado, c.urlCurso, c.titulo, cat.idCategoria, cat.nombre")
    List<CursoIndexResponseDTO> findDiplomadosWithAvailableProgramacionDTO();
}
