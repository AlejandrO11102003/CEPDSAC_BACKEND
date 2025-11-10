package com.example.cepsacbackend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cepsacbackend.dto.DescuentoAplicacion.DescuentoAplicacionCreateDTO;
import com.example.cepsacbackend.dto.DescuentoAplicacion.DescuentoAplicacionResponseDTO;
import com.example.cepsacbackend.exception.ResourceNotFoundException;
import com.example.cepsacbackend.mapper.DescuentoAplicacionMapper;
import com.example.cepsacbackend.model.Categoria;
import com.example.cepsacbackend.model.CursoDiplomado;
import com.example.cepsacbackend.model.Descuento;
import com.example.cepsacbackend.model.DescuentoAplicacion;
import com.example.cepsacbackend.model.Matricula;
import com.example.cepsacbackend.repository.CategoriaRepository;
import com.example.cepsacbackend.repository.CursoDiplomadoRepository;
import com.example.cepsacbackend.repository.DescuentoAplicacionRepository;
import com.example.cepsacbackend.repository.DescuentoRepository;
import com.example.cepsacbackend.repository.MatriculaRepository;
import com.example.cepsacbackend.service.DescuentoAplicacionService;
import com.example.cepsacbackend.enums.TipoAplicacionDescuento;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DescuentoAplicacionServiceImpl implements DescuentoAplicacionService {

    private final DescuentoAplicacionRepository repository;
    private final DescuentoRepository descuentoRepository;
    private final CategoriaRepository categoriaRepository;
    private final CursoDiplomadoRepository cursoDiplomadoRepository;
    private final MatriculaRepository matriculaRepository;
    private final DescuentoAplicacionMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<DescuentoAplicacionResponseDTO> listar() {
        return mapper.toResponseDtoList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public DescuentoAplicacionResponseDTO obtenerPorId(Integer id) {
        DescuentoAplicacion entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("No se encontró la regla de aplicación de descuento con ID %d.", id)));
        return mapper.toResponseDto(entity);
    }

    @Override
    @Transactional
    public DescuentoAplicacionResponseDTO crear(DescuentoAplicacionCreateDTO dto) {
        DescuentoAplicacion entity = mapper.toEntity(dto);

        Descuento descuento = descuentoRepository.findById(dto.getIdDescuento())
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("El descuento con ID %d no existe. Seleccione un descuento válido.", dto.getIdDescuento())));
        entity.setDescuento(descuento);

        if (dto.getTipoAplicacion() == TipoAplicacionDescuento.CATEGORIA) {
            Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("La categoría con ID %d no existe.", dto.getIdCategoria())));
            entity.setCategoria(categoria);
        } else if (dto.getTipoAplicacion() == TipoAplicacionDescuento.CURSO) {
            CursoDiplomado curso = cursoDiplomadoRepository.findById(dto.getIdCursoDiplomado())
                    .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("El curso/diplomado con ID %d no existe.", dto.getIdCursoDiplomado())));
            entity.setCursoDiplomado(curso);
        }

        if (dto.getIdMatricula() != null) {
            Matricula matricula = matriculaRepository.findById(dto.getIdMatricula())
                    .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("La matrícula con ID %d no existe.", dto.getIdMatricula())));
            entity.setMatricula(matricula);
        }

        DescuentoAplicacion guardado = repository.save(entity);
        return mapper.toResponseDto(guardado);
    }

    @Override
    @Transactional
    public DescuentoAplicacionResponseDTO actualizar(Integer id, DescuentoAplicacionCreateDTO dto) {
        DescuentoAplicacion entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("No se puede actualizar. La regla de aplicación de descuento con ID %d no existe.", id)));

        mapper.updateEntityFromDto(dto, entity);
        Descuento descuento = descuentoRepository.findById(dto.getIdDescuento())
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("El descuento con ID %d no existe.", dto.getIdDescuento())));
        entity.setDescuento(descuento);
        entity.setCategoria(null);
        entity.setCursoDiplomado(null);

        if (dto.getTipoAplicacion() == TipoAplicacionDescuento.CATEGORIA) {
            Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("La categoría con ID %d no existe.", dto.getIdCategoria())));
            entity.setCategoria(categoria);
        } else if (dto.getTipoAplicacion() == TipoAplicacionDescuento.CURSO) {
            CursoDiplomado curso = cursoDiplomadoRepository.findById(dto.getIdCursoDiplomado())
                    .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("El curso/diplomado con ID %d no existe.", dto.getIdCursoDiplomado())));
            entity.setCursoDiplomado(curso);
        }

        if (dto.getIdMatricula() != null) {
            Matricula matricula = matriculaRepository.findById(dto.getIdMatricula())
                    .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("La matrícula con ID %d no existe.", dto.getIdMatricula())));
            entity.setMatricula(matricula);
        } else {
            entity.setMatricula(null);
        }

        DescuentoAplicacion actualizado = repository.save(entity);
        return mapper.toResponseDto(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                String.format("No se puede eliminar. La regla de aplicación de descuento con ID %d no existe.", id));
        }
        repository.deleteById(id);
    }
}