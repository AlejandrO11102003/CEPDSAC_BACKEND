package com.example.cepsacbackend.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.cepsacbackend.Dto.CursoDiplomado.CursoDiplomadoCreateDTO;
import com.example.cepsacbackend.Dto.CursoDiplomado.CursoDiplomadoResponseDTO;
import com.example.cepsacbackend.Entity.CursoDiplomado;

@Mapper(componentModel = "spring")
public interface CursoDiplomadoMapper {

    @Mapping(target = "idCursoDiplomado", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    CursoDiplomado toEntity(CursoDiplomadoCreateDTO dto);

    @Mapping(source = "categoria.idCategoria", target = "idCategoria")
    @Mapping(source = "categoria.nombre", target = "nombreCategoria")
    @Mapping(target = "tipo", expression = "java(entity.getTipo() != null && entity.getTipo() ? \"DIPLOMADO\" : \"CURSO\")")
    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    @Mapping(source = "usuario.nombre", target = "nombreUsuario")
    CursoDiplomadoResponseDTO toResponseDto(CursoDiplomado entity);

    List<CursoDiplomadoResponseDTO> toResponseDtoList(List<CursoDiplomado> entities);
}
