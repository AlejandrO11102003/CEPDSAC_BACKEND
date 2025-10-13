package com.example.cepsacbackend.Service;

import com.example.cepsacbackend.Dto.Matricula.MatriculaCreateDTO;
import com.example.cepsacbackend.Entity.Matricula;
import com.example.cepsacbackend.Dto.Matricula.MatriculaResponseDTO;
import com.example.cepsacbackend.Dto.Matricula.MatriculaDetalleResponseDTO;

import java.util.List;

public interface MatriculaService {

    Matricula crearMatricula(MatriculaCreateDTO dto);
    Matricula aprobarMatricula(Integer idMatricula);
    List<MatriculaResponseDTO> listarMatriculas();
    Matricula cancelarMatricula(Integer idMatricula);
    MatriculaDetalleResponseDTO obtenerDetalle(Integer idMatricula);

}
