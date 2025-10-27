package com.example.cepsacbackend.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ALUMNO')")
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
        UsuarioResponseDTO usuarioDTO = usuarioService.obtenerUsuario(idUsuario);
        return ResponseEntity.ok(usuarioDTO);
    }

    @PostMapping("/crear")
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@Valid @RequestBody UsuarioCreateDTO dto) {
        UsuarioResponseDTO nuevoUsuarioDTO = usuarioService.crearUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuarioDTO);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@Valid @RequestBody UsuarioUpdateDTO dto) {
        UsuarioResponseDTO usuarioActualizadoDTO = usuarioService.actualizarUsuario(dto);
        return ResponseEntity.ok(usuarioActualizadoDTO);
    }

    @PatchMapping("/actualizar-parcial")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuarioParcialmente(@Valid @RequestBody UsuarioPatchDTO dto) {
        UsuarioResponseDTO usuarioActualizadoDTO = usuarioService.actualizarUsuarioParcialmente(dto);
        return ResponseEntity.ok(usuarioActualizadoDTO);
    }

    @DeleteMapping("/eliminar/{idUsuario}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Integer idUsuario) {
        usuarioService.eliminarUsuario(idUsuario);
        return ResponseEntity.ok("Usuario suspendido/eliminado correctamente");
    }

    @PostMapping("/restaurar/{idUsuario}")
    public ResponseEntity<UsuarioResponseDTO> restaurarUsuario(@PathVariable Integer idUsuario) {
        UsuarioResponseDTO usuarioRestaurado = usuarioService.restaurarUsuario(idUsuario);
        return ResponseEntity.ok(usuarioRestaurado);
    }
}