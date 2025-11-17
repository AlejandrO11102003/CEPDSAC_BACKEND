package com.example.cepsacbackend.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter filtroJwt;
    private final RateLimitingFilter rateLimitingFilter;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of(
            "http://127.0.0.1:3000",
            "http://localhost:4200",
            "http://localhost:4000",
            "https://*.ngrok-free.app"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain cadenaFiltroSeguridad(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults()) // habilitar cors
                .authorizeHttpRequests(autorizacion -> autorizacion
                        //aqui vamos agregando las rutas publicas
                        .requestMatchers("/api/monitor/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/images/**").permitAll() // servir img publicas
                        .requestMatchers(HttpMethod.GET, "/api/sponsors/listar").permitAll() // sponsors
                        .requestMatchers(HttpMethod.GET, "/api/cursos-diplomados/listar-index").permitAll() // todos para landing
                        .requestMatchers(HttpMethod.GET, "/api/cursos-diplomados/listar-cursos").permitAll() // solo cursos
                        .requestMatchers(HttpMethod.GET, "/api/cursos-diplomados/listar-diplomados").permitAll() // solo diplomados
                        .requestMatchers(HttpMethod.GET, "/api/cursos-diplomados/detalle/**").permitAll() // detalle curso/diplomado
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll() // registro de alumnos
                        .requestMatchers(HttpMethod.GET, "/api/paises/**").permitAll() //listar paises
                        .requestMatchers(HttpMethod.GET, "/api/tipos-identificacion/**").permitAll() //listar tipos identificacion
                        .requestMatchers(HttpMethod.POST, "/api/matriculas").permitAll() // crear matricula
                        .requestMatchers(HttpMethod.POST, "/api/matriculas/*/notificar-pago").permitAll() // notificar pago
                        .requestMatchers(HttpMethod.GET, "/api/metrics").permitAll() // metrics
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // cors
                        // necesario jwt
                        .anyRequest().authenticated()
                )
                // config de stateless
                .sessionManagement(manejoSesion -> manejoSesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // registrar filtros: primero rate limiting, luego jwt
                .addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
