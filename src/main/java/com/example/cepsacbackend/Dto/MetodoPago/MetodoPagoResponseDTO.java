package com.example.cepsacbackend.dto.MetodoPago;

import com.example.cepsacbackend.enums.TipoMetodo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NonNull
public class MetodoPagoResponseDTO {

    private Short idMetodoPago;
    private TipoMetodo tipoMetodo;
    private String descripcion;
    private String requisitos;
    private Boolean activo;
}
