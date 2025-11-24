package com.example.cepsacbackend.controller;

import com.example.cepsacbackend.model.ConfiguracionGeneral;
import com.example.cepsacbackend.service.ConfiguracionGeneralService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuracion/general")
@RequiredArgsConstructor
public class ConfiguracionGeneralController {

    private final ConfiguracionGeneralService service;

    @GetMapping
    public ResponseEntity<ConfiguracionGeneral> obtener() {
        return ResponseEntity.ok(service.obtener());
    }

    @PutMapping
    public ResponseEntity<ConfiguracionGeneral> actualizar(@RequestBody ConfiguracionGeneral configuracion) {
        return ResponseEntity.ok(service.actualizar(configuracion));
    }
}
