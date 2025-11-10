package com.example.cepsacbackend.model;

import com.example.cepsacbackend.auditory.AuditoriaListener;
import com.example.cepsacbackend.enums.TipoAplicacionDescuento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditoriaListener.class)
@Table(name = "DescuentoAplicacion", indexes = {
    @Index(name = "idx_descuento_aplicacion_tipo", columnList = "TipoAplicacion"),
    @Index(name = "idx_descuento_aplicacion_curso", columnList = "IdCursoDiplomado")
})
public class DescuentoAplicacion extends AuditableEntity {

    @Id    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdDescuentoAplicacion")
    private Integer idDescuentoAplicacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdDescuento", nullable = false)
    private Descuento descuento;

    @Enumerated(EnumType.STRING)
    @Column(name = "TipoAplicacion", nullable = false)
    private TipoAplicacionDescuento tipoAplicacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCategoria")
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCursoDiplomado")
    private CursoDiplomado cursoDiplomado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdUsuario")
    private Usuario usuario; //registra la aplicacion de descuento

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdMatricula")
    private Matricula matricula;
}