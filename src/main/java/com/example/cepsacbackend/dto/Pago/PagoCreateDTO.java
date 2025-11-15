package com.example.cepsacbackend.dto.Pago;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoCreateDTO{    
    @NotNull(message = "El ID de la matrícula es obligatorio")
    private Integer idMatricula;
    
    @NotNull(message = "El ID del método de pago es obligatorio")
    private Short idMetodoPago;
    
    @Positive(message = "El monto debe ser mayor a cero")
    private BigDecimal monto; //opcional , si no lo enviamos usa el automatico creado en cuota
}
