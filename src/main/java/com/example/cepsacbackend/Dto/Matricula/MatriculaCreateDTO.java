package com.example.cepsacbackend.dto.Matricula;

import jakarta.validation.constraints.NotNull;

public record MatriculaCreateDTO(
    @NotNull Integer idProgramacionCurso,
    @NotNull Integer idAlumno
) {}
