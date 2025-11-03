package com.example.cepsacbackend.service.impl;

import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoCreateDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoResponseDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoIndexResponseDTO;
import com.example.cepsacbackend.mapper.CursoDiplomadoMapper;
import com.example.cepsacbackend.model.Categoria;
import com.example.cepsacbackend.model.CursoDiplomado;
import com.example.cepsacbackend.model.Usuario;
import com.example.cepsacbackend.repository.CategoriaRepository;
import com.example.cepsacbackend.repository.CursoDiplomadoRepository;
import com.example.cepsacbackend.repository.UsuarioRepository;
import com.example.cepsacbackend.service.CursoDiplomadoService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoDiplomadoServiceImpl implements CursoDiplomadoService {

    private final CursoDiplomadoRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoDiplomadoMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CursoDiplomadoResponseDTO> listar() {
        return mapper.toResponseDtoList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public CursoDiplomadoResponseDTO obtenerPorId(Short id) {
        CursoDiplomado entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso/Diplomado no encontrado: " + id));
        return mapper.toResponseDto(entity);
    }

    @Override
    @Transactional
    public CursoDiplomadoResponseDTO crear(CursoDiplomadoCreateDTO dto) {
        //obtenemos correo del token
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario del token no encontrado"));
        CursoDiplomado entity = mapper.toEntity(dto);
        // mapeamos user del jwt
        entity.setUsuario(usuario);

        if (dto.getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + dto.getIdCategoria()));
            entity.setCategoria(categoria);
        }

        return mapper.toResponseDto(repository.save(entity));
    }

    @Override
    @Transactional
    public void eliminar(Short id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Curso/Diplomado no encontrado: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoIndexResponseDTO> listarIndex() {
        return mapper.toIndexResponseDtoList(repository.findAll());
    }
}