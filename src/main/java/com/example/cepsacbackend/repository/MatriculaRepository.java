package com.example.cepsacbackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.cepsacbackend.dto.Matricula.MatriculaAdminListDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaListResponseDTO;
import com.example.cepsacbackend.enums.EstadoMatricula;
import com.example.cepsacbackend.model.Matricula;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {

    //verifico si ya existe una matricula para el mismo alumno y programacion de curso
    Optional<Matricula> findByAlumnoIdUsuarioAndProgramacionCursoIdProgramacionCurso(Integer alumnoId, Integer idProgramacionCurso);

    //verifico si existe una matricula activa (no cancelada/rechazada) para el mismo alumno y programacion de curso
    @Query("SELECT m FROM Matricula m " +
           "WHERE m.alumno.idUsuario = :idAlumno " +
           "AND m.programacionCurso.idProgramacionCurso = :idProgramacion " +
           "AND m.estado IN :estados")
    Optional<Matricula> findMatriculaActivaByAlumnoAndProgramacion(
        @Param("idAlumno") Integer idAlumno,
        @Param("idProgramacion") Integer idProgramacion,
        @Param("estados") List<EstadoMatricula> estados
    );

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

       // cuenta matriculas por estado
       long countByEstado(com.example.cepsacbackend.enums.EstadoMatricula estado);

       @Query("SELECT COUNT(DISTINCT m.alumno.idUsuario) FROM Matricula m WHERE m.estado = :estado")
       long countDistinctAlumnoByEstado(@Param("estado") com.example.cepsacbackend.enums.EstadoMatricula estado);

       @Query("SELECT COUNT(DISTINCT m.alumno.idUsuario) FROM Matricula m")
       long countDistinctAlumno();

       //listo matriculas con pagos paginados apr admin
       @Query("""
              SELECT NEW com.example.cepsacbackend.dto.Matricula.MatriculaAdminListDTO(
              m.idMatricula,
              m.fechaMatricula,
              m.estado,
              m.monto,
              CONCAT(a.nombre, ' ', a.apellido),
              a.correo,
              a.numeroIdentificacion,
              cd.titulo,
              pc.fechaInicio,
              COUNT(p.idPago) AS totalCuotas,
              SUM(CASE WHEN p.estadoCuota = 'PAGADO' THEN 1 ELSE 0 END) AS cuotasPagadas,
              COALESCE(SUM(CASE WHEN p.estadoCuota = 'PAGADO' THEN p.montoPagado ELSE 0 END), 0) AS montoPagado,
              (m.monto - COALESCE(SUM(CASE WHEN p.estadoCuota = 'PAGADO' THEN p.montoPagado ELSE 0 END), 0)) AS saldoPendiente,
              MIN(CASE WHEN p.estadoCuota = 'PENDIENTE' THEN p.fechaVencimiento ELSE NULL END) AS proximoVencimiento,
              CASE 
                     WHEN SUM(CASE WHEN p.estadoCuota = 'PENDIENTE' AND p.fechaVencimiento < CURRENT_DATE THEN 1 ELSE 0 END) > 0 
                     THEN true 
                     ELSE false 
              END AS tieneVencidas
              )
              FROM Matricula m
              JOIN m.alumno a
              JOIN m.programacionCurso pc
              JOIN pc.cursoDiplomado cd
              LEFT JOIN Pago p ON p.matricula.idMatricula = m.idMatricula

              WHERE (:estado IS NULL OR m.estado = :estado)
              AND   (:dni IS NULL OR a.numeroIdentificacion LIKE %:dni%)
              GROUP BY 
              m.idMatricula,
              m.fechaMatricula,
              m.estado,
              m.monto,
              a.nombre, a.apellido, a.correo, a.numeroIdentificacion,
              cd.titulo,
              pc.fechaInicio
              ORDER BY m.fechaMatricula DESC
              """)
              Page<MatriculaAdminListDTO> findMatriculasAdmin(@Param("dni") String dni, @Param("estado") EstadoMatricula estado, Pageable pageable);

       // buscar matriculas activas por programacion de curso para cancelacion masiva
       @Query("SELECT m FROM Matricula m " +
              "LEFT JOIN FETCH m.alumno " +
              "WHERE m.programacionCurso.idProgramacionCurso = :idProgramacion " +
              "AND m.estado IN :estados")
       List<Matricula> findByProgramacionAndEstados(
           @Param("idProgramacion") Integer idProgramacion,
           @Param("estados") List<EstadoMatricula> estados
       );

       // contar inscritos por programacion
       long countByProgramacionCursoIdProgramacionCurso(Integer idProgramacionCurso);

}