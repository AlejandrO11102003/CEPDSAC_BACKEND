package com.example.cepsacbackend.Dto.Pago;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PagoCreateDTO(
    @NotNull Integer idMatricula,
    @NotNull Short idMetodoPago,
    @NotNull BigDecimal monto,
    @NotNull Integer idUsuarioRegistro
) {}
