package com.example.cepsacbackend.service.impl;

import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoCreateDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoResponseDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoUpdateDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoDetalleResponseDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoIndexResponseDTO;
import com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoSimpleDTO;
import com.example.cepsacbackend.exception.ResourceNotFoundException;
import com.example.cepsacbackend.mapper.CursoDiplomadoMapper;
import com.example.cepsacbackend.model.Categoria;
import com.example.cepsacbackend.model.CursoDiplomado;
import com.example.cepsacbackend.repository.CategoriaRepository;
import com.example.cepsacbackend.repository.CursoDiplomadoRepository;
import com.example.cepsacbackend.repository.ProgramacionCursoRepository;
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
    private final ProgramacionCursoRepository programacionRepository;
    private final CursoDiplomadoMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CursoDiplomadoResponseDTO> listar() {
        return mapper.toResponseDtoList(repository.findAllWithCategoria());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoDiplomadoResponseDTO> listarDiplomadosAdmin() {
        return repository.findAllDiplomadosAdmin();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoDiplomadoResponseDTO> listarCursosAdmin() {
        return repository.findAllCursosAdmin();
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
    @Transactional(readOnly = true)
    public CursoDetalleResponseDTO obtenerDetallePorId(Short id) {
        CursoDiplomado entity = repository.findByIdWithCategoriaForDetalle(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("No se encontró el curso/diplomado con ID %d. Verifique que el ID sea correcto.", id)));

        // obtener programaciones disponibles
        List<ProgramacionCursoSimpleDTO> programaciones = programacionRepository.findAvailableByCursoId(id);

        // construir DTO de detalle
        CursoDetalleResponseDTO detalle = new CursoDetalleResponseDTO();
        detalle.setIdCursoDiplomado(entity.getIdCursoDiplomado());
        detalle.setTitulo(entity.getTitulo());
        detalle.setUrlCurso(entity.getUrlCurso());
        detalle.setObjetivo(entity.getObjetivo());
        detalle.setMaterialesIncluidos(entity.getMaterialesIncluidos());
        detalle.setRequisitos(entity.getRequisitos());
        detalle.setTipo(entity.getTipo());
        detalle.setOtorgaCertificado(entity.getOtorgaCertificado());

        if (entity.getCategoria() != null) {
            detalle.setIdCategoria(entity.getCategoria().getIdCategoria());
            detalle.setNombreCategoria(entity.getCategoria().getNombre());
        }

        detalle.setProgramaciones(programaciones);

        return detalle;
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
        // devuelve solo cursos con programaciones disponibles DTO PROJECTION
        return repository.findAllWithAvailableProgramacionDTO();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoIndexResponseDTO> listarCursos() {
        // solo CURSOS (tipo=false) con programaciones disponibles
        return repository.findCursosWithAvailableProgramacionDTO();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoIndexResponseDTO> listarDiplomados() {
        // solo DIPLOMADOS (tipo=true) con programaciones disponibles
        return repository.findDiplomadosWithAvailableProgramacionDTO();
    }
}