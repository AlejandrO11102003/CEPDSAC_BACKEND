package com.example.cepsacbackend.mapper;

import com.example.cepsacbackend.dto.TipoIdentificacion.TipoIdentificacionDTO;
import com.example.cepsacbackend.model.TipoIdentificacion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoIdentificacionMapper {
    TipoIdentificacionDTO toDto(TipoIdentificacion entity);
    TipoIdentificacion toEntity(TipoIdentificacionDTO dto);
}
