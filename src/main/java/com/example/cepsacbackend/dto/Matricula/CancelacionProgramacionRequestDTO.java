package com.example.cepsacbackend.dto.Matricula;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CancelacionProgramacionRequestDTO {
    
    @NotBlank(message = "El motivo de cancelación es obligatorio")
    private String motivo;
}
