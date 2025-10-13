package com.example.cepsacbackend.Config;

import com.example.cepsacbackend.Repository.UsuarioRepository;
import com.example.cepsacbackend.Config.Security.CustomUserDetails;
import com.example.cepsacbackend.Entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UsuarioRepository repouser;

    @Bean
    public UserDetailsService servicioDetallesUsuario() {
        return nombreUsuario -> {
            Usuario usuario = repouser.findByCorreo(nombreUsuario)
                    .orElseThrow(() -> {
                        return new UsernameNotFoundException("Usuario no encontrado: " + nombreUsuario);
                    });
            // validamos estado para el login
            CustomUserDetails customDetails = new CustomUserDetails(usuario);
            return customDetails;
        };
    }

    @Bean
    public AuthenticationManager gestorAutenticacion(AuthenticationConfiguration configuracion) throws Exception {
        return configuracion.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder codificadorContrasena() {
        return new BCryptPasswordEncoder();
    }
}
