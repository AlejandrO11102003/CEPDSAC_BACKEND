package com.example.cepsacbackend.Mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.cepsacbackend.Dto.Usuario.UsuarioCreateDTO;
import com.example.cepsacbackend.Dto.Usuario.UsuarioListResponseDTO;
import com.example.cepsacbackend.Dto.Usuario.UsuarioPatchDTO;
import com.example.cepsacbackend.Dto.Usuario.UsuarioResponseDTO;
import com.example.cepsacbackend.Dto.Usuario.UsuarioUpdateDTO;
import com.example.cepsacbackend.Entity.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    // mapeo de createDTO a entidad Usuario
    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "pais", ignore = true)
    @Mapping(target = "tipoIdentificacion", ignore = true)
    Usuario toEntity(UsuarioCreateDTO dto);

    // para actualizar con PUT
    @Mapping(target = "pais", ignore = true)
    @Mapping(target = "tipoIdentificacion", ignore = true)
    void updateEntityFromUpdateDTO(UsuarioUpdateDTO dto, @MappingTarget Usuario entity);

    // para actualizar con PATCH
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "pais", ignore = true)
    @Mapping(target = "tipoIdentificacion", ignore = true)
    void updateEntityFromPatchDTO(UsuarioPatchDTO dto, @MappingTarget Usuario entity);

    // mapeo de entidad a responseDTO
    @Mapping(source = "pais.idPais", target = "idPais")
    @Mapping(source = "pais.nombre", target = "nombrePais")
    @Mapping(source = "pais.codigotelefono", target = "codigoTelefono")
    @Mapping(source = "tipoIdentificacion.idTipoIdentificacion", target = "idTipoIdentificacion")
    @Mapping(source = "tipoIdentificacion.nombre", target = "nombreTipoIdentificacion")
    @Mapping(target = "activo", expression = "java(usuario.getEstado() == com.example.cepsacbackend.Enums.EstadoUsuario.ACTIVO)")
    UsuarioResponseDTO toResponseDTO(Usuario usuario);

    List<UsuarioResponseDTO> toResponseDTOList(List<Usuario> usuarios);

    // mapeo de entidad a listResponseDTO
    @Mapping(target = "activo", expression = "java(usuario.getEstado() == com.example.cepsacbackend.Enums.EstadoUsuario.ACTIVO)")
    UsuarioListResponseDTO toListResponseDTO(Usuario usuario);

    List<UsuarioListResponseDTO> toListResponseDTOList(List<Usuario> usuarios);
}