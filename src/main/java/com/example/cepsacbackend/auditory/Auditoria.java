package com.example.cepsacbackend.auditory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
@Getter
@Setter
@NoArgsConstructor
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entidad", nullable = false)
    private String entidad;

    @Column(name = "entidad_id")
    private String entidadId;

    @Enumerated(EnumType.STRING)
    @Column(name = "operacion", nullable = false)
    private OperacionAuditoria operacion;

    @Column(name = "usuario_id")
    private Integer usuarioId;

    @CreationTimestamp
    @Column(name = "fecha", updatable = false)
    private LocalDateTime fecha;

    @Column(name = "valores_anteriores", columnDefinition = "JSON")
    private String valoresAnteriores;

    @Column(name = "valores_nuevos", columnDefinition = "JSON")
    private String valoresNuevos;
}