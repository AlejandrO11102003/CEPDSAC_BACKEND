package com.example.cepsacbackend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cepsacbackend.model.Pais;

@Repository
public interface PaisRepository extends JpaRepository<Pais, Short> {

    Optional<Pais> findByNombre(String nombre);
}