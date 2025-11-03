package com.example.cepsacbackend.dto.Pago;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record PagoCreateDTO(

    @NotNull 
    Integer idMatricula,

    @NotNull 
    Short idMetodoPago,

    @NotNull 
    BigDecimal monto,
    
    @NotNull 
    Integer idUsuarioRegistro
) {}
