package com.example.cepsacbackend.service.impl;

import com.example.cepsacbackend.exception.BadRequestException;
import com.example.cepsacbackend.exception.ResourceNotFoundException;
import com.example.cepsacbackend.model.Sponsor;
import com.example.cepsacbackend.repository.SponsorRepository;
import com.example.cepsacbackend.service.SponsorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SponsorServiceImpl implements SponsorService {

    private final SponsorRepository repository;

    @Value("${app.upload.dir:src/main/resources/static/images/sponsors}")
    private String uploadDir;

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
        // Guardar imagen y obtener ruta
        String rutaImagen = guardarImagen(imagen);

        Sponsor entity = new Sponsor();
        entity.setNombre(nombre);
        entity.setRutaImagen(rutaImagen);
        entity.setFechaModificacion(LocalDateTime.now());

        Sponsor guardado = repository.save(entity);
        log.info("Sponsor creado con ID: {} y ruta: {}", guardado.getIdSponsor(), rutaImagen);
        
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
        // si hay nueva imagen, eliminar la anterior y guardar la nueva
        if (imagen != null && !imagen.isEmpty()) {
            validarTipoImagen(imagen);
            eliminarImagenFisica(entity.getRutaImagen());
            String nuevaRuta = guardarImagen(imagen);
            entity.setRutaImagen(nuevaRuta);
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

        eliminarImagenFisica(entity.getRutaImagen());
        repository.deleteById(id);
        log.info("Sponsor eliminado con ID: {}", id);
    }

    //guardado fisico de la imagen
    private String guardarImagen(MultipartFile imagen) {
        try {
            // Crear directorio si no existe
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // Generar nombre unico
            String nombreOriginal = imagen.getOriginalFilename();
            String extension = nombreOriginal != null && nombreOriginal.contains(".") 
                    ? nombreOriginal.substring(nombreOriginal.lastIndexOf("."))
                    : ".png";
            String nombreArchivo = UUID.randomUUID().toString() + extension;
            Path rutaDestino = uploadPath.resolve(nombreArchivo);
            Files.copy(imagen.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
            // devuelve ruta para la bd
            return "/images/sponsors/" + nombreArchivo;

        } catch (IOException e) {
            log.error("Error al guardar imagen: {}", e.getMessage());
            throw new BadRequestException("Error al guardar la imagen: " + e.getMessage());
        }
    }

    private void eliminarImagenFisica(String rutaImagen) {
        try {
            if (rutaImagen == null || rutaImagen.isBlank()) {
                return;
            }

            // Extraer nombre de archivo de la ruta
            String nombreArchivo = rutaImagen.substring(rutaImagen.lastIndexOf("/") + 1);
            Path rutaArchivo = Paths.get(uploadDir, nombreArchivo);
            if (Files.exists(rutaArchivo)) {
                Files.delete(rutaArchivo);
                log.info("Imagen eliminada: {}", rutaArchivo);
            }
        } catch (IOException e) {
            log.error("Error al eliminar imagen física: {}", e.getMessage());
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
