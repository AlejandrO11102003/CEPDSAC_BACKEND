package com.example.cepsacbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cepsacbackend.config.security.JwtService;
import com.example.cepsacbackend.dto.Login.AuthResponseDTO;
import com.example.cepsacbackend.dto.Login.LoginRequestDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> iniciarSesion(@Valid @RequestBody LoginRequestDTO peticion) {
        log.info("Intentando autenticar al usuario: {}", peticion.getCorreo());
        // autenticar credenciales
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        peticion.getCorreo(),
                        peticion.getPassword()
                )
        );
        log.info("Usuario {} autenticado correctamente", peticion.getCorreo());

        // traemos userdetails
        final UserDetails detallesUsuario = userDetailsService.loadUserByUsername(peticion.getCorreo());
        log.info("Cargando detalles del usuario: {}", peticion.getCorreo());
        // generamos token
        final String tokenJwt = jwtService.generarToken(detallesUsuario);
        log.info("Token JWT generado para el usuario: {}", peticion.getCorreo());
        // devolvemos token
        return ResponseEntity.ok(AuthResponseDTO.builder().token(tokenJwt).build());
    }
}