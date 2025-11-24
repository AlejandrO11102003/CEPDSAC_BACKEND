package com.example.cepsacbackend.service.impl;

import com.example.cepsacbackend.dto.MetodoPago.MetodoPagoRequestDTO;
import com.example.cepsacbackend.exception.ResourceNotFoundException;
import com.example.cepsacbackend.mapper.MetodoPagoMapper;
import com.example.cepsacbackend.model.MetodoPago;
import com.example.cepsacbackend.repository.MetodoPagoRepository;
import com.example.cepsacbackend.service.CloudinaryService;
import com.example.cepsacbackend.service.MetodoPagoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetodoPagoServiceImpl implements MetodoPagoService {

    private final MetodoPagoRepository metodorepo;
    private final MetodoPagoMapper metodoPagoMapper;
    private final CloudinaryService cloudinaryService;

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
        
        // delete img cloudinary
        String imagenAnterior = metodoExistente.getImagenQR();
        String imagenNueva = dto.getImagenQR();
        
        if (imagenAnterior != null && !imagenAnterior.isEmpty() 
            && imagenNueva != null && !imagenAnterior.equals(imagenNueva)) {
            String publicId = cloudinaryService.extractPublicId(imagenAnterior);
            if (publicId != null) {
                try {
                    cloudinaryService.delete(publicId);
                } catch (IOException e) {
                    metodoPagoMapper.updateEntityFromRequestDTO(dto, metodoExistente);
                    metodorepo.save(metodoExistente);
                    throw new com.example.cepsacbackend.exception.WarningException(
                        "Método de pago actualizado correctamente, pero no se pudo eliminar la imagen anterior de Cloudinary.");
                }
            }
        }
        
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
        
        // delete img cloudinary
        String imagenQR = metodoExistente.getImagenQR();
        if (imagenQR != null && !imagenQR.isEmpty()) {
            String publicId = cloudinaryService.extractPublicId(imagenQR);
            if (publicId != null) {
                try {
                    cloudinaryService.delete(publicId);
                } catch (IOException e) {
                    metodorepo.delete(metodoExistente);
                    throw new com.example.cepsacbackend.exception.WarningException(
                        "Método de pago eliminado correctamente, pero no se pudo eliminar la imagen de Cloudinary.");
                }
            }
        }
        
        metodorepo.delete(metodoExistente);
    }
}
