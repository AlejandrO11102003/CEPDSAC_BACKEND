package com.example.cepsacbackend.controller;

import com.example.cepsacbackend.dto.Matricula.MatriculaApprovalDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaCreateDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaDetalleResponseDTO;
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
    public List<MatriculaResponseDTO> listarMatriculas() {
        return matriculaService.listarMatriculas();
    }

    @PostMapping
    public ResponseEntity<MatriculaResponseDTO> crear(@RequestBody @Valid MatriculaCreateDTO dto) {
        Matricula m = matriculaService.crearMatricula(dto);
        return new ResponseEntity<>(matriculaMapper.toResponseDTO(m), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<MatriculaResponseDTO> aprobar(@PathVariable Integer id, @RequestBody @Valid MatriculaApprovalDTO dto) {
        Matricula m = matriculaService.aprobarMatricula(id);
        return ResponseEntity.ok(matriculaMapper.toResponseDTO(m));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<MatriculaResponseDTO> cancelar(@PathVariable Integer id) {
        Matricula m = matriculaService.cancelarMatricula(id);
        return ResponseEntity.ok(matriculaMapper.toResponseDTO(m));
    }

    @GetMapping("/{id}/detalle")
    public ResponseEntity<MatriculaDetalleResponseDTO> obtenerDetalle(@PathVariable Integer id) {
        MatriculaDetalleResponseDTO detalle = matriculaService.obtenerDetalle(id);
        return ResponseEntity.ok(detalle);
    }

}
