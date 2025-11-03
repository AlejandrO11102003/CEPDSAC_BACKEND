package com.example.cepsacbackend.dto.Descuento;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.cepsacbackend.enums.TipoDescuento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DescuentoResponseDTO {
    private Short idDescuento;
    private TipoDescuento tipoDescuento;
    private BigDecimal valor;
    private Boolean vigente;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer idUsuario;

}
