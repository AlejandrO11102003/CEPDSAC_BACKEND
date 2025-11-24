package com.example.cepsacbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "configuracion_contacto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionContacto {

    @Id
    @Column(name = "IdConfiguracion")
    private Integer idConfiguracion;

    @Column(name = "CorreoContacto", length = 255)
    private String correoContacto;

    @Column(name = "Telefono", length = 20)
    private String telefono;

    @Column(name = "Whatsapp", length = 20)
    private String whatsapp;

    @Column(name = "Direccion", length = 500)
    private String direccion;

    @Column(name = "Facebook", length = 255)
    private String facebook;

    @Column(name = "Instagram", length = 255)
    private String instagram;

    @Column(name = "Linkedin", length = 255)
    private String linkedin;

    @Column(name = "Twitter", length = 255)
    private String twitter;
}
