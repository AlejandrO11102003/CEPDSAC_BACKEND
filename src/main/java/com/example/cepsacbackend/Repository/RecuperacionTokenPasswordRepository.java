package com.example.cepsacbackend.repository;

import com.example.cepsacbackend.model.RecuperacionPasswordToken;
import com.example.cepsacbackend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RecuperacionTokenPasswordRepository extends JpaRepository<RecuperacionPasswordToken, Integer> {

    Optional<RecuperacionPasswordToken> findByToken(String token);

    Optional<RecuperacionPasswordToken> findByUsuarioAndUtilizadoFalse(Usuario usuario);

    @Modifying
    @Query("DELETE FROM RecuperacionPasswordToken t WHERE t.fechaExpiracion < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);

    @Modifying
    @Query("DELETE FROM RecuperacionPasswordToken t WHERE t.usuario = :usuario")
    void deleteByUsuario(@Param("usuario") Usuario usuario);
}
