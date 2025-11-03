package com.example.cepsacbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.example.cepsacbackend.auditory.AuditoriaListener;
import com.example.cepsacbackend.enums.TipoDescuento;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditoriaListener.class)
public class Descuento extends AuditableEntity { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdDescuento")
    private Short idDescuento;

    @Enumerated(EnumType.STRING)
    @Column(name = "TipoDescuento", nullable = false)
    private TipoDescuento tipoDescuento;

    @Column(name = "Valor", precision = 8, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(name = "Vigente")
    private Boolean vigente = true;

    @Column(name = "FechaInicio")
    private LocalDate fechaInicio;

    @Column(name = "FechaFin")
    private LocalDate fechaFin;

}
