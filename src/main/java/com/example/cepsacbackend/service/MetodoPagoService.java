package com.example.cepsacbackend.service;

import com.example.cepsacbackend.dto.MetodoPago.MetodoPagoRequestDTO;
import com.example.cepsacbackend.model.MetodoPago;

import java.util.List;

public interface MetodoPagoService {

    List<MetodoPago> obtenerActivos();

    List<MetodoPago> obtenerTodos();

    MetodoPago obtenerPorId(Short id);

    MetodoPago crearMetodo(MetodoPagoRequestDTO dto);

    MetodoPago actualizarMetodo(Short id, MetodoPagoRequestDTO dto);

    MetodoPago cambiarEstado(Short id, Boolean nuevoEstado);

    void eliminarMetodo(Short id);
}
