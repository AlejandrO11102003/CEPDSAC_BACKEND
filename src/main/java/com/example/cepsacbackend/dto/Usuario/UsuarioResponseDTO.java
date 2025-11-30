package com.example.cepsacbackend.dto.Usuario;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.example.cepsacbackend.enums.Rol;
import com.example.cepsacbackend.enums.EstadoUsuario;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Integer idUsuario;
    private Rol rol;
    private String nombre;
    private String apellido;
    private String correo;
    private EstadoUsuario estado;
    private String numeroCelular;
    private String numeroIdentificacion;
    private Short idPais;
    private String nombrePais;
    private String codigoTelefono;
    private Short idTipoIdentificacion;
    private String inicialesTipoIdentificacion;

}