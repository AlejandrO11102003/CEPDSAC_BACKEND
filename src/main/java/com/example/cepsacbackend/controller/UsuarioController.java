package com.example.cepsacbackend.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import com.example.cepsacbackend.dto.Usuario.UsuarioCreateDTO;
import com.example.cepsacbackend.dto.Usuario.UsuarioListResponseDTO;
import com.example.cepsacbackend.dto.Usuario.UsuarioPatchDTO;
import com.example.cepsacbackend.dto.Usuario.UsuarioResponseDTO;
import com.example.cepsacbackend.dto.Usuario.UsuarioUpdateDTO;
import com.example.cepsacbackend.service.UsuarioService;
import com.example.cepsacbackend.enums.Rol;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;

    //EP modulo administracion 
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioListResponseDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    //EP modulo administracion
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/listar/{rol}")
    public ResponseEntity<List<UsuarioListResponseDTO>> listarUsuariosPorRol(@PathVariable Rol rol) {
        return ResponseEntity.ok(usuarioService.listarUsuariosPorRol(rol));
    }

    // EP modulo administracion y profesor
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/alumnos")
    public ResponseEntity<Page<UsuarioListResponseDTO>> listarAlumnosPaginado(
            @RequestParam(required = false) String buscar,
            @RequestParam(defaultValue = "false") boolean soloConMatricula,
            Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listarAlumnosPaginado(buscar, soloConMatricula, pageable));
    }

    //EP modulo administracion y usuario mismo
    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuario(@PathVariable Integer idUsuario) {
        UsuarioResponseDTO usuarioDTO = usuarioService.obtenerUsuario(idUsuario);
        return ResponseEntity.ok(usuarioDTO);
    }
    
    //EP modulo administracion y alumno al loguearse por primera vez
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@Valid @RequestBody UsuarioCreateDTO dto) {
        UsuarioResponseDTO nuevoUsuarioDTO = usuarioService.crearUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuarioDTO);
    }

    //EP modulo administracion y usuario mismo, actualizar datos
    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@PathVariable Integer idUsuario, @Valid @RequestBody UsuarioUpdateDTO dto) {
        UsuarioResponseDTO usuarioActualizadoDTO = usuarioService.actualizarUsuario(idUsuario, dto);
        return ResponseEntity.ok(usuarioActualizadoDTO);
    }

    //EP modulo administracion y usuario mis, actualizar correo o contraseña, falta implementar notificador email backend
    @PatchMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuarioParcialmente(@PathVariable Integer idUsuario, @Valid @RequestBody UsuarioPatchDTO dto) {
        UsuarioResponseDTO usuarioActualizadoDTO = usuarioService.actualizarUsuarioParcialmente(idUsuario, dto);
        log.info("fin del hilo principal");
        return ResponseEntity.ok(usuarioActualizadoDTO);
    }

    //EP modulo administracion, eliminar usuario (cambio de estado a SUSPENDIDO)
    @DeleteMapping("/{idUsuario}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer idUsuario) {
        usuarioService.eliminarUsuario(idUsuario);
        return ResponseEntity.noContent().build();
    }
    
    //EP modulo administracion, restaurar usuario (cambio de estado a ACTIVO)
    @PostMapping("/{idUsuario}/restaurar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponseDTO> restaurarUsuario(@PathVariable Integer idUsuario) {
        UsuarioResponseDTO usuarioRestaurado = usuarioService.restaurarUsuario(idUsuario);
        return ResponseEntity.ok(usuarioRestaurado);
    }
}