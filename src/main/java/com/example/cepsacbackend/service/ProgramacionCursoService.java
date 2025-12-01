package com.example.cepsacbackend.service;

import java.util.List;

import com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoRequestDTO;
import com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoResponseDTO;
import com.example.cepsacbackend.dto.ProgramacionCurso.ProgramacionCursoListResponseDTO;

public interface ProgramacionCursoService {
    List<ProgramacionCursoListResponseDTO> getAll();
    ProgramacionCursoResponseDTO getById(int id);
    ProgramacionCursoResponseDTO create(ProgramacionCursoRequestDTO programacionCursoRequestDTO);
    ProgramacionCursoResponseDTO update(int id, ProgramacionCursoRequestDTO programacionCursoRequestDTO);
    void delete(int id);
    List<ProgramacionCursoListResponseDTO> getDisponibles();
}