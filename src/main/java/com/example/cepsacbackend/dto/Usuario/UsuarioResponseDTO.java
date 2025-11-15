package com.example.cepsacbackend.dto.Usuario;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.example.cepsacbackend.enums.Rol;

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
    private boolean activo;
    private String numeroCelular;
    private String numeroIdentificacion;
    private Short idPais;
    private String nombrePais;
    private String codigoTelefono;
    private Short idTipoIdentificacion;
    private String inicialesTipoIdentificacion;

}