package com.example.cepsacbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.cepsacbackend.model.CursoDiplomado;

import java.util.Optional;
import java.util.List;

@Repository
public interface CursoDiplomadoRepository extends JpaRepository<CursoDiplomado, Short> {
    
    // cargamos la categoria junto con el curso/diplomado
    @Query("SELECT c FROM CursoDiplomado c LEFT JOIN FETCH c.categoria")
    List<CursoDiplomado> findAllWithCategoria();
    
    // cargamos la categoria junto con el curso/diplomado por id
    @Query("SELECT c FROM CursoDiplomado c LEFT JOIN FETCH c.categoria WHERE c.idCursoDiplomado = :id")
    Optional<CursoDiplomado> findByIdWithCategoria(@Param("id") Short id);
}
