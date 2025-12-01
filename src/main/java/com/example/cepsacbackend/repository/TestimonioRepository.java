package com.example.cepsacbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

import com.example.cepsacbackend.model.Testimonio;

@Repository
public interface TestimonioRepository extends JpaRepository<Testimonio, Integer> {
    @Query("SELECT t FROM Testimonio t JOIN FETCH t.idUsuario")
    List<Testimonio> findAll();
}
