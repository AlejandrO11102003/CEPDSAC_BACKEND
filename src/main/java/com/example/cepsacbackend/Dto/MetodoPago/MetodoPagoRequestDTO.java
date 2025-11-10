package com.example.cepsacbackend.dto.MetodoPago;

import com.example.cepsacbackend.enums.TipoMetodo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetodoPagoRequestDTO {

    @NotNull
    private TipoMetodo tipoMetodo;

    @NotBlank
    private String descripcion;

    @NotNull
    private String requisitos;
}
