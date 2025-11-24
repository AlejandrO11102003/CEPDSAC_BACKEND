package com.example.cepsacbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.cepsacbackend.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Short> {
    
    @Query("SELECT c FROM Categoria c WHERE c.estado = true")   
    List<Categoria> findByEstadoTrue();
}
