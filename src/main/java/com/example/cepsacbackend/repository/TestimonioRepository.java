package com.example.cepsacbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cepsacbackend.model.Testimonio;

@Repository
public interface TestimonioRepository extends JpaRepository<Testimonio, Integer> {
    
}
