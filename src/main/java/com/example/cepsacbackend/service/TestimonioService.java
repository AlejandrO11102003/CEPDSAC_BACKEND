package com.example.cepsacbackend.service;

import java.util.List;
import java.util.Optional;

import com.example.cepsacbackend.model.Testimonio;

public interface TestimonioService {

    List<Testimonio> findAll();

    Optional<Testimonio> findById(Integer id);

    Testimonio create(Testimonio testimonio);

    Testimonio update(Integer id, Testimonio testimonio);

    void delete(Integer id);

    void aprobarTestimonio(Integer idTestimonio, Boolean estadoAprobado);

}
