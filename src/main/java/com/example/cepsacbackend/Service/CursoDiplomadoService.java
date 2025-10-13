package com.example.cepsacbackend.Service;

import com.example.cepsacbackend.Dto.CursoDiplomado.CursoDiplomadoCreateDTO;
import com.example.cepsacbackend.Dto.CursoDiplomado.CursoDiplomadoResponseDTO;

import java.util.List;

public interface CursoDiplomadoService {
    List<CursoDiplomadoResponseDTO> listar();
    CursoDiplomadoResponseDTO obtenerPorId(Short id);
    CursoDiplomadoResponseDTO crear(CursoDiplomadoCreateDTO dto);
    void eliminar(Short id);
}