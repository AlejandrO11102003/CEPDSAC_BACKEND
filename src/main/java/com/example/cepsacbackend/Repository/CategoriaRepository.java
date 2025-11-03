package com.example.cepsacbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cepsacbackend.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Short> {
    
}
