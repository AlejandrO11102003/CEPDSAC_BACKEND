package com.example.cepsacbackend.model;

import com.example.cepsacbackend.auditory.AuditoriaListener;
import com.example.cepsacbackend.enums.EstadoCuota;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

// representa una cuota o pago asociado a una matricula
// puede ser cuota automatica (generada por duracionmeses) o manual (creada por admin)
// registra el estado de cada cuota: pendiente, pagada, vencida, etc
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditoriaListener.class)
@Table(name = "pago", indexes = {
    @Index(name = "idx_pago_matricula", columnList = "IdMatricula")
})
public class Pago extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdPago", columnDefinition = "SMALLINT UNSIGNED")
    private Integer idPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdMatricula")
    private Matricula matricula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdMetodoPago")
    private MetodoPago metodoPago;

    @Column(name = "Monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "NumeroCuota" , columnDefinition = "TINYINT UNSIGNED")
    private Short numeroCuota;

    @Column(name = "FechaPago")
    private LocalDateTime fechaPago;
    
    @Column(name = "FechaVencimiento")
    private java.time.LocalDate fechaVencimiento;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "EstadoCuota", length = 20)
    private EstadoCuota estadoCuota;
    
    @Column(name = "MontoPagado", precision = 10, scale = 2)
    private BigDecimal montoPagado;
    
    @Column(name = "EsAutomatico")
    private Boolean esAutomatico = false;

    @Column(name = "NumeroOperacion", length = 50)
    private String numeroOperacion;

    @Column(name = "Observaciones", columnDefinition = "TEXT")
    private String observaciones;

}