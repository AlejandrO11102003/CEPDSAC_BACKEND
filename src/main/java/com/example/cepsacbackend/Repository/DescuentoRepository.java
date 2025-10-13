package com.example.cepsacbackend.Repository;

import com.example.cepsacbackend.Entity.Descuento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DescuentoRepository extends JpaRepository<Descuento, Short> {
}
