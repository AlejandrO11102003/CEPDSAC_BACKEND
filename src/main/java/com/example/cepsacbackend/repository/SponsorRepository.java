package com.example.cepsacbackend.repository;

import com.example.cepsacbackend.model.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Integer> {
    
    List<Sponsor> findAllByOrderByIdSponsorAsc();
    boolean existsByNombre(String nombre);
}
