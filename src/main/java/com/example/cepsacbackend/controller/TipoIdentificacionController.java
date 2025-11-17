package com.example.cepsacbackend.controller;

import com.example.cepsacbackend.model.TipoIdentificacion;
import com.example.cepsacbackend.repository.TipoIdentificacionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tipos-identificacion")
public class TipoIdentificacionController {

    private final TipoIdentificacionRepository tipoRepository;

    @GetMapping
    public ResponseEntity<List<TipoIdentificacion>> listar() {
        return ResponseEntity.ok(tipoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoIdentificacion> obtenerPorId(@NonNull @PathVariable Short id) {
        return tipoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TipoIdentificacion> crear(@RequestBody TipoIdentificacion tipoIdentificacion) {
        tipoIdentificacion.setIdTipoIdentificacion(null);
        TipoIdentificacion nuevo = tipoRepository.save(tipoIdentificacion);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoIdentificacion> actualizar(@NonNull @PathVariable Short id, @RequestBody TipoIdentificacion tipoIdentificacion) {
        if (!tipoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        tipoIdentificacion.setIdTipoIdentificacion(id);
        return ResponseEntity.ok(tipoRepository.save(tipoIdentificacion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@NonNull @PathVariable Short id) {
        if (!tipoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        tipoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

        @GetMapping("/iniciales")
        public ResponseEntity<List<Map<String, Object>>> listarIniciales() {
        List<Map<String, Object>> iniciales = tipoRepository.findAll()
            .stream()
            .map(t -> {
                Map<String, Object> m = new HashMap<>();
                m.put("idTipoIdentificacion", t.getIdTipoIdentificacion());
                m.put("iniciales", t.getIniciales());
                return m;
            })
            .collect(Collectors.toList());
        return ResponseEntity.ok(iniciales);
        }
}