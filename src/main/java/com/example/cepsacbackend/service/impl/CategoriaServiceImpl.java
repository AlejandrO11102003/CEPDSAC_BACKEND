package com.example.cepsacbackend.service.impl;

import com.example.cepsacbackend.dto.Categoria.CategoriaCreateDTO;
import com.example.cepsacbackend.dto.Categoria.CategoriaResponseDTO;
import com.example.cepsacbackend.exception.ResourceNotFoundException;
import com.example.cepsacbackend.mapper.CategoriaMapper;
import com.example.cepsacbackend.model.Categoria;
import com.example.cepsacbackend.repository.CategoriaRepository;
import com.example.cepsacbackend.service.CategoriaService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository repository;
    private final CategoriaMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> findByEstadoTrue() {
        return mapper.toResponseDtoList(repository.findByEstadoTrue());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponseDTO obtenerPorId(@NonNull Short id) {
        Categoria entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("No se encontró la categoría con ID %d. Verifique que el ID sea correcto.", id)));
        return mapper.toResponseDto(entity);
    }

    @Override
    @Transactional
    public CategoriaResponseDTO crear( CategoriaCreateDTO dto) {
        Categoria entity = mapper.toEntity(dto);
        Categoria guardado = repository.save(entity);
        return mapper.toResponseDto(guardado);
    }

    @Override
    @Transactional
    public void eliminar(@NonNull Short id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                String.format("No se puede eliminar. La categoría con ID %d no existe.", id));
        }
        repository.deleteById(id);
    }
}
