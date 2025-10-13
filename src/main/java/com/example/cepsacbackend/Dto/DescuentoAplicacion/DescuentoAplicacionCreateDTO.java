package com.example.cepsacbackend.Dto.DescuentoAplicacion;

import com.example.cepsacbackend.Enums.TipoAplicacionDescuento;
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

    private Integer idUsuario; // Usuario que registra
}