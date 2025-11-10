package com.example.cepsacbackend.service;

import com.example.cepsacbackend.model.Pais;

import java.util.List;

public interface PaisService {

    List<Pais> getAllPaises();

    Pais getPaisById(Short idPais);

    Pais createPais(Pais pais);

    Pais updatePais(Short idPais, Pais paisDetails);

    void deletePais(Short idPais);
}
