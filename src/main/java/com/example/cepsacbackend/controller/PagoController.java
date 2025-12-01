package com.example.cepsacbackend.controller;

import com.example.cepsacbackend.dto.Pago.*;
import com.example.cepsacbackend.service.PagoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pagos")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class PagoController {

    private final PagoService pagoService;

    @PostMapping
    public ResponseEntity<PagoResponseDTO> registrar(@RequestBody @Valid PagoCreateDTO dto) {
        PagoResponseDTO response = pagoService.registrarPago(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/matricula/{idMatricula}")
    public ResponseEntity<List<PagoResponseDTO>> listarPagosPorMatricula(@PathVariable Integer idMatricula) {
        return ResponseEntity.ok(pagoService.listarPagosPorMatricula(idMatricula));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> actualizar(@PathVariable Integer id, @RequestBody @Valid PagoUpdateDTO dto) {
        PagoResponseDTO response = pagoService.actualizarPago(id, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/por-devolver")
    public ResponseEntity<List<PagoResponseDTO>> listarPagosPorDevolver() {
        return ResponseEntity.ok(pagoService.listarPagosPorDevolver());
    }

    @PostMapping("/{id}/devolver")
    public ResponseEntity<Void> marcarComoDevuelto(@PathVariable Integer id) {
        pagoService.marcarComoDevuelto(id);
        return ResponseEntity.noContent().build();
    }
}
