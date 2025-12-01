package com.example.cepsacbackend.auditory;

import com.example.cepsacbackend.config.security.CustomUserDetails;
import com.example.cepsacbackend.model.AuditableEntity; 
import jakarta.persistence.EntityManager;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AuditoriaListener {
    private static final Logger LOGGER = Logger.getLogger(AuditoriaListener.class.getName());

    //insert para registrar antes de persistir
    @PrePersist
    public void prePersist(Object object) {
        perform(object, OperacionAuditoria.INSERT);
    }

    //update para registrar antes de actualizar
    @PreUpdate
    public void preUpdate(Object object) {
        perform(object, OperacionAuditoria.UPDATE);
    }

    //delete para registrar antes de eliminar
    @PreRemove
    public void preRemove(Object object) {
        perform(object, OperacionAuditoria.DELETE);
    }

    // logica para registrar auditoría
    private void perform(Object object, OperacionAuditoria operacion) {
        setAuditableFields(object);
        AuditoriaService auditoriaService = BeanUtil.getBean(AuditoriaService.class);
        if (auditoriaService == null) {
            LOGGER.log(Level.SEVERE, "auditoriaservice no disponible. no se registrará la auditoría.");
            return;
        }
        Auditoria auditoria = new Auditoria();
        auditoria.setEntidad(object.getClass().getSimpleName());
        auditoria.setOperacion(operacion);
        auditoria.setUsuarioId(getUsuarioId());
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            Object id = entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(object);
            if (id != null) {
                auditoria.setEntidadId(id.toString());
            }
            switch (operacion) {
                case INSERT -> auditoria.setValoresNuevos(auditoriaService.convertObjectToJson(object));
                case UPDATE -> {
                    Object oldObject = getOldObject(entityManager, object);
                    auditoria.setValoresAnteriores(auditoriaService.convertObjectToJson(oldObject));
                    auditoria.setValoresNuevos(auditoriaService.convertObjectToJson(object));
                }
                case DELETE -> auditoria.setValoresAnteriores(auditoriaService.convertObjectToJson(object));
            }
            auditoriaService.registrarAuditoria(auditoria);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "error en la auditoría: " + e.getMessage());
        }
    }
    
    //este metodo establece los campos del abstract auditable en las entidades
    private void setAuditableFields(Object object) {
        if (object instanceof AuditableEntity auditableEntity) { 
            auditableEntity.setFechaModificacion(LocalDateTime.now());
            Integer usuarioId = getUsuarioId();
            if (usuarioId != null) {
                EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
                if (entityManager != null) {
                    try {
                        // usar getReference para evitar consulta innecesaria
                        var usuarioRef = entityManager.getReference(
                            com.example.cepsacbackend.model.Usuario.class, 
                            usuarioId
                        );
                        auditableEntity.setUsuarioModificador(usuarioRef);
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "No se pudo establecer usuario modificador: " + e.getMessage());
                    }
                } else {
                    LOGGER.log(Level.WARNING, "EntityManager no disponible.");
                }
            }
        }
    }
    
    // obtiene el estado anterior del model desde la base de datos
    private Object getOldObject(EntityManager entityManager, Object newObject) {
        try {
            Object id = entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(newObject);
            if (id == null)
                return null;
            Object oldObject = entityManager.find(newObject.getClass(), id);
            if (oldObject == null)
                return null;
            Map<String, Object> oldStateMap = new HashMap<>();
            for (var field : newObject.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(oldObject);
                if (value != null && isSimpleValue(value)) {
                    oldStateMap.put(field.getName(), value);
                }
            }
            return oldStateMap;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "no se pudo obtener el estado anterior: " + e.getMessage());
            return null;
        }
    }

    // verifica si valor es simple
    private boolean isSimpleValue(Object value) {
        return value instanceof String || value instanceof Number || value instanceof Boolean
                || value instanceof java.util.Date;
    }

    // obtiene el id del usuario autenticado
    private Integer getUsuarioId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal().toString()))
            return null;
        Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserDetails user) {
            return user.getId();
        }
        return null;
    }
}