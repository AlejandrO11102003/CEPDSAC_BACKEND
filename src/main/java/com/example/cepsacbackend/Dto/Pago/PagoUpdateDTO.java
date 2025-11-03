package com.example.cepsacbackend.dto.Pago;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PagoUpdateDTO(
    @NotNull
    Short idMetodoPago,
    @NotNull
    BigDecimal monto,
    @NotNull
    Short numeroCuota,
    @NotNull
    Integer idUsuarioRegistro
) {}