package com.example.cepsacbackend.dto.Matricula;

import jakarta.validation.constraints.NotNull;

public record MatriculaApprovalDTO(
    @NotNull Integer idAdministrador
) {}
