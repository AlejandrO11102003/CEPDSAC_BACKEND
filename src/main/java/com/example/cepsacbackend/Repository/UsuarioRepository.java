package com.example.cepsacbackend.Repository;

import com.example.cepsacbackend.Entity.Usuario;
import com.example.cepsacbackend.Enums.EstadoUsuario;
import java.util.List;
import java.util.Optional;
import com.example.cepsacbackend.Enums.Rol;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreo(String correo);
    List<Usuario> findByEstado(EstadoUsuario estado);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.pais LEFT JOIN FETCH u.tipoIdentificacion WHERE u.estado != 'suspendido'")
    List<Usuario> findAllActivos();

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.pais LEFT JOIN FETCH u.tipoIdentificacion WHERE u.estado != 'suspendido' AND u.idUsuario = :id")
    Optional<Usuario> findByIdActivo(@Param("id") Integer id);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.pais LEFT JOIN FETCH u.tipoIdentificacion WHERE u.estado != 'suspendido' AND u.correo = :correo")
    Optional<Usuario> findByCorreoActivo(@Param("correo") String correo);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.pais LEFT JOIN FETCH u.tipoIdentificacion WHERE u.estado != 'suspendido' AND u.rol = :rol")
    List<Usuario> findByRolActivo(@Param("rol") Rol rol);
}
