package com.example.cepsacbackend.Service;

import com.example.cepsacbackend.Dto.MetodoPago.MetodoPagoRequestDTO;
import com.example.cepsacbackend.Entity.MetodoPago;
import com.example.cepsacbackend.Mapper.MetodoPagoMapper;
import com.example.cepsacbackend.Repository.MetodoPagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetodoPagoService {

    private final MetodoPagoRepository metodorepo;
    private final MetodoPagoMapper metodoPagoMapper;

    public List<MetodoPago> obtenerActivos() {
        return metodorepo.findByActivoTrue();
    }

    public List<MetodoPago> obtenerTodos() {
        return metodorepo.findAll();
    }

    // obtener por id de metodopago
    public MetodoPago obtenerPorId(Short id) {
        return metodorepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Método de pago no encontrado con ID: " + id));
    }

    @Transactional
    public MetodoPago crearMetodo(MetodoPagoRequestDTO dto) {
        MetodoPago metodo = metodoPagoMapper.toEntity(dto);
        return metodorepo.save(metodo);
    }

    @Transactional
    public MetodoPago actualizarMetodo(Short id, MetodoPagoRequestDTO dto) {
        MetodoPago metodoExistente = obtenerPorId(id);
        metodoPagoMapper.updateEntityFromRequestDTO(dto, metodoExistente);
        return metodorepo.save(metodoExistente);
    }

    @Transactional
    public MetodoPago cambiarEstado(Short id, Boolean nuevoEstado) {
        MetodoPago metodoExistente = obtenerPorId(id);
        if (metodoExistente.getActivo().equals(nuevoEstado)) {
            return metodoExistente;
        }
        metodoExistente.setActivo(nuevoEstado);
        return metodorepo.save(metodoExistente);
    }

    @Transactional
    public void eliminarMetodo(Short id) {
        MetodoPago metodoExistente = obtenerPorId(id);
        metodorepo.delete(metodoExistente);
    }
}