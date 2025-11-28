package com.example.cepsacbackend.service;

import com.example.cepsacbackend.dto.Matricula.AplicarDescuentoDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaAdminListDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaCreateDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaDetalleResponseDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaListResponseDTO;
import com.example.cepsacbackend.model.Matricula;

import com.example.cepsacbackend.enums.EstadoMatricula;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MatriculaService {

    Matricula crearMatricula(MatriculaCreateDTO dto);
    void notificarPago(Integer idMatricula);
    Matricula confirmarPagoMatricula(Integer idMatricula);
    List<MatriculaListResponseDTO> listarMatriculas();
    List<MatriculaListResponseDTO> listarMatriculasPorAlumno(Integer idAlumno);
    Matricula cancelarMatricula(Integer idMatricula);
    MatriculaDetalleResponseDTO obtenerDetalle(Integer idMatricula);
    Matricula aplicarDescuentoAMatricula(Integer idMatricula, AplicarDescuentoDTO dto);
    Page<MatriculaAdminListDTO> listarMatriculasAdmin(String dni, EstadoMatricula estado, Pageable pageable);
    List<Matricula> cancelarMatriculasPorProgramacion(Integer idProgramacionCurso, String motivo);

}
