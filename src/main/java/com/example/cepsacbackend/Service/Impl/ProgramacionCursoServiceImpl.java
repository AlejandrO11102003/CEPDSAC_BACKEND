package com.example.cepsacbackend.Service.Impl;

import com.example.cepsacbackend.Dto.ProgramacionCurso.ProgramacionCursoRequestDTO;
import com.example.cepsacbackend.Dto.ProgramacionCurso.ProgramacionCursoResponseDTO;
import com.example.cepsacbackend.Entity.ProgramacionCurso;
import com.example.cepsacbackend.Mapper.ProgramacionCursoMapper;
import com.example.cepsacbackend.Repository.ProgramacionCursoRepository;
import com.example.cepsacbackend.Service.ProgramacionCursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramacionCursoServiceImpl implements ProgramacionCursoService {

    private final ProgramacionCursoRepository programacionCursoRepository;
    private final ProgramacionCursoMapper programacionCursoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProgramacionCursoResponseDTO> getAll() {
        List<ProgramacionCurso> programaciones = programacionCursoRepository.findAll();
        return programacionCursoMapper.toResponseDTOList(programaciones);
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramacionCursoResponseDTO getById(int id) {
        ProgramacionCurso programacionCurso = programacionCursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProgramacionCurso no encontrado"));
        return programacionCursoMapper.toResponseDTO(programacionCurso);
    }

    @Override
    @Transactional
    public ProgramacionCursoResponseDTO create(ProgramacionCursoRequestDTO programacionCursoRequestDTO) {
        ProgramacionCurso programacionCurso = programacionCursoMapper.toEntity(programacionCursoRequestDTO);
        ProgramacionCurso nuevaProgramacion = programacionCursoRepository.save(programacionCurso);
        return programacionCursoMapper.toResponseDTO(nuevaProgramacion);
    }

    @Override
    @Transactional
    public ProgramacionCursoResponseDTO update(int id, ProgramacionCursoRequestDTO programacionCursoRequestDTO) {
        ProgramacionCurso programacionCurso = programacionCursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProgramacionCurso no encontrado"));

        programacionCursoMapper.updateEntity(programacionCursoRequestDTO, programacionCurso);
        ProgramacionCurso programacionActualizada = programacionCursoRepository.save(programacionCurso);
        return programacionCursoMapper.toResponseDTO(programacionActualizada);
    }

    @Override
    @Transactional
    public void delete(int id) {
        if (!programacionCursoRepository.existsById(id)) {
            throw new RuntimeException("ProgramacionCurso no encontrado");
        }
        programacionCursoRepository.deleteById(id);
    }
}
