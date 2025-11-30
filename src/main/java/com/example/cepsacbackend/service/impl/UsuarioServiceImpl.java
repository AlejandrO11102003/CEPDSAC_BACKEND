package com.example.cepsacbackend.service.impl;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    protected final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    private Pais resolverPais(String nombrePais) {
        if (nombrePais == null || nombrePais.trim().isEmpty()) {
            return null;
        }
        return repopais.findByNombre(nombrePais.trim())
                .orElseThrow(() -> new BadRequestException(
                    String.format("El país '%s' no existe en el sistema. Verifique el nombre e intente nuevamente.", nombrePais)));
    }

    private TipoIdentificacion resolverTipoIdentificacion(Short idTipo) {
        if (idTipo == null) {
            return null;
        }
        return repotipo.findById(idTipo)
                .orElseThrow(() -> new BadRequestException(
                    String.format("El tipo de identificación con ID %d no existe. Por favor, seleccione un tipo válido.", idTipo)));
    }

    //una sola consulta con dto projection optimizada
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "usuarios", key = "'all'")
    public List<UsuarioListResponseDTO> listarUsuarios() {
        return usuarioRepository.findAllActivosAsDTO();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "usuarios", key = "#idUsuario")
    public UsuarioResponseDTO obtenerUsuario(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findByIdActivo(idUsuario)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("No se encontró el usuario con ID %d o ha sido suspendido.", idUsuario)));
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "usuarios", key = "'all'"),
        @CacheEvict(value = "usuarios", allEntries = true)
    })
    @CachePut(value = "usuarios", key = "#result.idUsuario")
    public UsuarioResponseDTO crearUsuario(UsuarioCreateDTO dto) {
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new ConflictException(
                String.format("El correo electrónico '%s' ya está registrado en el sistema. Por favor, use otro correo.", dto.getCorreo()));
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
    @Caching(evict = {
        @CacheEvict(value = "usuarios", key = "'all'"),
        @CacheEvict(value = "usuarios", key = "#idUsuario"),
        @CacheEvict(value = "usuarios", allEntries = true)
    })
    public UsuarioResponseDTO actualizarUsuario(Integer idUsuario, UsuarioUpdateDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findByIdActivo(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("No se puede actualizar. El usuario con ID %d no existe o está suspendido.", idUsuario)));
        if (dto.getCorreo() != null && !dto.getCorreo().equals(usuarioExistente.getCorreo())) {
            if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
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
    @Caching(evict = {
        @CacheEvict(value = "usuarios", key = "'all'"),
        @CacheEvict(value = "usuarios", key = "#idUsuario"),
        @CacheEvict(value = "usuarios", allEntries = true)
    })
    public UsuarioResponseDTO actualizarUsuarioParcialmente(Integer idUsuario, UsuarioPatchDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findByIdActivo(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + idUsuario));
        if (dto.getCorreo() != null && !dto.getCorreo().equals(usuarioExistente.getCorreo())) {
            if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
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
        @CacheEvict(value = "usuarios", key = "#idUsuario"),
        @CacheEvict(value = "usuarios", allEntries = true)
    })
    public void eliminarUsuario(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("No se puede eliminar. El usuario con ID %d no existe.", idUsuario)));
        usuario.setEstado(EstadoUsuario.SUSPENDIDO);
        usuarioRepository.save(usuario);
    }

    @Transactional
    @Override
    @Caching(evict = {
        @CacheEvict(value = "usuarios", key = "'all'"),
        @CacheEvict(value = "usuarios", key = "#idUsuario"),
        @CacheEvict(value = "usuarios", allEntries = true)
    })
    public UsuarioResponseDTO restaurarUsuario(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findByIdActivo(idUsuario)
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
        return usuarioRepository.findByRolActivoAsDTO(rol);
    }

    @Override
    @Transactional
    @CacheEvict(value = "usuarios", allEntries = true)
    public void cambiarPassword(Integer idUsuario, String nuevaPassword) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Usuario con ID %d no encontrado", idUsuario)));
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioListResponseDTO> listarAlumnosPaginado(String buscar, boolean soloConMatricula, Pageable pageable) {
        return usuarioRepository.findAlumnosConFiltros(buscar, soloConMatricula, pageable);
    }
}