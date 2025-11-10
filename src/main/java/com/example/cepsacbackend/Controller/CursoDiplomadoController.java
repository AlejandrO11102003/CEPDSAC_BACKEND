package com.example.cepsacbackend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoCreateDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoResponseDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoIndexResponseDTO;
import com.example.cepsacbackend.service.CursoDiplomadoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cursos-diplomados")
@RequiredArgsConstructor
public class CursoDiplomadoController {

    private final CursoDiplomadoService cursoDiplomadoService;

    @GetMapping("/listar")
    public ResponseEntity<List<CursoDiplomadoResponseDTO>> listar() {
        return ResponseEntity.ok(cursoDiplomadoService.listar());
    }

    @GetMapping("/listar-index")
    public ResponseEntity<List<CursoIndexResponseDTO>> listarIndex() {
        return ResponseEntity.ok(cursoDiplomadoService.listarIndex());
    }

    @GetMapping("/obtener/{id}")
    public ResponseEntity<CursoDiplomadoResponseDTO> obtenerPorId(@PathVariable Short id) {
        return ResponseEntity.ok(cursoDiplomadoService.obtenerPorId(id));
    }

    @PostMapping("/crear")
    public ResponseEntity<CursoDiplomadoResponseDTO> crear(@Valid @RequestBody CursoDiplomadoCreateDTO dto) {
        return new ResponseEntity<>(cursoDiplomadoService.crear(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Short id) {
        cursoDiplomadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
