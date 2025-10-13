package com.example.cepsacbackend.Controller;

import com.example.cepsacbackend.Dto.MetodoPago.MetodoPagoRequestDTO;
import com.example.cepsacbackend.Dto.MetodoPago.MetodoPagoResponseDTO;
import com.example.cepsacbackend.Entity.MetodoPago;
import com.example.cepsacbackend.Mapper.MetodoPagoMapper;
import com.example.cepsacbackend.Service.MetodoPagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/metodos-pago")
public class MetodoPagoController {

    private final MetodoPagoService metodoPagoService;
    private final MetodoPagoMapper metodoPagoMapper;

    @GetMapping("/activos")
    public ResponseEntity<List<MetodoPagoResponseDTO>> getAllMetodosActivos() {
        List<MetodoPago> metodos = metodoPagoService.obtenerActivos();
        List<MetodoPagoResponseDTO> response = metodoPagoMapper.toResponseDTOList(metodos);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    // @PreAuthorize("hasRole('ADMINISTRADOR')") // Recomendado
    public ResponseEntity<List<MetodoPagoResponseDTO>> getAllMetodosAdmin() {
        List<MetodoPago> metodos = metodoPagoService.obtenerTodos();
        List<MetodoPagoResponseDTO> response = metodoPagoMapper.toResponseDTOList(metodos);
        return ResponseEntity.ok(response);
    }

    /** GET: Obtener método por ID (para ver detalles de inactivos también). */
    @GetMapping("/{id}")
    // @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<MetodoPagoResponseDTO> getMetodoById(@PathVariable Short id) {
        MetodoPago metodo = metodoPagoService.obtenerPorId(id);
        return ResponseEntity.ok(metodoPagoMapper.toResponseDTO(metodo));
    }

    /** POST: Crear nuevo método de pago. */
    @PostMapping
    // @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<MetodoPagoResponseDTO> createMetodo(@RequestBody @Valid MetodoPagoRequestDTO dto) {
        MetodoPago nuevoMetodo = metodoPagoService.crearMetodo(dto);
        return new ResponseEntity<>(metodoPagoMapper.toResponseDTO(nuevoMetodo), HttpStatus.CREATED);
    }

    /** PUT: Actualizar detalles de un método de pago. */
    @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<MetodoPagoResponseDTO> updateMetodo(@PathVariable Short id, @RequestBody @Valid MetodoPagoRequestDTO dto) {
        MetodoPago metodoActualizado = metodoPagoService.actualizarMetodo(id, dto);
        return ResponseEntity.ok(metodoPagoMapper.toResponseDTO(metodoActualizado));
    }

    /** PUT: Cambiar Estado (Soft Delete/Restore). */
    @PutMapping("/{id}/estado")
    // @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<MetodoPagoResponseDTO> cambiarEstadoMetodo(@PathVariable Short id, @RequestParam Boolean activo) {
        MetodoPago metodoActualizado = metodoPagoService.cambiarEstado(id, activo);
        return ResponseEntity.ok(metodoPagoMapper.toResponseDTO(metodoActualizado));
    }

    /** DELETE: Eliminar permanentemente (Solo si es absolutamente necesario). */
    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMINISTRADOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteMetodo(@PathVariable Short id) {
        metodoPagoService.eliminarMetodo(id);
        return ResponseEntity.noContent().build();
    }
}