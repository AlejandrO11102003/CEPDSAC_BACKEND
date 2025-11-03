package com.example.cepsacbackend.service.impl;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cepsacbackend.dto.Usuario.UsuarioCreateDTO;
import com.example.cepsacbackend.dto.Usuario.UsuarioListResponseDTO;
import com.example.cepsacbackend.dto.Usuario.UsuarioPatchDTO;
import com.example.cepsacbackend.dto.Usuario.UsuarioResponseDTO;
import com.example.cepsacbackend.dto.Usuario.UsuarioUpdateDTO;
import com.example.cepsacbackend.exception.BadRequestException;
import com.example.cepsacbackend.exception.ConflictException;
import com.example.cepsacbackend.exception.ResourceNotFoundException;
import com.example.cepsacbackend.mapper.UsuarioMapper;
import com.example.cepsacbackend.model.Pais;
import com.example.cepsacbackend.model.TipoIdentificacion;
import com.example.cepsacbackend.model.Usuario;
import com.example.cepsacbackend.repository.PaisRepository;
import com.example.cepsacbackend.repository.TipoIdentificacionRepository;
import com.example.cepsacbackend.repository.UsuarioRepository;
import com.example.cepsacbackend.service.UsuarioService;
import com.example.cepsacbackend.enums.EstadoUsuario;
import com.example.cepsacbackend.enums.Rol;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PaisRepository repopais;
    private final TipoIdentificacionRepository repotipo;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    private Pais resolverPais(String nombrePais) {
        if (nombrePais == null || nombrePais.trim().isEmpty()) {
            return null;
        }
        return repopais.findByNombre(nombrePais)
                .orElseThrow(() -> new BadRequestException("País no encontrado: " + nombrePais));
    }

    private TipoIdentificacion resolverTipoIdentificacion(Short idTipo) {
        if (idTipo == null) {
            return null;
        }
        return repotipo.findById(idTipo)
                .orElseThrow(() -> new BadRequestException("Tipo de Identificación no encontrado id=" + idTipo));
    }

    //una sola consulta con join fetch
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "usuarios", key = "'all'")
    public List<UsuarioListResponseDTO> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAllActivos();
        return usuarioMapper.toListResponseDTOList(usuarios);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "usuarios", key = "#idUsuario")
    public UsuarioResponseDTO obtenerUsuario(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findByIdActivo(idUsuario)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + idUsuario));
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Override
    @Transactional
    @CacheEvict(value = "usuarios", key = "'all'")
    @CachePut(value = "usuarios", key = "#result.idUsuario")
    public UsuarioResponseDTO crearUsuario(UsuarioCreateDTO dto) {
        if (usuarioRepository.findByCorreo(dto.getCorreo()).isPresent()) {
            throw new ConflictException("Ya existe un usuario con el correo: " + dto.getCorreo());
        }
        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setPais(resolverPais(dto.getNombrePais()));
        usuario.setTipoIdentificacion(resolverTipoIdentificacion(dto.getIdTipoIdentificacion()));
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(nuevoUsuario);
    }

    @Override
    @Transactional
    @CacheEvict(value = "usuarios", key = "'all'")
    @CachePut(value = "usuarios", key = "#idUsuario")
    public UsuarioResponseDTO actualizarUsuario(Integer idUsuario, UsuarioUpdateDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + idUsuario));
        if (dto.getCorreo() != null && !dto.getCorreo().equals(usuarioExistente.getCorreo())) {
            if (usuarioRepository.findByCorreo(dto.getCorreo()).isPresent()) {
                throw new ConflictException("Ya existe otro usuario con el correo: " + dto.getCorreo());
            }
        }
        usuarioMapper.updateEntityFromUpdateDTO(dto, usuarioExistente);
        usuarioExistente.setPais(resolverPais(dto.getNombrePais()));
        usuarioExistente.setTipoIdentificacion(resolverTipoIdentificacion(dto.getIdTipoIdentificacion()));
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuarioExistente.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);
        return usuarioMapper.toResponseDTO(usuarioActualizado);
    }

    @Override
    @Transactional
    @CacheEvict(value = "usuarios", key = "'all'")
    @CachePut(value = "usuarios", key = "#idUsuario")
    public UsuarioResponseDTO actualizarUsuarioParcialmente(Integer idUsuario, UsuarioPatchDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + idUsuario));
        if (dto.getCorreo() != null && !dto.getCorreo().equals(usuarioExistente.getCorreo())) {
            if (usuarioRepository.findByCorreo(dto.getCorreo()).isPresent()) {
                throw new ConflictException("Ya existe otro usuario con el correo: " + dto.getCorreo());
            }
        }
        usuarioMapper.updateEntityFromPatchDTO(dto, usuarioExistente);
        if (dto.getNombrePais() != null) {
            usuarioExistente.setPais(resolverPais(dto.getNombrePais()));
        }
        if (dto.getIdTipoIdentificacion() != null) {
            usuarioExistente.setTipoIdentificacion(resolverTipoIdentificacion(dto.getIdTipoIdentificacion()));
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuarioExistente.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);
        return usuarioMapper.toResponseDTO(usuarioActualizado);
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "usuarios", key = "'all'"),
        @CacheEvict(value = "usuarios", key = "#idUsuario")
    })
    public void eliminarUsuario(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findByIdActivo(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + idUsuario)); // Usar ResourceNotFoundException
        usuario.setEstado(EstadoUsuario.SUSPENDIDO);
        usuarioRepository.save(usuario);
    }

    @Transactional
    @Override
    @Caching(evict = {
        @CacheEvict(value = "usuarios", key = "'all'"),
        @CacheEvict(value = "usuarios", key = "#idUsuario")
    })
    public UsuarioResponseDTO restaurarUsuario(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + idUsuario));
        if (usuario.getEstado() != EstadoUsuario.SUSPENDIDO) {
            throw new BadRequestException("El usuario no está suspendido/eliminado");
        }
        usuario.setEstado(EstadoUsuario.ACTIVO);
        Usuario usuarioRestaurado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioRestaurado);
    }

    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = "usuarios", key = "'rol_' + #rol.name()")
    public List<UsuarioListResponseDTO> listarUsuariosPorRol(Rol rol) {
        List<Usuario> usuarios = usuarioRepository.findByRolActivo(rol);
        return usuarioMapper.toListResponseDTOList(usuarios);
    }
}