package com.example.cepsacbackend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cepsacbackend.dto.Descuento.DescuentoCreateDTO;
import com.example.cepsacbackend.dto.Descuento.DescuentoResponseDTO;
import com.example.cepsacbackend.dto.Descuento.DescuentoUpdateDTO;
import com.example.cepsacbackend.service.DescuentoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/descuentos")
@Validated
@RequiredArgsConstructor
public class DescuentoController {

    private final DescuentoService descuentoService;

    @GetMapping("/listar")
    public List<DescuentoResponseDTO> listar() {
        return descuentoService.listar();
    }

    @GetMapping("/obtener/{id}")
    public ResponseEntity<DescuentoResponseDTO> obtener(@PathVariable("id") Short id) {
        return ResponseEntity.ok(descuentoService.obtener(id));
    }

    @PostMapping("/crear")
    public ResponseEntity<DescuentoResponseDTO> crear(@Valid @RequestBody DescuentoCreateDTO dto) {
        DescuentoResponseDTO creado = descuentoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<DescuentoResponseDTO> actualizar(@Valid @RequestBody DescuentoUpdateDTO dto) {
        DescuentoResponseDTO actualizado = descuentoService.actualizar(dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Short id) {
        descuentoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
