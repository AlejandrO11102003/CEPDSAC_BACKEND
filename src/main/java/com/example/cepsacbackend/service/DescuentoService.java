package com.example.cepsacbackend.service;

import java.util.List;

import com.example.cepsacbackend.dto.Descuento.DescuentoCreateDTO;
import com.example.cepsacbackend.dto.Descuento.DescuentoResponseDTO;
import com.example.cepsacbackend.dto.Descuento.DescuentoUpdateDTO;

public interface DescuentoService {
    List<DescuentoResponseDTO> listar();
    DescuentoResponseDTO obtener(Short idDescuento);
    DescuentoResponseDTO crear(DescuentoCreateDTO dto);
    DescuentoResponseDTO actualizar(DescuentoUpdateDTO dto);
    void eliminar(Short idDescuento);
}
