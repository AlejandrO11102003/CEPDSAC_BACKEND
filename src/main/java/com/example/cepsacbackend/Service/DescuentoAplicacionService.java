package com.example.cepsacbackend.service;

import java.util.List;

import com.example.cepsacbackend.dto.DescuentoAplicacion.DescuentoAplicacionCreateDTO;
import com.example.cepsacbackend.dto.DescuentoAplicacion.DescuentoAplicacionResponseDTO;

public interface DescuentoAplicacionService {
    List<DescuentoAplicacionResponseDTO> listar();
    DescuentoAplicacionResponseDTO obtenerPorId(Integer id);
    DescuentoAplicacionResponseDTO crear(DescuentoAplicacionCreateDTO dto);
    void eliminar(Integer id);
    DescuentoAplicacionResponseDTO actualizar(Integer id, DescuentoAplicacionCreateDTO dto);
}