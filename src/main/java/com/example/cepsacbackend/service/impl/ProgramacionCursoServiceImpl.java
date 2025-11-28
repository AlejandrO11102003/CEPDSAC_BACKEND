package com.example.cepsacbackend.service.impl;

import com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoRequestDTO;
import com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoResponseDTO;
import com.example.cepsacbackend.exception.ResourceNotFoundException;
import com.example.cepsacbackend.mapper.ProgramacionCursoMapper;
import com.example.cepsacbackend.model.CursoDiplomado;
import com.example.cepsacbackend.model.ProgramacionCurso;
import com.example.cepsacbackend.model.Usuario;
import com.example.cepsacbackend.repository.CursoDiplomadoRepository;
import com.example.cepsacbackend.repository.ProgramacionCursoRepository;
import com.example.cepsacbackend.repository.UsuarioRepository;
import com.example.cepsacbackend.repository.MatriculaRepository;
import com.example.cepsacbackend.service.ProgramacionCursoService;
import com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoListResponseDTO;

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
    private final UsuarioRepository usuarioRepository;
    private final MatriculaRepository matriculaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProgramacionCursoListResponseDTO> getAll() {
        List<ProgramacionCursoListResponseDTO> list = programacionCursoRepository.findAllAsDTO();
        list.forEach(dto -> dto.setCantidadInscritos(
            matriculaRepository.countByProgramacionCursoIdProgramacionCurso(dto.getIdProgramacionCurso())
        ));
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramacionCursoListResponseDTO> getDisponibles() {
        List<ProgramacionCursoListResponseDTO> list = programacionCursoRepository.findAllAvailableAsDTO(LocalDate.now());
        list.forEach(dto -> dto.setCantidadInscritos(
            matriculaRepository.countByProgramacionCursoIdProgramacionCurso(dto.getIdProgramacionCurso())
        ));
        return list;
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

        // validar existencia del curso sin cargarlo completo
        if (!cursoDiplomadoRepository.existsById(programacionCursoRequestDTO.getIdCursoDiplomado())) {
            throw new ResourceNotFoundException(
                String.format("El curso/diplomado con ID %d no existe. Seleccione un curso válido.",
                    programacionCursoRequestDTO.getIdCursoDiplomado()));
        }

        // usar getReferenceById
        CursoDiplomado cursoDiplomado = cursoDiplomadoRepository.getReferenceById(
            programacionCursoRequestDTO.getIdCursoDiplomado());
        programacionCurso.setCursoDiplomado(cursoDiplomado);

        // asignar docente si se proporciona
        if (programacionCursoRequestDTO.getIdDocente() != null) {
            if (!usuarioRepository.existsById(programacionCursoRequestDTO.getIdDocente())) {
                throw new ResourceNotFoundException(
                    String.format("El usuario/docente con ID %d no existe.",
                        programacionCursoRequestDTO.getIdDocente()));
            }
            Usuario docente = usuarioRepository.getReferenceById(programacionCursoRequestDTO.getIdDocente());
            programacionCurso.setDocente(docente);
        }

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

        // actualizar docente si se proporciona
        if (programacionCursoRequestDTO.getIdDocente() != null) {
            if (!usuarioRepository.existsById(programacionCursoRequestDTO.getIdDocente())) {
                throw new ResourceNotFoundException(
                    String.format("El usuario/docente con ID %d no existe.",
                        programacionCursoRequestDTO.getIdDocente()));
            }
            Usuario docente = usuarioRepository.getReferenceById(programacionCursoRequestDTO.getIdDocente());
            programacionCurso.setDocente(docente);
        } else {
            programacionCurso.setDocente(null);
        }

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
