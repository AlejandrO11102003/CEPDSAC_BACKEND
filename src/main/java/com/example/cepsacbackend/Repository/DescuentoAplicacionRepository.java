package com.example.cepsacbackend.Repository;

import com.example.cepsacbackend.Entity.Descuento;
import com.example.cepsacbackend.Entity.DescuentoAplicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DescuentoAplicacionRepository extends JpaRepository<DescuentoAplicacion, Integer> {

    @Query("""
        SELECT DISTINCT d FROM DescuentoAplicacion da
        JOIN da.descuento d
        WHERE d.vigente = true
            AND (d.fechaInicio IS NULL OR d.fechaInicio <= CURRENT_DATE)
            AND (d.fechaFin IS NULL OR d.fechaFin >= CURRENT_DATE)
            AND (
                    da.tipoAplicacion = 'GENERAL'
                OR (da.tipoAplicacion = 'CURSO' AND da.cursoDiplomado.idCursoDiplomado = :idCurso)
                OR (da.tipoAplicacion = 'CATEGORIA' AND da.categoria.idCategoria = :idCategoria)
            )
            ORDER BY d.valor DESC
        """)
    List<Descuento> findDescuentosVigentes(@Param("idCurso") Short idCurso, @Param("idCategoria") Short idCategoria);
}
