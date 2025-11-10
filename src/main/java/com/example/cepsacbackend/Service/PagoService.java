package com.example.cepsacbackend.service;

import java.util.List;

import com.example.cepsacbackend.dto.Pago.PagoCreateDTO;
import com.example.cepsacbackend.dto.Pago.PagoResponseDTO;
import com.example.cepsacbackend.dto.Pago.PagoUpdateDTO;

import jakarta.validation.Valid;

public interface PagoService {
    
    PagoResponseDTO registrarPago(@Valid PagoCreateDTO dto);
    
    List<PagoResponseDTO> listarPagosPorMatricula(Integer idMatricula);
    
    PagoResponseDTO actualizarPago(Integer id, PagoUpdateDTO dto);
}
