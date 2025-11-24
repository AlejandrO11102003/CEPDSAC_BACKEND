package com.example.cepsacbackend.controller;

import com.example.cepsacbackend.dto.MetodoPago.MetodoPagoRequestDTO;
import com.example.cepsacbackend.dto.MetodoPago.MetodoPagoResponseDTO;
import com.example.cepsacbackend.mapper.MetodoPagoMapper;
import com.example.cepsacbackend.model.MetodoPago;
import com.example.cepsacbackend.service.CloudinaryService;
import com.example.cepsacbackend.service.MetodoPagoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/metodos-pago")
public class MetodoPagoController {

    private final MetodoPagoService metodoPagoService;
    private final MetodoPagoMapper metodoPagoMapper;
    private final CloudinaryService cloudinaryService;

    @GetMapping("/activos")
    public ResponseEntity<List<MetodoPagoResponseDTO>> getAllMetodosActivos() {
        List<MetodoPago> metodos = metodoPagoService.obtenerActivos();
        List<MetodoPagoResponseDTO> response = metodoPagoMapper.toResponseDTOList(metodos);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MetodoPagoResponseDTO>> getAllMetodosAdmin() {
        List<MetodoPago> metodos = metodoPagoService.obtenerTodos();
        List<MetodoPagoResponseDTO> response = metodoPagoMapper.toResponseDTOList(metodos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetodoPagoResponseDTO> getMetodoById(@PathVariable Short id) {
        MetodoPago metodo = metodoPagoService.obtenerPorId(id);
        return ResponseEntity.ok(metodoPagoMapper.toResponseDTO(metodo));
    }

    @PostMapping
    public ResponseEntity<MetodoPagoResponseDTO> createMetodo(@RequestBody @Valid MetodoPagoRequestDTO dto) {
        MetodoPago nuevoMetodo = metodoPagoService.crearMetodo(dto);
        return new ResponseEntity<>(metodoPagoMapper.toResponseDTO(nuevoMetodo), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetodoPagoResponseDTO> updateMetodo(@PathVariable Short id, @RequestBody @Valid MetodoPagoRequestDTO dto) {
        MetodoPago metodoActualizado = metodoPagoService.actualizarMetodo(id, dto);
        return ResponseEntity.ok(metodoPagoMapper.toResponseDTO(metodoActualizado));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<MetodoPagoResponseDTO> cambiarEstadoMetodo(@PathVariable Short id, @RequestParam Boolean activo) {
        MetodoPago metodoActualizado = metodoPagoService.cambiarEstado(id, activo);
        return ResponseEntity.ok(metodoPagoMapper.toResponseDTO(metodoActualizado));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteMetodo(@PathVariable Short id) {
        metodoPagoService.eliminarMetodo(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload-qr")
    public ResponseEntity<Map<String, String>> uploadQR(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El archivo está vacío"));
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body(Map.of("error", "El archivo debe ser una imagen"));
            }
            if (file.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest().body(Map.of("error", "La imagen no debe superar los 5MB"));
            }
            // post cloudinary
            Map<String, Object> uploadResult = cloudinaryService.upload(file, "metodos-pago/qr");
            String imageUrl = (String) uploadResult.get("secure_url");
            return ResponseEntity.ok(Map.of("url", imageUrl));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al subir la imagen: " + e.getMessage()));
        }
    }
}