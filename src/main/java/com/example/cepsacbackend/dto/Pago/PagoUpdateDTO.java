package com.example.cepsacbackend.dto.Pago;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;


@NonNull
@Data
@AllArgsConstructor
public class PagoUpdateDTO{
    private Short idMetodoPago;
    private BigDecimal monto;
    private Short numeroCuota;
}