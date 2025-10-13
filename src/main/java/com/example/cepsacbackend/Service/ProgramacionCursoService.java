package com.example.cepsacbackend.Service;

import com.example.cepsacbackend.Dto.ProgramacionCurso.ProgramacionCursoRequestDTO;
import com.example.cepsacbackend.Dto.ProgramacionCurso.ProgramacionCursoResponseDTO;

import java.util.List;

public interface ProgramacionCursoService {
    List<ProgramacionCursoResponseDTO> getAll();
    ProgramacionCursoResponseDTO getById(int id);
    ProgramacionCursoResponseDTO create(ProgramacionCursoRequestDTO programacionCursoRequestDTO);
    ProgramacionCursoResponseDTO update(int id, ProgramacionCursoRequestDTO programacionCursoRequestDTO);
    void delete(int id);
}