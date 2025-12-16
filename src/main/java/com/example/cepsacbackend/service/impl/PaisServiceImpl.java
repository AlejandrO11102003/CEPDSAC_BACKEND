package com.example.cepsacbackend.service.impl;

import com.example.cepsacbackend.exception.ConflictException;
import com.example.cepsacbackend.exception.ResourceNotFoundException;
import com.example.cepsacbackend.model.Pais;
import com.example.cepsacbackend.repository.PaisRepository;
import com.example.cepsacbackend.service.PaisService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaisServiceImpl implements PaisService {

    private final PaisRepository paisRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Pais> getAllPaises() {
        return paisRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Pais getPaisById(Short idPais) {
        return paisRepository.findById(idPais)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("No se encontró el país con ID %d. Verifique que el ID sea correcto.", idPais)));
    }

    @Override
    @Transactional
    public Pais createPais(Pais pais) {
        if (paisRepository.findByNombre(pais.getNombre()).isPresent()) {
            throw new ConflictException(
                String.format("Ya existe un país registrado con el nombre '%s'. Por favor, use un nombre diferente.", pais.getNombre()));
        }
        return paisRepository.save(pais);
    }

    @Override
    @Transactional
    public Pais updatePais(Short idPais, Pais paisDetails) {
        Pais paisExistente = getPaisById(idPais);
        paisRepository.findByNombre(paisDetails.getNombre()).ifPresent(paisEncontrado -> {
            if (!paisEncontrado.getIdPais().equals(idPais)) {
                throw new ConflictException(
                    String.format("El nombre '%s' ya está siendo usado por otro país. Por favor, elija un nombre diferente.", paisDetails.getNombre()));
            }
        });
        paisExistente.setNombre(paisDetails.getNombre());
        return paisRepository.save(paisExistente);
    }

    @Override
    @Transactional
    public void deletePais(Short idPais) {
        if (!paisRepository.existsById(idPais)) {
            throw new ResourceNotFoundException(
                String.format("No se puede eliminar. El país con ID %d no existe.", idPais));
        }
        paisRepository.deleteById(idPais);
    }
}
