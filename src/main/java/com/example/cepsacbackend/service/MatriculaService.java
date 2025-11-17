package com.example.cepsacbackend.service;

import com.example.cepsacbackend.dto.Matricula.AplicarDescuentoDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaCreateDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaDetalleResponseDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaListResponseDTO;
import com.example.cepsacbackend.model.Matricula;

import java.util.List;

public interface MatriculaService {

    Matricula crearMatricula(MatriculaCreateDTO dto);
    void notificarPago(Integer idMatricula);
    List<MatriculaListResponseDTO> listarMatriculas();
    List<MatriculaListResponseDTO> listarMatriculasPorAlumno(Integer idAlumno);
    Matricula cancelarMatricula(Integer idMatricula);
    MatriculaDetalleResponseDTO obtenerDetalle(Integer idMatricula);
    Matricula aplicarDescuentoAMatricula(Integer idMatricula, AplicarDescuentoDTO dto);

}
