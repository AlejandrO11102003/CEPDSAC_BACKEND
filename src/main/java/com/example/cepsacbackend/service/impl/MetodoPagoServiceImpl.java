package com.example.cepsacbackend.service.impl;

import com.example.cepsacbackend.dto.MetodoPago.MetodoPagoRequestDTO;
import com.example.cepsacbackend.exception.ResourceNotFoundException;
import com.example.cepsacbackend.mapper.MetodoPagoMapper;
import com.example.cepsacbackend.model.MetodoPago;
import com.example.cepsacbackend.repository.MetodoPagoRepository;
import com.example.cepsacbackend.service.MetodoPagoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetodoPagoServiceImpl implements MetodoPagoService {

    private final MetodoPagoRepository metodorepo;
    private final MetodoPagoMapper metodoPagoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<MetodoPago> obtenerActivos() {
        return metodorepo.findByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MetodoPago> obtenerTodos() {
        return metodorepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public MetodoPago obtenerPorId(Short id) {
        return metodorepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("No se encontró el método de pago con ID %d. Verifique que el ID sea correcto.", id)));
    }

    @Override
    @Transactional
    public MetodoPago crearMetodo(MetodoPagoRequestDTO dto) {
        MetodoPago metodo = metodoPagoMapper.toEntity(dto);
        return metodorepo.save(metodo);
    }

    @Override
    @Transactional
    public MetodoPago actualizarMetodo(Short id, MetodoPagoRequestDTO dto) {
        MetodoPago metodoExistente = obtenerPorId(id);
        metodoPagoMapper.updateEntityFromRequestDTO(dto, metodoExistente);
        return metodorepo.save(metodoExistente);
    }

    @Override
    @Transactional
    public MetodoPago cambiarEstado(Short id, Boolean nuevoEstado) {
        MetodoPago metodoExistente = obtenerPorId(id);
        if (metodoExistente.getActivo().equals(nuevoEstado)) {
            return metodoExistente;
        }
        metodoExistente.setActivo(nuevoEstado);
        return metodorepo.save(metodoExistente);
    }

    @Override
    @Transactional
    public void eliminarMetodo(Short id) {
        MetodoPago metodoExistente = obtenerPorId(id);
        metodorepo.delete(metodoExistente);
    }
}
