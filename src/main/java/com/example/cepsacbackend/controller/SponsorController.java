package com.example.cepsacbackend.controller;

import com.example.cepsacbackend.model.Sponsor;
import com.example.cepsacbackend.service.SponsorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/sponsors")
@RequiredArgsConstructor
public class SponsorController {

    private final SponsorService sponsorService;

    @GetMapping
    public ResponseEntity<List<Sponsor>> listar() {
        return ResponseEntity.ok(sponsorService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sponsor> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(sponsorService.obtenerPorId(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Sponsor> crear(
            @RequestParam("nombre") String nombre,
            @RequestPart("imagen") MultipartFile imagen) {
        return new ResponseEntity<>(sponsorService.crear(nombre, imagen), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Sponsor> actualizar(
            @PathVariable Integer id,
            @RequestParam("nombre") String nombre,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        return ResponseEntity.ok(sponsorService.actualizar(id, nombre, imagen));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        sponsorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
