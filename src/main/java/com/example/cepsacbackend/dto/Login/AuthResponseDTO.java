package com.example.cepsacbackend.dto.Login;

import com.example.cepsacbackend.enums.Rol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private String token; // jwt para devolver al user
    private Rol rol;
    private String username;
}