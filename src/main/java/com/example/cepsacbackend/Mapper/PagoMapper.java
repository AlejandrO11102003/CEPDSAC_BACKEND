package com.example.cepsacbackend.Mapper;

import com.example.cepsacbackend.Dto.Pago.PagoResponseDTO;
import com.example.cepsacbackend.Entity.Pago;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PagoMapper {

    @Mapping(target = "metodoPagoDescripcion", source = "metodoPago.descripcion")
    @Mapping(target = "tipoMetodo", source = "metodoPago.tipoMetodo")
    @Mapping(target = "usuarioRegistroNombre", source = "usuarioRegistro.nombre")
    PagoResponseDTO toResponseDTO(Pago pago);

    List<PagoResponseDTO> toResponseDTOList(List<Pago> pagos);
}
