package com.example.cepsacbackend.Controller;

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

import com.example.cepsacbackend.Dto.CursoDiplomado.CursoDiplomadoCreateDTO;
import com.example.cepsacbackend.Dto.CursoDiplomado.CursoDiplomadoResponseDTO;
import com.example.cepsacbackend.Service.CursoDiplomadoService;

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

    @GetMapping("/obtener/{id}")
    public ResponseEntity<CursoDiplomadoResponseDTO> obtenerPorId(@PathVariable Short id) {
        try {
            return ResponseEntity.ok(cursoDiplomadoService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<CursoDiplomadoResponseDTO> crear(@Valid @RequestBody CursoDiplomadoCreateDTO dto) {
        return new ResponseEntity<>(cursoDiplomadoService.crear(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Short id) {
        try {
            cursoDiplomadoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
