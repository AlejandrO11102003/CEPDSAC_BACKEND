package com.example.cepsacbackend.service.impl;

import com.example.cepsacbackend.exception.ResourceNotFoundException;
import com.example.cepsacbackend.model.ConfiguracionGeneral;
import com.example.cepsacbackend.repository.ConfiguracionGeneralRepository;
import com.example.cepsacbackend.service.ConfiguracionGeneralService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfiguracionGeneralServiceImpl implements ConfiguracionGeneralService {

    private final ConfiguracionGeneralRepository repository;

    @Override
    @Transactional
    public ConfiguracionGeneral obtener() {
        // siempre devolvemos el primer registro (ID = 1)
        // si no existe, creamos uno con valores por defecto
        return repository.findById(1)
                .orElseGet(() -> {
                    log.info("No existe configuración general, creando valores por defecto");
                    ConfiguracionGeneral config = new ConfiguracionGeneral();
                    config.setIdConfiguracion(1);
                    config.setNumeroEstudiantes(0);
                    config.setNumeroCertificaciones(0);
                    config.setNumeroInstructores(0);
                    config.setNumeroCursos(0);
                    return repository.save(config);
                });
    }

    @Override
    @Transactional
    public ConfiguracionGeneral actualizar(ConfiguracionGeneral configuracion) {
        // buscar el registro existente o crear uno nuevo si no existe
        ConfiguracionGeneral existente = repository.findById(1)
                .orElseGet(() -> {
                    log.info("No existe configuración general, creando nueva con ID=1");
                    ConfiguracionGeneral nueva = new ConfiguracionGeneral();
                    nueva.setIdConfiguracion(1);
                    return nueva;
                });

        existente.setNumeroEstudiantes(configuracion.getNumeroEstudiantes());
        existente.setNumeroCertificaciones(configuracion.getNumeroCertificaciones());
        existente.setNumeroInstructores(configuracion.getNumeroInstructores());
        existente.setNumeroCursos(configuracion.getNumeroCursos());

        ConfiguracionGeneral actualizada = repository.save(existente);
        log.info("Configuración general actualizada");
        return actualizada;
    }

}
