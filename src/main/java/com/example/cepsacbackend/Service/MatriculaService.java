package com.example.cepsacbackend.service;

import com.example.cepsacbackend.dto.Matricula.MatriculaCreateDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaDetalleResponseDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaResponseDTO;
import com.example.cepsacbackend.model.Matricula;

import java.util.List;

public interface MatriculaService {

    Matricula crearMatricula(MatriculaCreateDTO dto);
    Matricula aprobarMatricula(Integer idMatricula);
    List<MatriculaResponseDTO> listarMatriculas();
    Matricula cancelarMatricula(Integer idMatricula);
    MatriculaDetalleResponseDTO obtenerDetalle(Integer idMatricula);

}
