package com.example.cepsacbackend.auditory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hibernate.Hibernate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AuditoriaService {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    // inyeccion del object mapper configurado en spring
    @Autowired
    private ObjectMapper defaultObjectMapper;

    private ObjectMapper getAuditoriaMapper() {
        ObjectMapper mapper = defaultObjectMapper.copy();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.FAIL_ON_SELF_REFERENCES);
        mapper.addMixIn(Object.class, IgnoreHibernatePropertiesMixin.class);
        return mapper;
    }

    // clase mixin para ignorar propiedades de hibernate
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "hibernateLazyInitializer"})
    private abstract static class IgnoreHibernatePropertiesMixin {}

    // registra la auditoría de forma asincrona osea no bloqueante
    @Async
    public void registrarAuditoria(Auditoria auditoria) {
        try {
            auditoriaRepository.save(auditoria);
            log.debug("Auditoría registrada: {} {} ID:{}", auditoria.getOperacion(), auditoria.getEntidad(), auditoria.getEntidadId());
        } catch (Exception e) {
            log.error("Error al registrar auditoría de forma asíncrona", e);
        }
    }

    // convierte un objeto a su representación JSON
    public String convertObjectToJson(Object object) {
        if (object == null) {
            return null;
        }

        try {
            ObjectMapper mapper = getAuditoriaMapper();
            
            //si el objeto es un map lo serializo directamente a json
            if (object instanceof Map) {
                return mapper.writeValueAsString(object);
            }
            
            //intento serializar el objeto completo a json
            return mapper.writeValueAsString(object);
            
        } catch (JsonProcessingException e) {
            log.warn("No se pudo serializar el objeto completo, creando versión simplificada: {}", e.getMessage());
            return crearVersionSimplificada(object);
        }
    }
    
    private String crearVersionSimplificada(Object object) {
        try {
            Map<String, Object> simplificado = new HashMap<>();
            simplificado.put("clase", object.getClass().getSimpleName());
            simplificado.put("info", "Objeto con relaciones lazy no inicializadas");
            // intenta obtener algunos campos básicos mediante reflection
            var fields = object.getClass().getDeclaredFields();
            for (var field : fields) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(object);
                    // solo incluye valores simples inicializados
                    if (value != null && Hibernate.isInitialized(value) && isSimpleType(value)) {
                        simplificado.put(field.getName(), value);
                    }
                } catch (Exception ignored) {
                }
            }
            
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(simplificado);
        } catch (Exception e) {
            log.error("Error creando versión simplificada: {}", e.getMessage());
            return "{\"error\":\"No se pudo serializar el objeto\",\"tipo\":\"" + object.getClass().getSimpleName() + "\"}";
        }
    }
    
    // verifica si un tipo es simple (no es una entidad u objeto complejo)
    private boolean isSimpleType(Object value) {
        return value instanceof String 
            || value instanceof Number 
            || value instanceof Boolean 
            || value instanceof java.util.Date
            || value instanceof java.time.LocalDate
            || value instanceof java.time.LocalDateTime
            || value.getClass().isEnum();
    }
}
