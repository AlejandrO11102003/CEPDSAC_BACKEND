package com.example.cepsacbackend.Service;

import com.example.cepsacbackend.Dto.DescuentoAplicacion.DescuentoAplicacionCreateDTO;
import com.example.cepsacbackend.Dto.DescuentoAplicacion.DescuentoAplicacionResponseDTO;

import java.util.List;

public interface DescuentoAplicacionService {
    List<DescuentoAplicacionResponseDTO> listar();
    DescuentoAplicacionResponseDTO obtenerPorId(Integer id);
    DescuentoAplicacionResponseDTO crear(DescuentoAplicacionCreateDTO dto);
    void eliminar(Integer id);
}