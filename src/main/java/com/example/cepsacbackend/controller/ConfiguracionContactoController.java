package com.example.cepsacbackend.controller;

import com.example.cepsacbackend.model.ConfiguracionContacto;
import com.example.cepsacbackend.service.ConfiguracionContactoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuracion/contacto")
@RequiredArgsConstructor
public class ConfiguracionContactoController {

    private final ConfiguracionContactoService service;

    @GetMapping
    public ResponseEntity<ConfiguracionContacto> obtener() {
        return ResponseEntity.ok(service.obtener());
    }

    @PutMapping
    public ResponseEntity<ConfiguracionContacto> actualizar(@RequestBody ConfiguracionContacto configuracion) {
        return ResponseEntity.ok(service.actualizar(configuracion));
    }
}
