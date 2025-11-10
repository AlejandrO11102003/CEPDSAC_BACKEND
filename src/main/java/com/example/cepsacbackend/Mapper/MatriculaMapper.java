package com.example.cepsacbackend.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.cepsacbackend.dto.Matricula.MatriculaCreateDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaDetalleResponseDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaResponseDTO;
import com.example.cepsacbackend.model.Matricula;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class, CursoDiplomadoMapper.class})
public interface MatriculaMapper {

    @Mapping(target = "idMatricula", ignore = true)
    @Mapping(target = "programacionCurso", ignore = true)
    @Mapping(target = "alumno", ignore = true)
    @Mapping(target = "administradorAprobador", ignore = true)
    @Mapping(target = "fechaMatricula", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "montoBase", ignore = true)
    @Mapping(target = "montoDescontado", ignore = true)
    @Mapping(target = "monto", ignore = true)
    @Mapping(target = "descuento", ignore = true)
    @Mapping(target = "usuarioModificador", ignore = true)
    @Mapping(target = "fechaModificacion", ignore = true)
    Matricula toEntity(MatriculaCreateDTO dto);

    @Mapping(source = "programacionCurso.idProgramacionCurso", target = "idProgramacionCurso")
    @Mapping(source = "alumno.idUsuario", target = "idAlumno")
    MatriculaResponseDTO toResponseDTO(Matricula entity);

    @Mapping(source = "programacionCurso.cursoDiplomado", target = "cursoDiplomado")
    @Mapping(target = "pagos", ignore = true)
    MatriculaDetalleResponseDTO toDetalleResponseDTO(Matricula entity);

    List<MatriculaResponseDTO> toResponseDTOList(List<Matricula> entities);
}
