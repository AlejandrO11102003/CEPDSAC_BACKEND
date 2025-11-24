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

import com.example.cepsacbackend.model.Testimonio;
import com.example.cepsacbackend.service.TestimonioService;

@RestController
@RequestMapping("/api/testimonios")
public class TestimonioController {

    private final TestimonioService testimonioService;

    public TestimonioController(TestimonioService testimonioService) {
        this.testimonioService = testimonioService;
    }

    @GetMapping
    public ResponseEntity<List<Testimonio>> getAll() {
        return ResponseEntity.ok(testimonioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Testimonio> getById(@PathVariable Integer id) {
        return testimonioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Testimonio> create(@RequestBody Testimonio testimonio) {
        Testimonio created = testimonioService.create(testimonio);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Testimonio> update(@PathVariable Integer id, @RequestBody Testimonio testimonio) {
        try {
            Testimonio updated = testimonioService.update(id, testimonio);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        testimonioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    public static class AprobacionRequest {
        public Integer idAprobador;
        public Boolean estadoAprobado;
    }

    @PutMapping("/{id}/aprobacion")
    public ResponseEntity<Testimonio> asignarAprobador(@PathVariable Integer id, @RequestBody AprobacionRequest req) {
        try {
            testimonioService.aprobarTestimonio(id, req.estadoAprobado == null ? Boolean.TRUE : req.estadoAprobado);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

}
