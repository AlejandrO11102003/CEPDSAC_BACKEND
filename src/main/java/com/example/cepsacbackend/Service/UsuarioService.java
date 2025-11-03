package com.example.cepsacbackend.service;

import java.util.List;

import com.example.cepsacbackend.dto.Usuario.UsuarioCreateDTO;
import com.example.cepsacbackend.dto.Usuario.UsuarioListResponseDTO;
import com.example.cepsacbackend.dto.Usuario.UsuarioPatchDTO;
import com.example.cepsacbackend.dto.Usuario.UsuarioResponseDTO;
import com.example.cepsacbackend.dto.Usuario.UsuarioUpdateDTO;
import com.example.cepsacbackend.enums.Rol;

public interface UsuarioService {

    List<UsuarioListResponseDTO> listarUsuarios();
    UsuarioResponseDTO obtenerUsuario(Integer idUsuario);
    UsuarioResponseDTO crearUsuario(UsuarioCreateDTO dto);
    UsuarioResponseDTO actualizarUsuario(Integer idUsuario, UsuarioUpdateDTO dto);
    UsuarioResponseDTO actualizarUsuarioParcialmente(Integer idUsuario, UsuarioPatchDTO dto);
    void eliminarUsuario(Integer idUsuario);
    UsuarioResponseDTO restaurarUsuario(Integer idUsuario);
    List<UsuarioListResponseDTO> listarUsuariosPorRol(Rol rol);

}