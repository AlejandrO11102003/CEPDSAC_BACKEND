package com.example.cepsacbackend.Dto.DescuentoAplicacion;

import com.example.cepsacbackend.Enums.TipoAplicacionDescuento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DescuentoAplicacionResponseDTO {

    private Integer idDescuentoAplicacion;

    private TipoAplicacionDescuento tipoAplicacion;

    // Info del descuento
    private Short idDescuento;
    private String infoDescuento;

    // Info de la aplicación
    private Short idCategoria;
    private String nombreCategoria;
    private Short idCursoDiplomado;
    private String tituloCursoDiplomado;
    private String nombreUsuario;
}