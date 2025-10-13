package com.example.cepsacbackend.Service;

import java.util.List;

import com.example.cepsacbackend.Dto.Usuario.UsuarioCreateDTO;
import com.example.cepsacbackend.Dto.Usuario.UsuarioListResponseDTO;
import com.example.cepsacbackend.Dto.Usuario.UsuarioPatchDTO;
import com.example.cepsacbackend.Dto.Usuario.UsuarioResponseDTO;
import com.example.cepsacbackend.Dto.Usuario.UsuarioUpdateDTO;
import com.example.cepsacbackend.Enums.Rol;

public interface UsuarioService {

    List<UsuarioListResponseDTO> listarUsuarios();
    UsuarioResponseDTO obtenerUsuario(Integer idUsuario);
    UsuarioResponseDTO crearUsuario(UsuarioCreateDTO dto);
    UsuarioResponseDTO actualizarUsuario(UsuarioUpdateDTO dto);
    UsuarioResponseDTO actualizarUsuarioParcialmente(UsuarioPatchDTO dto);
    void eliminarUsuario(Integer idUsuario);

    UsuarioResponseDTO restaurarUsuario(Integer idUsuario);
    List<UsuarioListResponseDTO> listarUsuariosPorRol(Rol rol);

}