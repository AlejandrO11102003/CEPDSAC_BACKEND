package com.example.cepsacbackend.mapper;

import org.mapstruct.Mapper;
import com.example.cepsacbackend.dto.Descuento.DescuentoResponseDTO;
import com.example.cepsacbackend.model.Descuento;

@Mapper(componentModel = "spring")
public interface DescuentoMapper {
    
    DescuentoResponseDTO toResponseDTO(Descuento entity);
    Descuento toEntity(DescuentoResponseDTO dto);
}
