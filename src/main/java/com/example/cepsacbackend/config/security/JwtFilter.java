package com.example.cepsacbackend.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtService servicioJwt;
    private final UserDetailsService servicioDetallesUsuario;

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        final String path = request.getServletPath();
        // permitir la ruta de autenticación sin filtrar
        return path.contains("/api/auth/login");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest peticion,@NonNull HttpServletResponse respuesta,@NonNull FilterChain cadenaFiltros) throws ServletException, IOException {
        final String encabezadoAuth = peticion.getHeader("Authorization");
        final String tokenJwt;
        final String nombreUsuario;
        // validamos encabezado
        if (encabezadoAuth == null || !encabezadoAuth.startsWith("Bearer ")) {
            log.debug("JwtFilter: Authorization header missing or does not start with 'Bearer '");
            cadenaFiltros.doFilter(peticion, respuesta);
            return; //si no hay token detenemos flujo
        }

        // traemos token y username
        tokenJwt = encabezadoAuth.substring(7);
        try {
            nombreUsuario = servicioJwt.extraerNombreUsuario(tokenJwt);
        } catch (Exception ex) {
            log.warn("JwtFilter: error al parsear token JWT: {}", ex.getMessage());
            cadenaFiltros.doFilter(peticion, respuesta);
            return;
        }

        // validamos token
        if (nombreUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails detallesUsuario = this.servicioDetallesUsuario.loadUserByUsername(nombreUsuario);
                if (servicioJwt.esTokenValido(tokenJwt, detallesUsuario)) {
                    // autenticamos
                    UsernamePasswordAuthenticationToken tokenAutenticacion = new UsernamePasswordAuthenticationToken(
                            detallesUsuario,
                            null,
                            detallesUsuario.getAuthorities()
                    );
                    tokenAutenticacion.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(peticion)
                    );
                    // pasamos autenticacion al security
                    SecurityContextHolder.getContext().setAuthentication(tokenAutenticacion);
                    log.debug("JwtFilter: usuario autenticado {}", nombreUsuario);
                } else {
                    log.debug("JwtFilter: token no válido para usuario {}", nombreUsuario);
                }
            } catch (Exception ex) {
                log.warn("JwtFilter: error cargando detalles de usuario {} : {}", nombreUsuario, ex.getMessage());
            }
        }
        cadenaFiltros.doFilter(peticion, respuesta);
    }
}