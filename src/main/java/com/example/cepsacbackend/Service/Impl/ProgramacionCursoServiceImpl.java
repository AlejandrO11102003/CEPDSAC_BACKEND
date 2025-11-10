package com.example.cepsacbackend.service.impl;

import com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoRequestDTO;
import com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoResponseDTO;
import com.example.cepsacbackend.exception.ResourceNotFoundException;
import com.example.cepsacbackend.mapper.ProgramacionCursoMapper;
import com.example.cepsacbackend.model.CursoDiplomado;
import com.example.cepsacbackend.model.ProgramacionCurso;
import com.example.cepsacbackend.repository.CursoDiplomadoRepository;
import com.example.cepsacbackend.repository.ProgramacionCursoRepository;
import com.example.cepsacbackend.service.ProgramacionCursoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramacionCursoServiceImpl implements ProgramacionCursoService {

    private final ProgramacionCursoRepository programacionCursoRepository;
    private final ProgramacionCursoMapper programacionCursoMapper;
    private final CursoDiplomadoRepository cursoDiplomadoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProgramacionCursoResponseDTO> getAll() {
        return programacionCursoRepository.findAllAsDTO();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramacionCursoResponseDTO> getDisponibles() {
        List<ProgramacionCurso> programaciones = programacionCursoRepository.findAllAvailable(LocalDate.now());
        return programacionCursoMapper.toResponseDTOList(programaciones);
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramacionCursoResponseDTO getById(int id) {
        ProgramacionCurso programacionCurso = programacionCursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("No se encontró la programación de curso con ID %d. Verifique que el ID sea correcto.", id)));
        return programacionCursoMapper.toResponseDTO(programacionCurso);
    }

    @Override
    @Transactional
    public ProgramacionCursoResponseDTO create(ProgramacionCursoRequestDTO programacionCursoRequestDTO) {
        ProgramacionCurso programacionCurso = programacionCursoMapper.toEntity(programacionCursoRequestDTO);
        CursoDiplomado cursoDiplomado = cursoDiplomadoRepository.findById(programacionCursoRequestDTO.getIdCursoDiplomado())
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("El curso/diplomado con ID %d no existe. Seleccione un curso válido.", programacionCursoRequestDTO.getIdCursoDiplomado())));
        programacionCurso.setCursoDiplomado(cursoDiplomado);
        ProgramacionCurso nuevaProgramacion = programacionCursoRepository.save(programacionCurso);
        return programacionCursoMapper.toResponseDTO(nuevaProgramacion);
    }

    @Override
    @Transactional
    public ProgramacionCursoResponseDTO update(int id, ProgramacionCursoRequestDTO programacionCursoRequestDTO) {
        ProgramacionCurso programacionCurso = programacionCursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("No se puede actualizar. La programación de curso con ID %d no existe.", id)));

        programacionCursoMapper.updateEntity(programacionCursoRequestDTO, programacionCurso);
        ProgramacionCurso programacionActualizada = programacionCursoRepository.save(programacionCurso);
        return programacionCursoMapper.toResponseDTO(programacionActualizada);
    }

    @Override
    @Transactional
    public void delete(int id) {
        if (!programacionCursoRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                String.format("No se puede eliminar. La programación de curso con ID %d no existe.", id));
        }
        programacionCursoRepository.deleteById(id);
    }
}
