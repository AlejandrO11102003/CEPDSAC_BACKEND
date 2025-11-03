package com.example.cepsacbackend.service.impl;

import com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoRequestDTO;
import com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoResponseDTO;
import com.example.cepsacbackend.mapper.ProgramacionCursoMapper;
import com.example.cepsacbackend.model.CursoDiplomado;
import com.example.cepsacbackend.model.ProgramacionCurso;
import com.example.cepsacbackend.model.Usuario;
import com.example.cepsacbackend.repository.CursoDiplomadoRepository;
import com.example.cepsacbackend.repository.ProgramacionCursoRepository;
import com.example.cepsacbackend.repository.UsuarioRepository;
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
    private final UsuarioRepository usuarioRepository;
    private final CursoDiplomadoRepository cursoDiplomadoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProgramacionCursoResponseDTO> getAll() {
        List<ProgramacionCurso> programaciones = programacionCursoRepository.findAll();
        return programacionCursoMapper.toResponseDTOList(programaciones);
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
                .orElseThrow(() -> new RuntimeException("ProgramacionCurso no encontrado"));
        return programacionCursoMapper.toResponseDTO(programacionCurso);
    }

    @Override
    @Transactional
    public ProgramacionCursoResponseDTO create(ProgramacionCursoRequestDTO programacionCursoRequestDTO) {
        ProgramacionCurso programacionCurso = programacionCursoMapper.toEntity(programacionCursoRequestDTO);
        Usuario usuario = usuarioRepository.findById(programacionCursoRequestDTO.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        CursoDiplomado cursoDiplomado = cursoDiplomadoRepository.findById(programacionCursoRequestDTO.getIdCursoDiplomado())
                .orElseThrow(() -> new RuntimeException("CursoDiplomado no encontrado"));
        programacionCurso.setUsuario(usuario);
        programacionCurso.setCursoDiplomado(cursoDiplomado);

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
