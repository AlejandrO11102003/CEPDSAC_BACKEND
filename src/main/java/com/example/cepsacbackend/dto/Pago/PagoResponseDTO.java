package com.example.cepsacbackend.dto.Pago;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.cepsacbackend.enums.EstadoCuota;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NonNull
@NoArgsConstructor
public class PagoResponseDTO{
        private Integer idPago;
        private BigDecimal monto;
        private Short numeroCuota;
        private LocalDateTime fechaPago;
        private String metodoPagoDescripcion;
        private String tipoMetodo;
        
        // Nuevos campos opcionales para cuotas automáticas
        private LocalDate fechaVencimiento;
        private EstadoCuota estadoCuota;
        private BigDecimal montoPagado;
        private Boolean esAutomatico;

        private String numeroOperacion;
        private String observaciones;

        // Campos adicionales para devoluciones
        private String nombreAlumno;
        private String emailAlumno;
        private String nombreCurso;
        private String nombreMetodoPago;
}
