package com.example.cepsacbackend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cepsacbackend.dto.Categoria.CategoriaCreateDTO;
import com.example.cepsacbackend.dto.Categoria.CategoriaResponseDTO;
import com.example.cepsacbackend.service.CategoriaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final ObjectMapper objectMapper;
    private final CategoriaService categoriaService;

    @GetMapping("/listar")
    public ResponseEntity<List<CategoriaResponseDTO>> listar() {
        return ResponseEntity.ok(categoriaService.findAll());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<CategoriaResponseDTO>> listarActivas() {
        List<CategoriaResponseDTO> list = categoriaService.findByEstadoTrue();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/obtener/{id}")
    public ResponseEntity<CategoriaResponseDTO> obtenerPorId(@PathVariable Short id) {
        return ResponseEntity.ok(categoriaService.obtenerPorId(id));
    }

    @PostMapping("/crear")
    public ResponseEntity<CategoriaResponseDTO> crear(@Valid @RequestBody CategoriaCreateDTO dto) {
        return new ResponseEntity<>(categoriaService.crear(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Short id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/cambiar-estado/{id}")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Short id) {
        categoriaService.cambiarEstado(id);
        return ResponseEntity.ok().build();
    }
}
