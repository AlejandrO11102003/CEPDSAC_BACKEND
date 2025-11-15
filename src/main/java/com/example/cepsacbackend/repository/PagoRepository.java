package com.example.cepsacbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cepsacbackend.model.Pago;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
    List<Pago> findByMatriculaIdMatricula(Integer idMatricula);
    //obtenemos el num max x cuota en una matriucla
    @Query("SELECT MAX(p.numeroCuota) FROM Pago p WHERE p.matricula.idMatricula = :idMatricula")
    Integer findMaxNumeroCuotaByMatriculaId(@Param("idMatricula") Integer idMatricula);
    // traemos la matricula con su lista de pagos en una sola N
    @Query("SELECT p FROM Pago p " +
            "JOIN FETCH p.matricula m " +
            "WHERE m.idMatricula = :idMatricula")
    List<Pago> findPagosConMatricula(@Param("idMatricula") Integer idMatricula);
}