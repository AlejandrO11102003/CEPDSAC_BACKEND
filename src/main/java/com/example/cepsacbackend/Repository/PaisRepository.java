package com.example.cepsacbackend.Repository;

import com.example.cepsacbackend.Entity.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaisRepository extends JpaRepository<Pais, Short> {

    java.util.Optional<Pais> findByNombre(String nombre);
}