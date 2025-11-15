package com.example.cepsacbackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.cepsacbackend.model.Usuario;
import com.example.cepsacbackend.enums.EstadoUsuario;
import com.example.cepsacbackend.enums.Rol;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Cacheable(value = "usuarios", key = "'correo_' + #correo")
    Optional<Usuario> findByCorreo(String correo);

    List<Usuario> findByEstado(EstadoUsuario estado);
    
    // solo verifica existencia sin cargar la entidad completa
    boolean existsByCorreo(String correo);

    // lista todos los usuarios activos con solo los campos del DTO de listado
    @Query("SELECT new com.example.cepsacbackend.dto.Usuario.UsuarioListResponseDTO(" +
           "u.idUsuario, " +
           "u.nombre, " +
           "u.apellido, " +
           "u.correo, " +
           "u.rol, " +
           "CASE WHEN u.estado = com.example.cepsacbackend.enums.EstadoUsuario.ACTIVO THEN true ELSE false END, " +
           "u.tipoIdentificacion.iniciales, " +
           "u.numeroIdentificacion) " +
           "FROM Usuario u " +
           "WHERE u.estado != com.example.cepsacbackend.enums.EstadoUsuario.SUSPENDIDO " +
           "ORDER BY u.nombre")
    List<com.example.cepsacbackend.dto.Usuario.UsuarioListResponseDTO> findAllActivosAsDTO();
    
    // lista usuarios por rol con solo los campos del DTO de listado
    @Query("SELECT new com.example.cepsacbackend.dto.Usuario.UsuarioListResponseDTO(" +
           "u.idUsuario, " +
           "u.nombre, " +
           "u.apellido, " +
           "u.correo, " +
           "u.rol, " +
           "CASE WHEN u.estado = com.example.cepsacbackend.enums.EstadoUsuario.ACTIVO THEN true ELSE false END, " +
           "u.tipoIdentificacion.iniciales, " +
           "u.numeroIdentificacion) " +
           "FROM Usuario u " +
           "WHERE u.estado != com.example.cepsacbackend.enums.EstadoUsuario.SUSPENDIDO AND u.rol = :rol " +
           "ORDER BY u.nombre")
    List<com.example.cepsacbackend.dto.Usuario.UsuarioListResponseDTO> findByRolActivoAsDTO(@Param("rol") Rol rol);

    // busca usuario por ID con todas sus relaciones cargadas
    @Query("SELECT u FROM Usuario u " +
           "LEFT JOIN FETCH u.pais " +
           "LEFT JOIN FETCH u.tipoIdentificacion " +
           "WHERE u.estado != com.example.cepsacbackend.enums.EstadoUsuario.SUSPENDIDO AND u.idUsuario = :id")
    Optional<Usuario> findByIdActivo(@Param("id") Integer id);

    // busca usuario por correo con todas sus relaciones cargadas
    @Query("SELECT u FROM Usuario u " +
           "LEFT JOIN FETCH u.pais " +
           "LEFT JOIN FETCH u.tipoIdentificacion " +
           "WHERE u.estado != com.example.cepsacbackend.enums.EstadoUsuario.SUSPENDIDO AND u.correo = :correo")
    Optional<Usuario> findByCorreoActivo(@Param("correo") String correo);
}
