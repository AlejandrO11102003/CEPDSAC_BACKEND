package com.example.cepsacbackend.Service.Impl;

import com.example.cepsacbackend.Dto.Usuario.UsuarioCreateDTO;
import com.example.cepsacbackend.Dto.Usuario.UsuarioListResponseDTO;
import com.example.cepsacbackend.Dto.Usuario.UsuarioPatchDTO;
import com.example.cepsacbackend.Dto.Usuario.UsuarioResponseDTO;
import com.example.cepsacbackend.Dto.Usuario.UsuarioUpdateDTO;
import com.example.cepsacbackend.Entity.Pais;
import com.example.cepsacbackend.Entity.TipoIdentificacion;
import com.example.cepsacbackend.Entity.Usuario;
import com.example.cepsacbackend.Enums.EstadoUsuario;
import com.example.cepsacbackend.Enums.Rol;
import com.example.cepsacbackend.Mapper.UsuarioMapper;
import com.example.cepsacbackend.Repository.PaisRepository;
import com.example.cepsacbackend.Repository.TipoIdentificacionRepository;
import com.example.cepsacbackend.Repository.UsuarioRepository;
import com.example.cepsacbackend.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                .orElseThrow(() -> new RuntimeException("País no encontrado: " + nombrePais));
    }

    private TipoIdentificacion resolverTipoIdentificacion(Short idTipo) {
        if (idTipo == null) {
            return null;
        }
        return repotipo.findById(idTipo)
                .orElseThrow(() -> new RuntimeException("Tipo de Identificación no encontrado id=" + idTipo));
    }

    //una sola consulta con join fetch
    @Override
    @Transactional(readOnly = true)
    @Cacheable("usuarios")
    public List<UsuarioListResponseDTO> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAllActivos();
        return usuarioMapper.toListResponseDTOList(usuarios);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerUsuario(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findByIdActivo(idUsuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Override
    @Transactional
    @CacheEvict(value = "usuarios", allEntries = true)
    public UsuarioResponseDTO crearUsuario(UsuarioCreateDTO dto) {
        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setPais(resolverPais(dto.getNombrePais()));
        usuario.setTipoIdentificacion(resolverTipoIdentificacion(dto.getIdTipoIdentificacion()));
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(nuevoUsuario);
    }

    @Override
    @Transactional
    @CacheEvict(value = "usuarios", allEntries = true)
    public UsuarioResponseDTO actualizarUsuario(UsuarioUpdateDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getIdUsuario()));
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
    @CacheEvict(value = "usuarios", allEntries = true)
    public UsuarioResponseDTO actualizarUsuarioParcialmente(UsuarioPatchDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getIdUsuario()));
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
    @CacheEvict(value = "usuarios", allEntries = true)
    public void eliminarUsuario(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findByIdActivo(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
        usuario.setEstado(EstadoUsuario.SUSPENDIDO);
        usuarioRepository.save(usuario);
    }

    //metodo para restaurar usuario suspendido
    @Transactional
    @Override
    @CacheEvict(value = "usuarios", allEntries = true)
    public UsuarioResponseDTO restaurarUsuario(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
        if (usuario.getEstado() != EstadoUsuario.SUSPENDIDO) {
            throw new RuntimeException("El usuario no está suspendido/eliminado");
        }
        usuario.setEstado(EstadoUsuario.ACTIVO);
        Usuario usuarioRestaurado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioRestaurado);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UsuarioListResponseDTO> listarUsuariosPorRol(Rol rol) {
        List<Usuario> usuarios = usuarioRepository.findByRolActivo(rol);
        return usuarioMapper.toListResponseDTOList(usuarios);
    }
}