package com.example.cepsacbackend.Controller;

import com.example.cepsacbackend.Dto.DescuentoAplicacion.DescuentoAplicacionCreateDTO;
import com.example.cepsacbackend.Dto.DescuentoAplicacion.DescuentoAplicacionResponseDTO;
import com.example.cepsacbackend.Service.DescuentoAplicacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aplicaciondescuento")
@RequiredArgsConstructor
public class DescuentoAplicacionController {

    private final DescuentoAplicacionService descuentoAplicacionService;

    @GetMapping("/listar")
    public ResponseEntity<List<DescuentoAplicacionResponseDTO>> listar() {
        return ResponseEntity.ok(descuentoAplicacionService.listar());
    }

    @GetMapping("/obtener/{id}")
    public ResponseEntity<DescuentoAplicacionResponseDTO> obtenerPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(descuentoAplicacionService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<DescuentoAplicacionResponseDTO> crear(@Valid @RequestBody DescuentoAplicacionCreateDTO dto) {
        return new ResponseEntity<>(descuentoAplicacionService.crear(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            descuentoAplicacionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}