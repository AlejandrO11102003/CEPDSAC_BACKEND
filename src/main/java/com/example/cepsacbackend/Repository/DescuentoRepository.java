package com.example.cepsacbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cepsacbackend.model.Descuento;

public interface DescuentoRepository extends JpaRepository<Descuento, Short> {
}
