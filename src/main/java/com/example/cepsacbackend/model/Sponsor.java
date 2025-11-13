package com.example.cepsacbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.example.cepsacbackend.auditory.AuditoriaListener;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "Sponsor")
@EntityListeners(AuditoriaListener.class)
public class Sponsor extends AuditableEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdSponsor", columnDefinition = "SMALLINT UNSIGNED")
    private Integer idSponsor;

    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "RutaImagen", nullable = false, length = 255)
    private String rutaImagen;
}
