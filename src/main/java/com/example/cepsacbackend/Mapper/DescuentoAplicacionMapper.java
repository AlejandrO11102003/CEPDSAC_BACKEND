package com.example.cepsacbackend.Mapper;

import com.example.cepsacbackend.Dto.DescuentoAplicacion.DescuentoAplicacionCreateDTO;
import com.example.cepsacbackend.Dto.DescuentoAplicacion.DescuentoAplicacionResponseDTO;
import com.example.cepsacbackend.Entity.DescuentoAplicacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DescuentoAplicacionMapper {

    @Mapping(target = "idDescuentoAplicacion", ignore = true)
    @Mapping(target = "descuento", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "cursoDiplomado", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    DescuentoAplicacion toEntity(DescuentoAplicacionCreateDTO dto);

    @Mapping(source = "descuento.idDescuento", target = "idDescuento")
    @Mapping(target = "infoDescuento", expression = "java(entity.getDescuento().getValor() + (entity.getDescuento().getTipoDescuento() == com.example.cepsacbackend.Enums.TipoDescuento.PORCENTAJE ? \"%\" : \" Monto Fijo\"))")
    @Mapping(source = "categoria.idCategoria", target = "idCategoria")
    @Mapping(source = "categoria.nombre", target = "nombreCategoria")
    @Mapping(source = "cursoDiplomado.idCursoDiplomado", target = "idCursoDiplomado")
    @Mapping(source = "cursoDiplomado.titulo", target = "tituloCursoDiplomado")
    @Mapping(source = "usuario.nombre", target = "nombreUsuario")
    DescuentoAplicacionResponseDTO toResponseDto(DescuentoAplicacion entity);

    List<DescuentoAplicacionResponseDTO> toResponseDtoList(List<DescuentoAplicacion> entities);
}