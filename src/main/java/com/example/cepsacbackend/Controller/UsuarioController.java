package com.example.cepsacbackend.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.example.cepsacbackend.Dto.Usuario.UsuarioCreateDTO;
import com.example.cepsacbackend.Dto.Usuario.UsuarioListResponseDTO;
import com.example.cepsacbackend.Dto.Usuario.UsuarioPatchDTO;
import com.example.cepsacbackend.Dto.Usuario.UsuarioResponseDTO;
import com.example.cepsacbackend.Dto.Usuario.UsuarioUpdateDTO;
import com.example.cepsacbackend.Enums.Rol;
import com.example.cepsacbackend.Service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/listar")
    public List<UsuarioListResponseDTO> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/listar/{rol}")
    public List<UsuarioListResponseDTO> listarUsuariosPorRol(@PathVariable Rol rol) {
        return usuarioService.listarUsuariosPorRol(rol);
    }

    @GetMapping("/obtener/{idUsuario}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuario(@PathVariable Integer idUsuario) {
        try {
            UsuarioResponseDTO usuarioDTO = usuarioService.obtenerUsuario(idUsuario);
            return ResponseEntity.ok(usuarioDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@Valid @RequestBody UsuarioCreateDTO dto) {
        try {
            UsuarioResponseDTO nuevoUsuarioDTO = usuarioService.crearUsuario(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuarioDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@Valid @RequestBody UsuarioUpdateDTO dto) {
        try {
            UsuarioResponseDTO usuarioActualizadoDTO = usuarioService.actualizarUsuario(dto);
            return ResponseEntity.ok(usuarioActualizadoDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/actualizar-parcial")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuarioParcialmente(@Valid @RequestBody UsuarioPatchDTO dto) {
        try {
            UsuarioResponseDTO usuarioActualizadoDTO = usuarioService.actualizarUsuarioParcialmente(dto);
            return ResponseEntity.ok(usuarioActualizadoDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/eliminar/{idUsuario}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Integer idUsuario) {
        try {
            usuarioService.eliminarUsuario(idUsuario);
            return ResponseEntity.ok("Usuario suspendido/eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/restaurar/{idUsuario}")
    public ResponseEntity<UsuarioResponseDTO> restaurarUsuario(@PathVariable Integer idUsuario) {
        try {
            UsuarioResponseDTO usuarioRestaurado = usuarioService.restaurarUsuario(idUsuario);
            return ResponseEntity.ok(usuarioRestaurado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}