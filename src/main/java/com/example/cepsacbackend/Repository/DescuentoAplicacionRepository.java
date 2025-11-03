package com.example.cepsacbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.cepsacbackend.model.Descuento;
import com.example.cepsacbackend.model.DescuentoAplicacion;

import java.util.List;

@Repository
public interface DescuentoAplicacionRepository extends JpaRepository<DescuentoAplicacion, Integer> {

    // buscamos descuentos vigentes que apliquen a un curso o categoría 
    // o que sean generales, ordenados por valor descendente
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
                OR (da.tipoAplicacion = 'MATRICULA' AND da.matricula.idMatricula = :idMatricula)
            )
            ORDER BY 
                CASE 
                    WHEN da.tipoAplicacion = 'MATRICULA' THEN 1
                    WHEN da.tipoAplicacion = 'CURSO' THEN 2
                    WHEN da.tipoAplicacion = 'CATEGORIA' THEN 3
                    ELSE 4
                END,
                d.valor DESC
        """)
    List<Descuento> findDescuentosVigentes(@Param("idCurso") Short idCurso, @Param("idCategoria") Short idCategoria, @Param("idMatricula") Integer idMatricula);
}
