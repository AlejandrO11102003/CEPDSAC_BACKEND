package com.example.cepsacbackend.dto.CursoDiplomado;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para actualizar un Curso o Diplomado existente.
 */
@Getter
@Setter
public class CursoDiplomadoUpdateDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 255, message = "El nombre no puede tener más de 255 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @NotNull(message = "El monto es requerido")
    private BigDecimal monto;

    @NotNull(message = "El tipo es requerido (true para Diplomado, false para Curso)")
    private Boolean tipo;

    @NotNull(message = "El ID de la categoría es requerido")
    private Short idCategoria;
}
