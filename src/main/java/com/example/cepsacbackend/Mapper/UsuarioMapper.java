package com.example.cepsacbackend.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.cepsacbackend.dto.Usuario.UsuarioCreateDTO;
import com.example.cepsacbackend.dto.Usuario.UsuarioListResponseDTO;
import com.example.cepsacbackend.dto.Usuario.UsuarioPatchDTO;
import com.example.cepsacbackend.dto.Usuario.UsuarioResponseDTO;
import com.example.cepsacbackend.dto.Usuario.UsuarioUpdateDTO;
import com.example.cepsacbackend.model.Usuario;


@Mapper(componentModel = "spring", uses = {TipoIdentificacionMapper.class})
public interface UsuarioMapper {

    // mapeo de createDTO a entidad Usuario
    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "pais", ignore = true)
    @Mapping(target = "tipoIdentificacion", ignore = true)
    Usuario toEntity(UsuarioCreateDTO dto);

    // para actualizar con PUT
    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "pais", ignore = true)
    @Mapping(target = "tipoIdentificacion", ignore = true)
    void updateEntityFromUpdateDTO(UsuarioUpdateDTO dto, @MappingTarget Usuario entity);

    // para actualizar con PATCH
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "pais", ignore = true)
    @Mapping(target = "tipoIdentificacion", ignore = true)
    void updateEntityFromPatchDTO(UsuarioPatchDTO dto, @MappingTarget Usuario entity);

    // mapeo de entidad a responseDTO
    @Mapping(source = "pais.nombre", target = "nombrePais")
    @Mapping(source = "tipoIdentificacion.idTipoIdentificacion", target = "idTipoIdentificacion")
    @Mapping(source = "tipoIdentificacion.iniciales", target = "inicialesTipoIdentificacion")
    @Mapping(source = "pais.idPais", target = "idPais")
    @Mapping(source = "pais.codigoTelefono", target = "codigoTelefono")
    @Mapping(target = "activo", expression = "java(usuario.getEstado() == com.example.cepsacbackend.enums.EstadoUsuario.ACTIVO)")
    UsuarioResponseDTO toResponseDTO(Usuario usuario);

    List<UsuarioResponseDTO> toResponseDTOList(List<Usuario> usuarios);

    // mapeo de entidad a listResponseDTO
    @Mapping(target = "activo", expression = "java(usuario.getEstado() == com.example.cepsacbackend.enums.EstadoUsuario.ACTIVO)")
    @Mapping(source = "numeroIdentificacion", target = "numeroIdentificacion")
    @Mapping(source = "tipoIdentificacion.iniciales", target = "inicialesTipoIdentificacion")
    UsuarioListResponseDTO toListResponseDTO(Usuario usuario);

    List<UsuarioListResponseDTO> toListResponseDTOList(List<Usuario> usuarios);
}