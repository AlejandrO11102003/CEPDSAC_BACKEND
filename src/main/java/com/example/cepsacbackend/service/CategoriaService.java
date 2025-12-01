package com.example.cepsacbackend.service;

import java.util.List;

import com.example.cepsacbackend.dto.Categoria.CategoriaCreateDTO;
import com.example.cepsacbackend.dto.Categoria.CategoriaResponseDTO;

public interface CategoriaService {
    List<CategoriaResponseDTO> findByEstadoTrue();
    List<CategoriaResponseDTO> findAll();
    CategoriaResponseDTO obtenerPorId(Short id);
    CategoriaResponseDTO crear(CategoriaCreateDTO dto);
    void eliminar(Short id);
    void cambiarEstado(Short id);
}
