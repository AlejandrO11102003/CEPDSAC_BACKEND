package com.example.cepsacbackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.cepsacbackend.model.Usuario;
import com.example.cepsacbackend.enums.EstadoUsuario;
import com.example.cepsacbackend.enums.Rol;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreo(String correo);
    List<Usuario> findByEstado(EstadoUsuario estado);

    // buscamos todos los user activos e inactivos, solo filtramos suspendidos
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.pais LEFT JOIN FETCH u.tipoIdentificacion WHERE u.estado != 'suspendido'")
    List<Usuario> findAllActivos();

    // buscamos user por id, pero solo si no estan suspendidos
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.pais LEFT JOIN FETCH u.tipoIdentificacion WHERE u.estado != 'suspendido' AND u.idUsuario = :id")
    Optional<Usuario> findByIdActivo(@Param("id") Integer id);

    // buscamos user por correo, pero solo si no estan suspendidos
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.pais LEFT JOIN FETCH u.tipoIdentificacion WHERE u.estado != 'suspendido' AND u.correo = :correo")
    Optional<Usuario> findByCorreoActivo(@Param("correo") String correo);

    // buscamos todos los user activos e inactivos de un rol especifico, solo filtramos suspendidos
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.pais LEFT JOIN FETCH u.tipoIdentificacion WHERE u.estado != 'suspendido' AND u.rol = :rol")
    List<Usuario> findByRolActivo(@Param("rol") Rol rol);
}
