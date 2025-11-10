package com.example.cepsacbackend.service.impl;

import com.example.cepsacbackend.dto.Descuento.DescuentoCreateDTO;
import com.example.cepsacbackend.dto.Descuento.DescuentoResponseDTO;
import com.example.cepsacbackend.dto.Descuento.DescuentoUpdateDTO;
import com.example.cepsacbackend.exception.ResourceNotFoundException;
import com.example.cepsacbackend.model.Descuento;
import com.example.cepsacbackend.repository.DescuentoRepository;
import com.example.cepsacbackend.service.DescuentoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DescuentoServiceImpl implements DescuentoService {

    private final DescuentoRepository repo;

    private DescuentoResponseDTO toDTO(Descuento d) {
        DescuentoResponseDTO dto = new DescuentoResponseDTO();
        dto.setIdDescuento(d.getIdDescuento());
        dto.setTipoDescuento(d.getTipoDescuento());
        dto.setValor(d.getValor());
        dto.setVigente(d.getVigente());
        dto.setFechaInicio(d.getFechaInicio());
        dto.setFechaFin(d.getFechaFin());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DescuentoResponseDTO> listar() {
        List<Descuento> lista = repo.findAll();
        return lista.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DescuentoResponseDTO obtener(Short idDescuento) {
        Descuento d = repo.findById(idDescuento)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("No se encontró el descuento con ID %d. Verifique que el ID sea correcto.", idDescuento)));
        return toDTO(d);
    }

    @Override
    @Transactional
    public DescuentoResponseDTO crear(DescuentoCreateDTO dto) {
        Descuento d = new Descuento();
        // set basicos
        d.setTipoDescuento(dto.getTipoDescuento());
        d.setValor(dto.getValor());
        d.setVigente(dto.getVigente() == null ? Boolean.TRUE : dto.getVigente());
        d.setFechaInicio(dto.getFechaInicio());
        d.setFechaFin(dto.getFechaFin());        
        Descuento guardado = repo.save(d);
        return toDTO(guardado);
    }

    @Override
    @Transactional
    public DescuentoResponseDTO actualizar(DescuentoUpdateDTO dto) {
        Descuento d = repo.findById(dto.getIdDescuento())
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("No se puede actualizar. El descuento con ID %d no existe.", dto.getIdDescuento())));
        //actualizo los campos del descuento con los nuevos valores
        d.setTipoDescuento(dto.getTipoDescuento());
        d.setValor(dto.getValor());
        d.setVigente(dto.getVigente());
        d.setFechaInicio(dto.getFechaInicio());
        d.setFechaFin(dto.getFechaFin());
        Descuento actualizado = repo.save(d);
        return toDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Short idDescuento) {
        // borrado fisico simple acorde a fk on delete cascade en aplicaciones
        if (!repo.existsById(idDescuento)) {
            throw new ResourceNotFoundException(
                String.format("No se puede eliminar. El descuento con ID %d no existe.", idDescuento));
        }
        repo.deleteById(idDescuento);
    }
}
