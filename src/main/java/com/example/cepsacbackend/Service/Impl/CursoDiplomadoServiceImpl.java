package com.example.cepsacbackend.service.impl;

import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoCreateDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoResponseDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoUpdateDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoIndexResponseDTO;
import com.example.cepsacbackend.exception.ResourceNotFoundException;
import com.example.cepsacbackend.mapper.CursoDiplomadoMapper;
import com.example.cepsacbackend.model.Categoria;
import com.example.cepsacbackend.model.CursoDiplomado;
import com.example.cepsacbackend.repository.CategoriaRepository;
import com.example.cepsacbackend.repository.CursoDiplomadoRepository;
import com.example.cepsacbackend.service.CursoDiplomadoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoDiplomadoServiceImpl implements CursoDiplomadoService {

    private final CursoDiplomadoRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final CursoDiplomadoMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CursoDiplomadoResponseDTO> listar() {
        return mapper.toResponseDtoList(repository.findAllWithCategoria());
    }

    @Override
    @Transactional(readOnly = true)
    public CursoDiplomadoResponseDTO obtenerPorId(Short id) {
        CursoDiplomado entity = repository.findByIdWithCategoria(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("No se encontró el curso/diplomado con ID %d. Verifique que el ID sea correcto.", id)));
        return mapper.toResponseDto(entity);
    }

    @Override
    @Transactional
    public CursoDiplomadoResponseDTO crear(CursoDiplomadoCreateDTO dto) {
        CursoDiplomado entity = mapper.toEntity(dto);
        if (dto.getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("La categoría con ID %d no existe. Seleccione una categoría válida.", dto.getIdCategoria())));
            entity.setCategoria(categoria);
        }
        CursoDiplomado savedEntity = repository.save(entity);
        return mapper.toResponseDto(savedEntity);
    }

    @Override
    @Transactional
    public CursoDiplomadoResponseDTO actualizar(Short idCursoDiplomado, CursoDiplomadoUpdateDTO dto) {
        CursoDiplomado entity = repository.findById(idCursoDiplomado)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("No se puede actualizar. El curso/diplomado con ID %d no existe.", idCursoDiplomado)));

        mapper.updateEntityFromDto(dto, entity);
        if (dto.getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("La categoría con ID %d no existe.", dto.getIdCategoria())));
            entity.setCategoria(categoria);
        }

        CursoDiplomado updatedEntity = repository.save(entity);
        return mapper.toResponseDto(updatedEntity);
    }

    @Override
    @Transactional
    public void eliminar(Short id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                String.format("No se puede eliminar. El curso/diplomado con ID %d no existe.", id));
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoIndexResponseDTO> listarIndex() {
        return mapper.toIndexResponseDtoList(repository.findAllWithCategoria());
    }
}