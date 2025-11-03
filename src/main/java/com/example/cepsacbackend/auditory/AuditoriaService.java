package com.example.cepsacbackend.auditory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
        mapper.addMixIn(Object.class, IgnoreHibernatePropertiesMixin.class);
        return mapper;
    }

    // clase mixin para ignorar propiedades de hibernate
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private abstract static class IgnoreHibernatePropertiesMixin {}

    // registra la auditoría de forma asincrona osea no bloqueante
    @Async
    public void registrarAuditoria(Auditoria auditoria) {
        try {
            auditoriaRepository.save(auditoria);
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
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(" Error conversion json:", e.getMessage());
            return "{\"error\":\"No se pudo serializar el objeto\"}";
        }
    }
}
