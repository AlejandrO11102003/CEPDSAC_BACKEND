package com.example.cepsacbackend.Mapper;

import com.example.cepsacbackend.Dto.ProgramacionCurso.ProgramacionCursoRequestDTO;
import com.example.cepsacbackend.Dto.ProgramacionCurso.ProgramacionCursoResponseDTO;
import com.example.cepsacbackend.Entity.ProgramacionCurso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProgramacionCursoMapper {

    @Mapping(target = "idProgramacionCurso", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "cursoDiplomado", ignore = true)
    ProgramacionCurso toEntity(ProgramacionCursoRequestDTO dto);

    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    @Mapping(source = "usuario.nombre", target = "nombreUsuario")
    @Mapping(source = "cursoDiplomado.idCursoDiplomado", target = "idCursoDiplomado")
    @Mapping(source = "cursoDiplomado.titulo", target = "nombreCursoDiplomado")
    ProgramacionCursoResponseDTO toResponseDTO(ProgramacionCurso entity);

    List<ProgramacionCursoResponseDTO> toResponseDTOList(List<ProgramacionCurso> entities);

    @Mapping(target = "idProgramacionCurso", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "cursoDiplomado", ignore = true)
    void updateEntity(ProgramacionCursoRequestDTO dto, @MappingTarget ProgramacionCurso entity);
}
