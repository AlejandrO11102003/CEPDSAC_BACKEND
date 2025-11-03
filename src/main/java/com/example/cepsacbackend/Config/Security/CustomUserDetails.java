package com.example.cepsacbackend.config.security;

import com.example.cepsacbackend.model.Usuario;
import com.example.cepsacbackend.enums.EstadoUsuario;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // devolvemos una lista con el rol del usuario
        String roleName = "ROLE_" + usuario.getRol().name().toUpperCase();
        return List.of(new SimpleGrantedAuthority(roleName));
    }
    

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        // correo es el username
        return usuario.getCorreo();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !usuario.getEstado().equals(EstadoUsuario.SUSPENDIDO);
    }

    @Override
    public boolean isEnabled() {
        // importante habilitar usuario si esta activo
        return usuario.getEstado().equals(EstadoUsuario.ACTIVO);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //metodos para auditoria
    public Integer getId() {
        return usuario.getIdUsuario();
    }

    public Usuario getUsuario() {
        return usuario;
    }

}


