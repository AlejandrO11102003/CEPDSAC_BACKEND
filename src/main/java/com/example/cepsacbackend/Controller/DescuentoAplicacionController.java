package com.example.cepsacbackend.controller;

import com.example.cepsacbackend.dto.DescuentoAplicacion.DescuentoAplicacionCreateDTO;
import com.example.cepsacbackend.dto.DescuentoAplicacion.DescuentoAplicacionResponseDTO;
import com.example.cepsacbackend.service.DescuentoAplicacionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aplicaciondescuento")
@RequiredArgsConstructor
public class DescuentoAplicacionController {

    private final DescuentoAplicacionService descuentoAplicacionService;

    @GetMapping("/listar")
    public ResponseEntity<List<DescuentoAplicacionResponseDTO>> listar() {
        return ResponseEntity.ok(descuentoAplicacionService.listar());
    }

    @GetMapping("/obtener/{id}")
    public ResponseEntity<DescuentoAplicacionResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(descuentoAplicacionService.obtenerPorId(id));
    }

    @PostMapping("/crear")
    public ResponseEntity<DescuentoAplicacionResponseDTO> crear(@Valid @RequestBody DescuentoAplicacionCreateDTO dto) {
        return new ResponseEntity<>(descuentoAplicacionService.crear(dto), HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<DescuentoAplicacionResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody DescuentoAplicacionCreateDTO dto) {
        return ResponseEntity.ok(descuentoAplicacionService.actualizar(id, dto));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        descuentoAplicacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}