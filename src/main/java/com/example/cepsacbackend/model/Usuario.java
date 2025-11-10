package com.example.cepsacbackend.model;

import com.example.cepsacbackend.auditory.AuditoriaListener;
import com.example.cepsacbackend.enums.EstadoUsuario;
import com.example.cepsacbackend.enums.Rol;
import jakarta.persistence.*;
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
@Table(name = "usuario", indexes = {
    @Index(name = "idx_usuario_correo", columnList = "Correo", unique = true),
    @Index(name = "idx_usuario_estado", columnList = "Estado"),
    @Index(name = "idx_usuario_estado_rol", columnList = "Estado,Rol")
})
public class Usuario extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUsuario", columnDefinition = "SMALLINT UNSIGNED")
    private Integer idUsuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "Rol")
    private Rol rol;

    @Column(name = "Nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "Apellido", length = 50)
    private String apellido;

    @Column(name = "Correo", length = 255)
    private String correo;

    @Column(name = "Password", length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado")
    private EstadoUsuario estado = EstadoUsuario.ACTIVO;

    @Column(name = "NumeroCelular", length = 15)
    private String numeroCelular;

    @Column(name = "NumeroIdentificacion", length = 20)
    private String numeroIdentificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCodigoPais")
    private Pais pais;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdTipoIdentificacion")
    private TipoIdentificacion tipoIdentificacion;
}