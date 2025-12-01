package com.example.cepsacbackend.service;

import java.util.List;

import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoCreateDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoResponseDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoUpdateDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoDetalleResponseDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoIndexResponseDTO;

public interface CursoDiplomadoService {
    List<CursoDiplomadoResponseDTO> listar();
    CursoDiplomadoResponseDTO obtenerPorId(Short id);
    CursoDetalleResponseDTO obtenerDetallePorId(Short id);
    CursoDiplomadoResponseDTO crear(CursoDiplomadoCreateDTO dto);
    CursoDiplomadoResponseDTO actualizar(Short id, CursoDiplomadoUpdateDTO dto);
    void eliminar(Short id);
    List<CursoIndexResponseDTO> listarIndex();
    List<CursoIndexResponseDTO> listarCursos();
    List<CursoIndexResponseDTO> listarDiplomados();
    List<CursoDiplomadoResponseDTO> listarCursosAdmin();
    List<CursoDiplomadoResponseDTO> listarDiplomadosAdmin();
}