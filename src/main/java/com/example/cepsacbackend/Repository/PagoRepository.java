package com.example.cepsacbackend.Repository;

import com.example.cepsacbackend.Entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
    List<Pago> findByMatriculaIdMatricula(Integer idMatricula);
    // traemos la matricula con su lista de pagos en una sola N
    @Query("SELECT p FROM Pago p " +
            "JOIN FETCH p.matricula m " +
            "WHERE m.idMatricula = :idMatricula")
    List<Pago> findPagosConMatricula(@Param("idMatricula") Integer idMatricula);
}