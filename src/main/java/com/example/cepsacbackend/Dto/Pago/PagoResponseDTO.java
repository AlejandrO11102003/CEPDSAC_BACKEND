package com.example.cepsacbackend.Dto.Pago;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagoResponseDTO(
        Integer idPago,
        BigDecimal monto,
        Short numeroCuota,
        LocalDateTime fechaPago,
        String metodoPagoDescripcion,
        String tipoMetodo,
        String usuarioRegistroNombre
) {}
