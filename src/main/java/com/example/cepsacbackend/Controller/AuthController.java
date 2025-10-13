package com.example.cepsacbackend.Controller;

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

import com.example.cepsacbackend.Config.Security.JwtService;
import com.example.cepsacbackend.Dto.Login.AuthResponseDTO;
import com.example.cepsacbackend.Dto.Login.LoginRequestDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> iniciarSesion(@Valid @RequestBody LoginRequestDTO peticion) {
        // autenticar credenciales
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        peticion.getCorreo(),
                        peticion.getPassword()
                )
        );
        // traemos userdetails
        final UserDetails detallesUsuario = userDetailsService.loadUserByUsername(peticion.getCorreo());
        // generamos token
        final String tokenJwt = jwtService.generarToken(detallesUsuario);
        // devolvemos token
        return ResponseEntity.ok(AuthResponseDTO.builder().token(tokenJwt).build());
    }
}