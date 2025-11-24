package com.example.cepsacbackend.repository;

import com.example.cepsacbackend.model.ConfiguracionContacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracionContactoRepository extends JpaRepository<ConfiguracionContacto, Integer> {
}
