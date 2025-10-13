package com.example.cepsacbackend.Entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdPago", columnDefinition = "SMALLINT UNSIGNED")
    private Integer idPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdMatricula")
    private Matricula matricula; // FK a Matricula

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdMetodoPago")
    private MetodoPago metodoPago; // FK a MetodoPago

    @Column(name = "Monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "NumeroCuota" , columnDefinition = "TINYINT UNSIGNED")
    private Short numeroCuota;

    @Column(name = "FechaPago", updatable = false)
    private LocalDateTime fechaPago = LocalDateTime.now();

    // Admin que registro el pago
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdUsuario")
    private Usuario usuarioRegistro;
}