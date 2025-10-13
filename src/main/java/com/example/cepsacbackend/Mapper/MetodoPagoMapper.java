package com.example.cepsacbackend.Mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.cepsacbackend.Dto.MetodoPago.MetodoPagoRequestDTO;
import com.example.cepsacbackend.Dto.MetodoPago.MetodoPagoResponseDTO;
import com.example.cepsacbackend.Entity.MetodoPago;
@Mapper(componentModel = "spring")
public interface MetodoPagoMapper {

    // RequestDTO a Entity post
    @Mapping(target = "idMetodoPago", ignore = true)
    @Mapping(target = "activo", constant = "true")
    MetodoPago toEntity(MetodoPagoRequestDTO dto);

    // actualizar entidad existente desde RequestDTO put
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idMetodoPago", ignore = true)
    @Mapping(target = "activo", ignore = true)
    void updateEntityFromRequestDTO(MetodoPagoRequestDTO dto, @MappingTarget MetodoPago entity);

    // Entity a ResponseDTO
    MetodoPagoResponseDTO toResponseDTO(MetodoPago entity);

    // Lista de entidades a Lista de ResponseDTOs
    List<MetodoPagoResponseDTO> toResponseDTOList(List<MetodoPago> entities);
}