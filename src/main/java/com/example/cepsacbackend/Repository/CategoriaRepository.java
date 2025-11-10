package com.example.cepsacbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cepsacbackend.model.Categoria;


@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Short> {
    
}
