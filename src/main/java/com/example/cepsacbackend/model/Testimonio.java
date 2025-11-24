package com.example.cepsacbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// entidad para almacenar testimonios de usuarios sobre los cursos o la plataforma

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Testimonio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTestimonio;

    @Column(name= "Comentario", length = 500)
    private String comentario;

    @Column(name= "EstadoAprobado")
    private Boolean estadoAprobado; //0: desaprobado, 1:aprobado

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdUsuario")
    private Usuario idUsuario;
    
}
