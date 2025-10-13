package com.example.cepsacbackend.Controller;

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

import com.example.cepsacbackend.Dto.ProgramacionCurso.ProgramacionCursoRequestDTO;
import com.example.cepsacbackend.Dto.ProgramacionCurso.ProgramacionCursoResponseDTO;
import com.example.cepsacbackend.Service.ProgramacionCursoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/programacioncursos")
public class ProgramacionCursoController {

    private final ProgramacionCursoService programacionCursoService;

    @GetMapping
    public ResponseEntity<List<ProgramacionCursoResponseDTO>> getAllProgramacionCursos() {
        return ResponseEntity.ok(programacionCursoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramacionCursoResponseDTO> getProgramacionCursoById(@PathVariable int id) {
        return ResponseEntity.ok(programacionCursoService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ProgramacionCursoResponseDTO> createProgramacionCurso(@Valid @RequestBody ProgramacionCursoRequestDTO programacionCursoRequestDTO) {
        return new ResponseEntity<>(programacionCursoService.create(programacionCursoRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgramacionCursoResponseDTO> updateProgramacionCurso(@PathVariable int id, @Valid @RequestBody ProgramacionCursoRequestDTO programacionCursoRequestDTO) {
        return ResponseEntity.ok(programacionCursoService.update(id, programacionCursoRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgramacionCurso(@PathVariable int id) {
        programacionCursoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}