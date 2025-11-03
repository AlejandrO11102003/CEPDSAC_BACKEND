package com.example.cepsacbackend.dto.MetodoPago;

import com.example.cepsacbackend.enums.TipoMetodo;

public record MetodoPagoResponseDTO(
    Short idMetodoPago,
    TipoMetodo tipoMetodo,
    String descripcion,
    String requisitos,
    Boolean activo
) {}

