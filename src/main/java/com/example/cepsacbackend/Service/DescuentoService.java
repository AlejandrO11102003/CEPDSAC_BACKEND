package com.example.cepsacbackend.Service;

import com.example.cepsacbackend.Dto.Descuento.DescuentoCreateDTO;
import com.example.cepsacbackend.Dto.Descuento.DescuentoResponseDTO;
import com.example.cepsacbackend.Dto.Descuento.DescuentoUpdateDTO;

import java.util.List;

public interface DescuentoService {
    List<DescuentoResponseDTO> listar();
    DescuentoResponseDTO obtener(Short idDescuento);
    DescuentoResponseDTO crear(DescuentoCreateDTO dto);
    DescuentoResponseDTO actualizar(DescuentoUpdateDTO dto);
    void eliminar(Short idDescuento);
}
