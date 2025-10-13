package com.example.cepsacbackend.Service.Impl;

import com.example.cepsacbackend.Dto.Descuento.DescuentoCreateDTO;
import com.example.cepsacbackend.Dto.Descuento.DescuentoResponseDTO;
import com.example.cepsacbackend.Dto.Descuento.DescuentoUpdateDTO;
import com.example.cepsacbackend.Entity.Descuento;
import com.example.cepsacbackend.Entity.Usuario;
import com.example.cepsacbackend.Repository.DescuentoRepository;
import com.example.cepsacbackend.Repository.UsuarioRepository;
import com.example.cepsacbackend.Service.DescuentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DescuentoServiceImpl implements DescuentoService {

    private final DescuentoRepository repo;
    private final UsuarioRepository repoUsuario;

    private DescuentoResponseDTO toDTO(Descuento d) {
        DescuentoResponseDTO dto = new DescuentoResponseDTO();
        dto.setIdDescuento(d.getIdDescuento());
        dto.setTipoDescuento(d.getTipoDescuento());
        dto.setValor(d.getValor());
        dto.setVigente(d.getVigente());
        dto.setFechaInicio(d.getFechaInicio());
        dto.setFechaFin(d.getFechaFin());
        dto.setIdUsuario(d.getUsuario() != null ? d.getUsuario().getIdUsuario() : null);
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
                .orElseThrow(() -> new RuntimeException("Descuento no encontrado: " + idDescuento));
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
        // set usuario creador si viene id
        if (dto.getIdUsuario() != null) {
            Usuario u = repoUsuario.findById(dto.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + dto.getIdUsuario()));
            d.setUsuario(u);
        }
        Descuento guardado = repo.save(d);
        return toDTO(guardado);
    }

    @Override
    @Transactional
    public DescuentoResponseDTO actualizar(DescuentoUpdateDTO dto) {
        Descuento d = repo.findById(dto.getIdDescuento())
                .orElseThrow(() -> new RuntimeException("Descuento no encontrado: " + dto.getIdDescuento()));
        // actualizar campos
        d.setTipoDescuento(dto.getTipoDescuento());
        d.setValor(dto.getValor());
        d.setVigente(dto.getVigente());
        d.setFechaInicio(dto.getFechaInicio());
        d.setFechaFin(dto.getFechaFin());
        // no cambiamos creador, pero si llega un usuario válido lo referenciamos
        if (dto.getIdUsuario() != null) {
            Usuario u = repoUsuario.findById(dto.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + dto.getIdUsuario()));
            d.setUsuario(u);
        }
        Descuento actualizado = repo.save(d);
        return toDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Short idDescuento) {
        // borrado fisico simple acorde a fk on delete cascade en aplicaciones
        if (!repo.existsById(idDescuento)) {
            throw new RuntimeException("Descuento no encontrado: " + idDescuento);
        }
        repo.deleteById(idDescuento);
    }
}
