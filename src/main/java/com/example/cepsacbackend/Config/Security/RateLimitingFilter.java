package com.example.cepsacbackend.config.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//filtro para limitar la tasa de requests por IP en endpoints criticos
@Slf4j
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    // cache en memoria de buckets por ip
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    // configuracion de limite: solo para matriculas
    private static final int MATRICULA_LIMIT = 10; // 10 matriculas maximas
    private static final Duration MATRICULA_REFILL = Duration.ofHours(1); // por hora

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        String ip = obtenerIpReal(request);
        String path = request.getServletPath();
        String method = request.getMethod();

        // aplicar rate limiting SOLO a creacion de matriculas
        if (esEndpointMatricula(path, method)) {
            Bucket bucket = obtenerBucket(ip);
            
            if (bucket.tryConsume(1)) {
                log.debug("Rate limit OK para IP {} en creación de matrícula", ip);
                filterChain.doFilter(request, response);
            } else {
                log.warn("⚠️ Rate limit excedido para IP {} - Intentó crear más de {} matrículas por hora", ip, MATRICULA_LIMIT);
                response.setStatus(429); // too many requests
                response.setContentType("application/json");
                response.getWriter().write(String.format(
                    "{\"error\":\"Demasiadas matrículas\",\"mensaje\":\"Has excedido el límite de %d matrículas por hora. Intenta nuevamente en 1 hora.\"}",
                    MATRICULA_LIMIT
                ));
            }
        } else {
            // todos los demas endpoints pasan sin restriccion
            filterChain.doFilter(request, response);
        }
    }
    // obtiene o crea un bucket para la ip dada
    private Bucket obtenerBucket(String ip) {
        return cache.computeIfAbsent(ip, k -> crearNuevoBucket());
    }

    // crea un nuevo bucket con limite de matriculas
    private Bucket crearNuevoBucket() {
        Bandwidth limit = Bandwidth.classic(MATRICULA_LIMIT, Refill.intervally(MATRICULA_LIMIT, MATRICULA_REFILL));
        log.info("Bucket creado para matrículas: {} por hora", MATRICULA_LIMIT);
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    // verifica si el endpoint es el de creacion de matriculas
    private boolean esEndpointMatricula(String path, String method) {
        return "POST".equals(method) && path.startsWith("/api/matriculas");
    }

    // obtiene la IP real del cliente, considerando proxies
    private String obtenerIpReal(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // si viene con multiples ips (proxy chain), tomar la primera
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
