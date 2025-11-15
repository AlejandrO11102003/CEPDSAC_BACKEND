package com.example.cepsacbackend.mapper;

import com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoRequestDTO;
import com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoResponseDTO;
import com.example.cepsacbackend.model.ProgramacionCurso;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProgramacionCursoMapper {

    @Mapping(target = "idProgramacionCurso", ignore = true)
    @Mapping(target = "cursoDiplomado", ignore = true)
    @Mapping(target = "docente", ignore = true)
    ProgramacionCurso toEntity(ProgramacionCursoRequestDTO dto);

    @Mapping(source = "cursoDiplomado.idCursoDiplomado", target = "idCursoDiplomado")
    @Mapping(source = "cursoDiplomado.titulo", target = "nombreCursoDiplomado")
    @Mapping(source = "docente.idUsuario", target = "idDocente")
    @Mapping(expression = "java(entity.getDocente() != null ? entity.getDocente().getNombre() + \" \" + entity.getDocente().getApellido() : null)", target = "nombreDocente")
    ProgramacionCursoResponseDTO toResponseDTO(ProgramacionCurso entity);

    List<ProgramacionCursoResponseDTO> toResponseDTOList(List<ProgramacionCurso> entities);

    @Mapping(target = "idProgramacionCurso", ignore = true)
    @Mapping(target = "cursoDiplomado", ignore = true)
    @Mapping(target = "docente", ignore = true)
    void updateEntity(ProgramacionCursoRequestDTO dto, @MappingTarget ProgramacionCurso entity);
}
