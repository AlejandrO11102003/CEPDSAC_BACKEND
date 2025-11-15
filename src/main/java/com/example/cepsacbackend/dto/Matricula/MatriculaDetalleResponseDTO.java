package com.example.cepsacbackend.dto.Matricula;

import com.example.cepsacbackend.dto.CursoDiplomado.CursoDiplomadoResponseDTO;
import com.example.cepsacbackend.dto.Descuento.DescuentoResponseDTO;
import com.example.cepsacbackend.dto.Pago.PagoResponseDTO;
import com.example.cepsacbackend.dto.Usuario.UsuarioListResponseDTO;
import com.example.cepsacbackend.enums.EstadoMatricula;

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
