package com.example.cepsacbackend.controller;

import com.example.cepsacbackend.model.Pais;
import com.example.cepsacbackend.service.PaisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/paises")
public class PaisController {

    private final PaisService paisService;

    @GetMapping
    public ResponseEntity<List<Pais>> listarPaises() {
        List<Pais> paises = paisService.getAllPaises();
        return ResponseEntity.ok(paises);
    }

    @GetMapping("/{idPais}")
    public ResponseEntity<Pais> obtenerPais(@PathVariable Short idPais) {
        Pais pais = paisService.getPaisById(idPais);
        return ResponseEntity.ok(pais);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Pais> crearPais(@Valid @RequestBody Pais pais) {
        Pais nuevoPais = paisService.createPais(pais);
        return new ResponseEntity<>(nuevoPais, HttpStatus.CREATED);
    }

    @PutMapping("/{idPais}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Pais> actualizarPais(@PathVariable Short idPais, @Valid @RequestBody Pais pais) {
        Pais paisActualizado = paisService.updatePais(idPais, pais);
        return ResponseEntity.ok(paisActualizado);
    }

    @DeleteMapping("/{idPais}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarPais(@PathVariable Short idPais) {
        paisService.deletePais(idPais);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content, estándar para eliminaciones exitosas
    }
}