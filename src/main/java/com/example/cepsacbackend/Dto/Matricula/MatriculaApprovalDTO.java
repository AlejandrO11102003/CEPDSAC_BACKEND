package com.example.cepsacbackend.Dto.Matricula;

import jakarta.validation.constraints.NotNull;

public record MatriculaApprovalDTO(
    @NotNull Integer idAdministrador
) {}
