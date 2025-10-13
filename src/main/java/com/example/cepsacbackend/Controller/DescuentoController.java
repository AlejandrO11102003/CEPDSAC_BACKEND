package com.example.cepsacbackend.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cepsacbackend.Dto.Descuento.DescuentoCreateDTO;
import com.example.cepsacbackend.Dto.Descuento.DescuentoResponseDTO;
import com.example.cepsacbackend.Dto.Descuento.DescuentoUpdateDTO;
import com.example.cepsacbackend.Service.DescuentoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/descuentos")
@Validated
@RequiredArgsConstructor
public class DescuentoController {

    private final DescuentoService descuentoService;

    @GetMapping("/listar")
    public List<DescuentoResponseDTO> listar() {
        return descuentoService.listar();
    }

    @GetMapping("/obtener/{id}")
    public ResponseEntity<DescuentoResponseDTO> obtener(@PathVariable("id") Short id) {
        try {
            return ResponseEntity.ok(descuentoService.obtener(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<DescuentoResponseDTO> crear(@Valid @RequestBody DescuentoCreateDTO dto) {
        try {
            DescuentoResponseDTO creado = descuentoService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<DescuentoResponseDTO> actualizar(@Valid @RequestBody DescuentoUpdateDTO dto) {
        try {
            DescuentoResponseDTO actualizado = descuentoService.actualizar(dto);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable("id") Short id) {
        try {
            descuentoService.eliminar(id);
            return ResponseEntity.ok("descuento eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
