package com.example.cepsacbackend.Dto.Usuario;

import com.example.cepsacbackend.Enums.Rol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioListResponseDTO {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private Rol rol;
    private boolean activo;
}