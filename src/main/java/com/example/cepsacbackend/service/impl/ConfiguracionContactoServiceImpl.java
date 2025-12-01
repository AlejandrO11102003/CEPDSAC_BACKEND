package com.example.cepsacbackend.service.impl;

import com.example.cepsacbackend.model.ConfiguracionContacto;
import com.example.cepsacbackend.repository.ConfiguracionContactoRepository;
import com.example.cepsacbackend.service.ConfiguracionContactoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfiguracionContactoServiceImpl implements ConfiguracionContactoService {

    private final ConfiguracionContactoRepository repository;

    @Override
    @Transactional
    public ConfiguracionContacto obtener() {
        return repository.findById(1)
                .orElseGet(() -> {
                    log.info("No existe configuración de contacto, creando valores por defecto");
                    ConfiguracionContacto config = new ConfiguracionContacto();
                    config.setIdConfiguracion(1);
                    config.setCorreoContacto("");
                    config.setTelefono("");
                    config.setWhatsapp("");
                    config.setDireccion("");
                    config.setFacebook("");
                    config.setInstagram("");
                    config.setLinkedin("");
                    config.setTwitter("");
                    return repository.save(config);
                });
    }

    @Override
    @Transactional
    public ConfiguracionContacto actualizar(ConfiguracionContacto configuracion) {
        // buscar el registro existente o crear uno nuevo si no existe
        ConfiguracionContacto existente = repository.findById(1)
                .orElseGet(() -> {
                    log.info("No existe configuración de contacto, creando nueva con ID=1");
                    ConfiguracionContacto nueva = new ConfiguracionContacto();
                    nueva.setIdConfiguracion(1);
                    return nueva;
                });

        existente.setCorreoContacto(configuracion.getCorreoContacto());
        existente.setTelefono(configuracion.getTelefono());
        existente.setWhatsapp(configuracion.getWhatsapp());
        existente.setDireccion(configuracion.getDireccion());
        existente.setFacebook(configuracion.getFacebook());
        existente.setInstagram(configuracion.getInstagram());
        existente.setLinkedin(configuracion.getLinkedin());
        existente.setTwitter(configuracion.getTwitter());

        ConfiguracionContacto actualizada = repository.save(existente);
        log.info("Configuración de contacto actualizada");
        return actualizada;
    }

}
