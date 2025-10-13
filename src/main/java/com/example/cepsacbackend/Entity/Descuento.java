package com.example.cepsacbackend.Entity;

import com.example.cepsacbackend.Enums.TipoDescuento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Descuento {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdUsuario")
    private Usuario usuario;
}
