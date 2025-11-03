package com.example.cepsacbackend.service;

import java.util.List;

import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoCreateDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoResponseDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoIndexResponseDTO;

public interface CursoDiplomadoService {
    List<CursoDiplomadoResponseDTO> listar();
    CursoDiplomadoResponseDTO obtenerPorId(Short id);
    CursoDiplomadoResponseDTO crear(CursoDiplomadoCreateDTO dto);
    void eliminar(Short id);
    List<CursoIndexResponseDTO> listarIndex();
}