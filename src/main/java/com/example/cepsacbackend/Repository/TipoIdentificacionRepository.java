package com.example.cepsacbackend.Repository;

import com.example.cepsacbackend.Entity.TipoIdentificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoIdentificacionRepository extends JpaRepository<TipoIdentificacion, Short> {
    java.util.Optional<TipoIdentificacion> findByNombre(String nombre);
}