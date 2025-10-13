package com.example.cepsacbackend.Dto.Matricula;

import jakarta.validation.constraints.NotNull;

public record MatriculaCreateDTO(
    @NotNull Integer idProgramacionCurso,
    @NotNull Integer idAlumno
) {}
