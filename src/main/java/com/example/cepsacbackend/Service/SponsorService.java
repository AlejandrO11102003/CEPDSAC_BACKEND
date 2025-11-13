package com.example.cepsacbackend.service;

import com.example.cepsacbackend.model.Sponsor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SponsorService {
    List<Sponsor> listar();
    Sponsor obtenerPorId(Integer id);
    Sponsor crear(String nombre, MultipartFile imagen);
    Sponsor actualizar(Integer id, String nombre, MultipartFile imagen);
    void eliminar(Integer id);
}
