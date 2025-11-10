package com.example.cepsacbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cepsacbackend.model.Descuento;

@Repository
public interface DescuentoRepository extends JpaRepository<Descuento, Short> {
}
