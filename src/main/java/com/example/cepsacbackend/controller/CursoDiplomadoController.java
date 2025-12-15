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

import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoCreateDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoResponseDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoUpdateDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoDetalleResponseDTO;
import com.example.cepsacbackend.dto.CursoDiplomado.CursoIndexResponseDTO;
import com.example.cepsacbackend.service.CursoDiplomadoService;
import com.example.cepsacbackend.config.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cursos-diplomados")
@RequiredArgsConstructor
public class CursoDiplomadoController {

    private final CursoDiplomadoService cursoDiplomadoService;

    @GetMapping
    public ResponseEntity<List<CursoDiplomadoResponseDTO>> listar() {
        return ResponseEntity.ok(cursoDiplomadoService.listar());
    }

    @GetMapping("/index")
    public ResponseEntity<List<CursoIndexResponseDTO>> listarIndex() { 
        return ResponseEntity.ok(cursoDiplomadoService.listarIndex());
    }

    @GetMapping("/cursos")
    public ResponseEntity<List<CursoIndexResponseDTO>> listarCursos() {
        return ResponseEntity.ok(cursoDiplomadoService.listarCursos());
    }

    @GetMapping("/diplomados")
    public ResponseEntity<List<CursoIndexResponseDTO>> listarDiplomados() {
        return ResponseEntity.ok(cursoDiplomadoService.listarDiplomados());
    }

    @GetMapping("/admin/cursos")
    public ResponseEntity<List<CursoDiplomadoResponseDTO>> listarCursosAdmin() {
        return ResponseEntity.ok(cursoDiplomadoService.listarCursosAdmin());
    }

    @GetMapping("/admin/diplomados")
    public ResponseEntity<List<CursoDiplomadoResponseDTO>> listarDiplomadosAdmin() {
        return ResponseEntity.ok(cursoDiplomadoService.listarDiplomadosAdmin());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoDiplomadoResponseDTO> obtenerPorId(@PathVariable Short id) {
        return ResponseEntity.ok(cursoDiplomadoService.obtenerPorId(id));
    }

    @GetMapping("/{id}/detalle")
    public ResponseEntity<CursoDetalleResponseDTO> obtenerDetalle(@PathVariable Short id) {
        return ResponseEntity.ok(cursoDiplomadoService.obtenerDetallePorId(id));
    }

    @PostMapping
    public ResponseEntity<CursoDiplomadoResponseDTO> crear(@Valid @RequestBody CursoDiplomadoCreateDTO dto) {
        return new ResponseEntity<>(cursoDiplomadoService.crear(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoDiplomadoResponseDTO> actualizar(@PathVariable Short id, @Valid @RequestBody CursoDiplomadoUpdateDTO dto) {
        return ResponseEntity.ok(cursoDiplomadoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Short id) {
        cursoDiplomadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/docente/cursos")
    public ResponseEntity<List<CursoIndexResponseDTO>> listarCursosDocente(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(cursoDiplomadoService.listarCursosDocente(userDetails.getIdUsuario()));
    }

    @GetMapping("/docente/diplomados")
    public ResponseEntity<List<CursoIndexResponseDTO>> listarDiplomadosDocente(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(cursoDiplomadoService.listarDiplomadosDocente(userDetails.getIdUsuario()));
    }
}
