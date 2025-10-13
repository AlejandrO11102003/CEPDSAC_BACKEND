package com.example.cepsacbackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.cepsacbackend.Entity.MetodoPago;

import java.util.List;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Short> {
    List<MetodoPago> findByActivoTrue();
}