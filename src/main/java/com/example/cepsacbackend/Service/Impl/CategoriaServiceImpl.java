package com.example.cepsacbackend.service.impl;

import com.example.cepsacbackend.dto.Categoria.CategoriaCreateDTO;
import com.example.cepsacbackend.dto.Categoria.CategoriaResponseDTO;
import com.example.cepsacbackend.mapper.CategoriaMapper;
import com.example.cepsacbackend.model.Categoria;
import com.example.cepsacbackend.model.Usuario;
import com.example.cepsacbackend.repository.CategoriaRepository;
import com.example.cepsacbackend.repository.UsuarioRepository;
import com.example.cepsacbackend.service.CategoriaService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listar() {
        return mapper.toResponseDtoList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponseDTO obtenerPorId(Short id) {
        Categoria entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + id));
        return mapper.toResponseDto(entity);
    }

    @Override
    @Transactional
    public CategoriaResponseDTO crear(CategoriaCreateDTO dto) {
        Categoria entity = mapper.toEntity(dto);

        if (dto.getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + dto.getIdUsuario()));
            entity.setUsuario(usuario);
        }

        Categoria guardado = repository.save(entity);
        return mapper.toResponseDto(guardado);
    }

    @Override
    @Transactional
    public void eliminar(Short id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada: " + id);
        }
        repository.deleteById(id);
    }
}
