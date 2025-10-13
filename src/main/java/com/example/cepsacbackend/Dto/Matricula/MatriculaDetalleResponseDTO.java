package com.example.cepsacbackend.Dto.Matricula;

import com.example.cepsacbackend.Dto.Usuario.UsuarioListResponseDTO;
import com.example.cepsacbackend.Dto.CursoDiplomado.CursoDiplomadoResponseDTO;
import com.example.cepsacbackend.Dto.Descuento.DescuentoResponseDTO;
import com.example.cepsacbackend.Dto.Pago.PagoResponseDTO;
import com.example.cepsacbackend.Enums.EstadoMatricula;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record MatriculaDetalleResponseDTO(
        Integer idMatricula,
        EstadoMatricula estado,
        LocalDateTime fechaMatricula,
        BigDecimal montoBase,
        BigDecimal montoDescontado,
        BigDecimal monto,
        UsuarioListResponseDTO alumno,
        CursoDiplomadoResponseDTO cursoDiplomado,
        DescuentoResponseDTO descuento,
        List<PagoResponseDTO> pagos
) {}
