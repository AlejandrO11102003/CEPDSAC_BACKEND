package com.example.cepsacbackend.service.impl;

import com.example.cepsacbackend.exception.BadRequestException;
import com.example.cepsacbackend.exception.ResourceNotFoundException;
import com.example.cepsacbackend.model.Sponsor;
import com.example.cepsacbackend.repository.SponsorRepository;
import com.example.cepsacbackend.service.CloudinaryService;
import com.example.cepsacbackend.service.SponsorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SponsorServiceImpl implements SponsorService {

    private final SponsorRepository repository;
    private final CloudinaryService cloudinaryService;

    private static final String CLOUDINARY_FOLDER = "sponsors/logos";

    @Override
    @Transactional(readOnly = true)
    public List<Sponsor> listar() {
        return repository.findAllByOrderByIdSponsorAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public Sponsor obtenerPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("No se encontró el sponsor con ID %d", id)));
    }

    @Override
    @Transactional
    public Sponsor crear(String nombre, MultipartFile imagen) {
        if (nombre == null || nombre.isBlank()) {
            throw new BadRequestException("El nombre es requerido");
        }
        
        if (imagen == null || imagen.isEmpty()) {
            throw new BadRequestException("La imagen es requerida");
        }
        
        validarTipoImagen(imagen);
        // subir img
        String urlImagen = subirImagenCloudinary(imagen);
        Sponsor entity = new Sponsor();
        entity.setNombre(nombre);
        entity.setRutaImagen(urlImagen);
        entity.setFechaModificacion(LocalDateTime.now());

        Sponsor guardado = repository.save(entity);
        log.info("Sponsor creado con ID: {} y URL: {}", guardado.getIdSponsor(), urlImagen);
        
        return guardado;
    }

    @Override
    @Transactional
    public Sponsor actualizar(Integer id, String nombre, MultipartFile imagen) {
        Sponsor entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("No se encontró el sponsor con ID %d", id)));
        if (nombre != null && !nombre.isBlank()) {
            entity.setNombre(nombre);
        }
        // si hay img, delete img cloudinary
        if (imagen != null && !imagen.isEmpty()) {
            validarTipoImagen(imagen);
            String imagenAnterior = entity.getRutaImagen();
            if (imagenAnterior != null && !imagenAnterior.isEmpty()) {
                eliminarImagenCloudinary(imagenAnterior);
            }
            String nuevaUrl = subirImagenCloudinary(imagen);
            entity.setRutaImagen(nuevaUrl);
        }

        entity.setFechaModificacion(LocalDateTime.now());
        Sponsor actualizado = repository.save(entity);
        log.info("Sponsor actualizado con ID: {}", id);
        return actualizado;
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        Sponsor entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("No se puede eliminar. El sponsor con ID %d no existe", id)));

        //delete cloudinary
        String rutaImagen = entity.getRutaImagen();
        if (rutaImagen != null && !rutaImagen.isEmpty()) {
            eliminarImagenCloudinary(rutaImagen);
        }
        
        repository.deleteById(id);
        log.info("Sponsor eliminado con ID: {}", id);
    }

    private String subirImagenCloudinary(MultipartFile imagen) {
        try {
            Map<String, Object> uploadResult = cloudinaryService.upload(imagen, CLOUDINARY_FOLDER);
            String imageUrl = (String) uploadResult.get("secure_url");
            log.info("Imagen de sponsor subida a Cloudinary: {}", imageUrl);
            return imageUrl;
        } catch (IOException e) {
            log.error("Error al subir imagen a Cloudinary: {}", e.getMessage());
            throw new BadRequestException("Error al subir la imagen: " + e.getMessage());
        }
    }

    private void eliminarImagenCloudinary(String url) {
        try {
            String publicId = cloudinaryService.extractPublicId(url);
            if (publicId != null) {
                cloudinaryService.delete(publicId);
                log.info("Imagen de sponsor eliminada de Cloudinary: {}", publicId);
            }
        } catch (IOException e) {
            log.warn("No se pudo eliminar la imagen de Cloudinary: {}", e.getMessage());
        }
    }

    private void validarTipoImagen(MultipartFile archivo) {
        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadRequestException("El archivo debe ser una imagen (JPG, PNG, GIF, etc.)");
        }
        // validar size 5 max
        long maxSize = 5 * 1024 * 1024; // 5MB
        if (archivo.getSize() > maxSize) {
            throw new BadRequestException("La imagen no debe superar los 5MB");
        }
    }
}
