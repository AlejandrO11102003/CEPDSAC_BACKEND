package com.example.cepsacbackend.dto.Matricula;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AplicarDescuentoDTO(
    @NotNull(message = "El ID del descuento es obligatorio")
    @Positive(message = "El ID del descuento debe ser un número positivo")
    Short idDescuento
) {
}
