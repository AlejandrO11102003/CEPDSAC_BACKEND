package com.example.cepsacbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_modificador")
    private Usuario usuarioModificador;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;
}