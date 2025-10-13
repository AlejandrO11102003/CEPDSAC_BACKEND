package com.example.cepsacbackend.Service;

import com.example.cepsacbackend.Dto.Categoria.CategoriaCreateDTO;
import com.example.cepsacbackend.Dto.Categoria.CategoriaResponseDTO;

import java.util.List;

public interface CategoriaService {
    List<CategoriaResponseDTO> listar();
    CategoriaResponseDTO obtenerPorId(Short id);
    CategoriaResponseDTO crear(CategoriaCreateDTO dto);
    void eliminar(Short id);
}
