package com.example.cepsacbackend.dto.Descuento;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.cepsacbackend.enums.TipoDescuento;

import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class DescuentoUpdateDTO {

    @NotNull
    private Short idDescuento;

    @NotNull
    private TipoDescuento tipoDescuento;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal valor;

    private Boolean vigente;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    // id del usuario que edita (opcional), no cambia el creador en bd
    @Positive
    private Integer idUsuario;


}
