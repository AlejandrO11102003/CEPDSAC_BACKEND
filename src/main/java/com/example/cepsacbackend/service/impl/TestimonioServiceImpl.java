package com.example.cepsacbackend.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cepsacbackend.model.Testimonio;
import com.example.cepsacbackend.repository.TestimonioRepository;
import com.example.cepsacbackend.service.TestimonioService;
import lombok.extern.slf4j.Slf4j;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j  
public class TestimonioServiceImpl implements TestimonioService {

    private final TestimonioRepository testimonioRepository;

    @Override
    public List<Testimonio> findAll() {
        return testimonioRepository.findAll();
    }

    @Override
    public Optional<Testimonio> findById(Integer id) {
        return testimonioRepository.findById(id);
    }

    @Override
    public Testimonio create(Testimonio testimonio) {
        testimonio.setIdTestimonio(null);
        return testimonioRepository.save(testimonio);
    }

    @Override
    public Testimonio update(Integer id, Testimonio testimonio) {
        Optional<Testimonio> opt = testimonioRepository.findById(id);
        if (!opt.isPresent()) {
            throw new IllegalArgumentException("Testimonio no encontrado: " + id);
        }
        Testimonio existing = opt.get();
        existing.setComentario(testimonio.getComentario());
        existing.setEstadoAprobado(testimonio.getEstadoAprobado());
        existing.setIdUsuario(testimonio.getIdUsuario());
        return testimonioRepository.save(existing);
    }

    @Override
    public void delete(Integer id) {
        testimonioRepository.deleteById(id);
    }

    //aprobartestimonio
    @Override
    public void aprobarTestimonio(Integer idTestimonio, Boolean estadoAprobado) {
        Optional<Testimonio> opt = testimonioRepository.findById(idTestimonio);
        if (!opt.isPresent()) {
            throw new IllegalArgumentException("Testimonio no encontrado: " + idTestimonio);
        }
        Testimonio testimonio = opt.get();
        testimonio.setEstadoAprobado(estadoAprobado);
        testimonioRepository.save(testimonio);
    }
    

}
