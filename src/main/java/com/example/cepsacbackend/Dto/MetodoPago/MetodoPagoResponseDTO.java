package com.example.cepsacbackend.Dto.MetodoPago;

import com.example.cepsacbackend.Enums.TipoMetodo;

public record MetodoPagoResponseDTO(
    Short idMetodoPago,
    TipoMetodo tipoMetodo,
    String descripcion,
    String requisitos,
    Boolean activo
) {}

