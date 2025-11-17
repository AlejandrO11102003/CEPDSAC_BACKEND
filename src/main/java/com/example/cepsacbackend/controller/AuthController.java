package com.example.cepsacbackend.controller;

import com.example.cepsacbackend.dto.Login.MessageResponseDTO;
import com.example.cepsacbackend.exception.BadRequestException;
import com.example.cepsacbackend.exception.ResourceNotFoundException;
import com.example.cepsacbackend.model.RecuperacionPasswordToken;
import com.example.cepsacbackend.model.Usuario;
import com.example.cepsacbackend.repository.RecuperacionTokenPasswordRepository;
import com.example.cepsacbackend.repository.UsuarioRepository;
import com.example.cepsacbackend.service.EmailService;
import com.example.cepsacbackend.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.cepsacbackend.config.security.CustomUserDetails;

import com.example.cepsacbackend.config.security.JwtService;
import com.example.cepsacbackend.dto.Login.AuthResponseDTO;
import com.example.cepsacbackend.dto.Login.LoginRequestDTO;
import com.example.cepsacbackend.dto.Login.RecuperarPasswordRequestDTO;
import com.example.cepsacbackend.dto.Login.ResetPasswordRequestDTO;
import com.example.cepsacbackend.enums.Rol;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final RecuperacionTokenPasswordRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> iniciarSesion(@Valid @RequestBody LoginRequestDTO peticion) {
        try {
            //autenticar al usuario
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            peticion.getCorreo(),
                            peticion.getPassword()
                    )
            );
            final UserDetails detallesUsuario = userDetailsService.loadUserByUsername(peticion.getCorreo());
            final String tokenJwt = jwtService.generarToken(detallesUsuario); 
            CustomUserDetails customUserDetails = (CustomUserDetails) detallesUsuario;
            Rol rol = Rol.valueOf(customUserDetails.getRol());
            return ResponseEntity.ok(AuthResponseDTO.builder().token(tokenJwt).rol(rol).build());
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Correo o contraseña incorrectos. Por favor, verifica tus credenciales e intenta nuevamente.");
        } catch (AuthenticationException e) {
            throw new BadRequestException("No se pudo autenticar. Por favor, verifica tus credenciales.");
        }
    }

    @PostMapping("/forgot-password")
    @Transactional
    public ResponseEntity<MessageResponseDTO> solicitarRestablecerPassword(@Valid @RequestBody RecuperarPasswordRequestDTO request) {
        // Buscar usuario por correo
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("No se encontró ningún usuario con el correo: %s", request.getCorreo())));
        tokenRepository.deleteByUsuario(usuario);
        // Generar nuevo token único
        String token = UUID.randomUUID().toString();
        RecuperacionPasswordToken resetToken = new RecuperacionPasswordToken();
        resetToken.setToken(token);
        resetToken.setUsuario(usuario);
        resetToken.setFechaExpiracion(LocalDateTime.now().plusHours(1));
        tokenRepository.save(resetToken);

        // Enviar email con el token
        emailService.enviarEmailRestablecerPassword(usuario.getCorreo(), token);
        return ResponseEntity.ok(MessageResponseDTO.builder()
                .mensaje("Se ha enviado un correo con las instrucciones para restablecer tu contraseña")
                .build());
    }

    @PostMapping("/reset-password")
    @Transactional
    public ResponseEntity<MessageResponseDTO> restablecerPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {
        // Buscar token
        RecuperacionPasswordToken resetToken = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Token inválido o no encontrado"));
        // validar que no haya sido utilizado
        if (resetToken.getUtilizado()) {
            throw new BadRequestException("Este token ya ha sido utilizado");
        }
        // validar que no haya expirado = 1hr despues
        if (resetToken.isExpirado()) {
            throw new BadRequestException("El token ha expirado. Por favor, solicita un nuevo restablecimiento");
        }
        // Actualizar la contraseña del usuario usando el service (invalida caché)
        Usuario usuario = resetToken.getUsuario();
        usuarioService.cambiarPassword(usuario.getIdUsuario(), request.getNuevaPassword());
        // inhabilitar el token 
        resetToken.setUtilizado(true);
        tokenRepository.save(resetToken);
        return ResponseEntity.ok(MessageResponseDTO.builder()
                .mensaje("Tu contraseña ha sido restablecida exitosamente")
                .build());
    }
}