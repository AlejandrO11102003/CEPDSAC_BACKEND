package com.example.cepsacbackend.controller;

import com.example.cepsacbackend.dto.Matricula.AplicarDescuentoDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaCreateDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaDetalleResponseDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaListResponseDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaResponseDTO;
import com.example.cepsacbackend.mapper.MatriculaMapper;
import com.example.cepsacbackend.model.Matricula;
import com.example.cepsacbackend.service.MatriculaService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final MatriculaService matriculaService;
    private final MatriculaMapper matriculaMapper;

    @GetMapping("/listar")
    public ResponseEntity<List<MatriculaListResponseDTO>> listarMatriculas() {
        List<MatriculaListResponseDTO> matriculas = matriculaService.listarMatriculas();
        return ResponseEntity.ok(matriculas);
    }

    @GetMapping("/alumno/{idAlumno}")
    public ResponseEntity<List<MatriculaListResponseDTO>> listarMatriculasPorAlumno(@PathVariable Integer idAlumno) {
        List<MatriculaListResponseDTO> matriculas = matriculaService.listarMatriculasPorAlumno(idAlumno);
        return ResponseEntity.ok(matriculas);
    }

    @PostMapping
    public ResponseEntity<MatriculaResponseDTO> crear(@RequestBody @Valid MatriculaCreateDTO dto) {
        Matricula matricula = matriculaService.crearMatricula(dto);
        MatriculaResponseDTO response = matriculaMapper.toResponseDTO(matricula);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/notificar-pago")
    public ResponseEntity<Void> notificarPago(@PathVariable Integer id) {
        matriculaService.notificarPago(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<MatriculaResponseDTO> cancelar(@PathVariable Integer id) {
        Matricula matricula = matriculaService.cancelarMatricula(id);
        return ResponseEntity.ok(matriculaMapper.toResponseDTO(matricula));
    }

    @GetMapping("/{id}/detalle")
    public ResponseEntity<MatriculaDetalleResponseDTO> obtenerDetalle(@PathVariable Integer id) {
        MatriculaDetalleResponseDTO detalle = matriculaService.obtenerDetalle(id);
        return ResponseEntity.ok(detalle);
    }

    @PutMapping("/{id}/aplicar-descuento")
    public ResponseEntity<MatriculaResponseDTO> aplicarDescuento(
            @PathVariable Integer id,
            @RequestBody @Valid AplicarDescuentoDTO dto) {
        Matricula matricula = matriculaService.aplicarDescuentoAMatricula(id, dto);
        return ResponseEntity.ok(matriculaMapper.toResponseDTO(matricula));
    }
}
