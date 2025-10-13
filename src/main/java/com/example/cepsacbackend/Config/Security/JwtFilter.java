package com.example.cepsacbackend.Config.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
            cadenaFiltros.doFilter(peticion, respuesta);
            return; //si no hay token detenemos flujo
        }

        // traemos token y username
        tokenJwt = encabezadoAuth.substring(7);
        nombreUsuario = servicioJwt.extraerNombreUsuario(tokenJwt);

        // validamos token
        if (nombreUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
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
            }
        }
        cadenaFiltros.doFilter(peticion, respuesta);
    }
}