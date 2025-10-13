package com.example.cepsacbackend.Dto.MetodoPago;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.example.cepsacbackend.Enums.TipoMetodo;

public record MetodoPagoRequestDTO(
    @NotNull
    TipoMetodo tipoMetodo,
    @NotBlank
    String descripcion,
    String requisitos // Puede ser nulo
) {}