package com.example.cepsacbackend.service;

import com.example.cepsacbackend.exception.BadRequestException;
import com.example.cepsacbackend.exception.ConflictException;
import com.example.cepsacbackend.exception.ResourceNotFoundException;
import com.example.cepsacbackend.model.Pais;
import com.example.cepsacbackend.repository.PaisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaisService {

    private final PaisRepository paisRepository;

    @Transactional(readOnly = true)
    public List<Pais> getAllPaises() {
        return paisRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pais getPaisById(Short idPais) {
        return paisRepository.findById(idPais)
                .orElseThrow(() -> new ResourceNotFoundException("País con ID " + idPais + " no encontrado."));
    }

    @Transactional
    public Pais createPais(Pais pais) {
        if (pais.getIdPais() != null) {
            throw new BadRequestException("No se debe incluir el ID para crear un nuevo país.");
        }
        if (paisRepository.findByNombre(pais.getNombre()).isPresent()) {
            throw new ConflictException("Ya existe un país con el nombre '" + pais.getNombre() + "'.");
        }
        return paisRepository.save(pais);
    }

    @Transactional
    public Pais updatePais(Short idPais, Pais paisDetails) {
        Pais paisExistente = getPaisById(idPais);
        paisRepository.findByNombre(paisDetails.getNombre()).ifPresent(paisEncontrado -> {
            if (!paisEncontrado.getIdPais().equals(idPais)) {
                throw new ConflictException("El nombre '" + paisDetails.getNombre() + "' ya está en uso por otro país.");
            }
        });

        paisExistente.setNombre(paisDetails.getNombre());
        return paisRepository.save(paisExistente);
    }

    @Transactional
    public void deletePais(Short idPais) {
        if (!paisRepository.existsById(idPais)) {
            throw new ResourceNotFoundException("País con ID " + idPais + " no encontrado, no se puede eliminar.");
        }
        paisRepository.deleteById(idPais);
    }
}
