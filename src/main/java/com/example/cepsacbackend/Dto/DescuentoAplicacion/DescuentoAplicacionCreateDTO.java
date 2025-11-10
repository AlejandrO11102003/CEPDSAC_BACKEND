package com.example.cepsacbackend.dto.DescuentoAplicacion;

import com.example.cepsacbackend.enums.TipoAplicacionDescuento;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DescuentoAplicacionCreateDTO {

    @NotNull
    private Short idDescuento;

    @NotNull
    private TipoAplicacionDescuento tipoAplicacion;

    private Short idCategoria;

    private Short idCursoDiplomado;

    private Integer idMatricula;
}