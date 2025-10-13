package com.example.cepsacbackend.Repository;

import com.example.cepsacbackend.Entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Short> {
    
}
