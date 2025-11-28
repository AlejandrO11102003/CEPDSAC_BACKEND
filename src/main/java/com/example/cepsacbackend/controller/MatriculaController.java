package com.example.cepsacbackend.controller;

import com.example.cepsacbackend.dto.Matricula.AplicarDescuentoDTO;
import com.example.cepsacbackend.dto.Matricula.CancelacionProgramacionRequestDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaAdminListDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaCreateDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaDetalleResponseDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaListResponseDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaResponseDTO;
import com.example.cepsacbackend.enums.EstadoMatricula;
import com.example.cepsacbackend.mapper.MatriculaMapper;
import com.example.cepsacbackend.model.Matricula;
import com.example.cepsacbackend.service.MatriculaService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final MatriculaService matriculaService;
    private final MatriculaMapper matriculaMapper;

    @GetMapping("/alumno/{idAlumno}")
    public ResponseEntity<List<MatriculaListResponseDTO>> listarMatriculasPorAlumno(@PathVariable Integer idAlumno) {
        List<MatriculaListResponseDTO> matriculas = matriculaService.listarMatriculasPorAlumno(idAlumno);
        return ResponseEntity.ok(matriculas);
    }

    @GetMapping("/admin/listar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Page<MatriculaAdminListDTO>> listarMatriculasAdmin(
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) EstadoMatricula estado,
            @org.springframework.data.web.PageableDefault(size = 10, sort = "fechaMatricula", direction = org.springframework.data.domain.Sort.Direction.DESC) org.springframework.data.domain.Pageable pageable) {
        Page<MatriculaAdminListDTO> matriculas = matriculaService.listarMatriculasAdmin(dni, estado, pageable);
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

    @PutMapping("/{id}/confirmar-pago")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> confirmarPago(@PathVariable Integer id) {
        matriculaService.confirmarPagoMatricula(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancelar-por-programacion/{idProgramacionCurso}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<MatriculaResponseDTO>> cancelarMatriculasPorProgramacion(
            @PathVariable Integer idProgramacionCurso,
            @RequestBody @Valid CancelacionProgramacionRequestDTO dto) {
        List<com.example.cepsacbackend.model.Matricula> matriculasCanceladas = 
                matriculaService.cancelarMatriculasPorProgramacion(idProgramacionCurso, dto.getMotivo());
        List<MatriculaResponseDTO> response = matriculasCanceladas.stream()
                .map(matriculaMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(response);
    }
}
