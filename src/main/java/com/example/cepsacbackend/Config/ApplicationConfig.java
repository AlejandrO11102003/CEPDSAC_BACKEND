package com.example.cepsacbackend.config;

import com.example.cepsacbackend.config.security.CustomUserDetails;
import com.example.cepsacbackend.model.Usuario;
import com.example.cepsacbackend.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;

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

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Configura el módulo de Hibernate para Jackson
        Hibernate6Module hibernate6Module = new Hibernate6Module();
        // Deshabilita la característica que fuerza la inicialización de propiedades lazy
        hibernate6Module.configure(Hibernate6Module.Feature.FORCE_LAZY_LOADING, false);
        objectMapper.registerModule(hibernate6Module);
        
        // Es buena práctica registrar también el módulo de Java Time si no lo hace Spring Boot por defecto
        objectMapper.findAndRegisterModules(); 
        
        return objectMapper;
    }
}
