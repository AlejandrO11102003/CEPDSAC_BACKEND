package com.example.cepsacbackend.dto.MetodoPago;

import com.example.cepsacbackend.enums.TipoMetodo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MetodoPagoRequestDTO(
    @NotNull
    TipoMetodo tipoMetodo,
    @NotBlank
    String descripcion,
    String requisitos // Puede ser nulo
) {}