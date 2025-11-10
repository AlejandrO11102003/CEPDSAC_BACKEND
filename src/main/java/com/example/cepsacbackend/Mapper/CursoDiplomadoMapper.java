package com.example.cepsacbackend.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoCreateDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoResponseDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoIndexResponseDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoUpdateDTO;
import com.example.cepsacbackend.model.CursoDiplomado;

@Mapper(componentModel = "spring")
public interface CursoDiplomadoMapper {

    @Mapping(target = "idCursoDiplomado", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    CursoDiplomado toEntity(CursoDiplomadoCreateDTO dto);

    @Mapping(target = "idCursoDiplomado", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    void updateEntityFromDto(CursoDiplomadoUpdateDTO dto, @MappingTarget CursoDiplomado entity);

    @Mapping(source = "categoria.idCategoria", target = "idCategoria")
    @Mapping(source = "categoria.nombre", target = "nombreCategoria")
    @Mapping(source = "tipo", target = "tipo")
    CursoDiplomadoResponseDTO toResponseDto(CursoDiplomado entity);
    List<CursoIndexResponseDTO> toIndexResponseDtoList(List<CursoDiplomado> entities);
    List<CursoDiplomadoResponseDTO> toResponseDtoList(List<CursoDiplomado> entities);

    //metodo para mapear el tipo de curso a string
    default String mapTipoToString(Boolean tipo) {
        if (tipo != null && tipo) {
            return "DIPLOMADO";
        }
        return "CURSO";
    }
}
