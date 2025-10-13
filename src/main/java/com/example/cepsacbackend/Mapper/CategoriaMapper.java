package com.example.cepsacbackend.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.cepsacbackend.Dto.Categoria.CategoriaCreateDTO;
import com.example.cepsacbackend.Dto.Categoria.CategoriaResponseDTO;
import com.example.cepsacbackend.Entity.Categoria;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    @Mapping(target = "idCategoria", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    Categoria toEntity(CategoriaCreateDTO dto);

    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    CategoriaResponseDTO toResponseDto(Categoria entity);

    List<CategoriaResponseDTO> toResponseDtoList(List<Categoria> entities);
}
