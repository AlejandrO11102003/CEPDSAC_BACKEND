package com.example.cepsacbackend.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.example.cepsacbackend.dto.Pago.PagoCreateDTO;
import com.example.cepsacbackend.dto.Pago.PagoResponseDTO;
import com.example.cepsacbackend.dto.Pago.PagoUpdateDTO;
import com.example.cepsacbackend.enums.EstadoCuota;
import com.example.cepsacbackend.enums.EstadoMatricula;
import com.example.cepsacbackend.exception.ResourceNotFoundException;
import com.example.cepsacbackend.mapper.PagoMapper;
import com.example.cepsacbackend.model.Matricula;
import com.example.cepsacbackend.model.MetodoPago;
import com.example.cepsacbackend.model.Pago;
import com.example.cepsacbackend.repository.MatriculaRepository;
import com.example.cepsacbackend.repository.MetodoPagoRepository;
import com.example.cepsacbackend.repository.PagoRepository;
import com.example.cepsacbackend.service.PagoService;
import com.example.cepsacbackend.exception.BadRequestException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final MatriculaRepository matriculaRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final PagoMapper pagoMapper;

    @Override
    @Transactional
    @CacheEvict(value = "matriculas-detalle", key = "#dto.getIdMatricula()")
    public PagoResponseDTO registrarPago(@Valid PagoCreateDTO dto) {
        Matricula matricula = matriculaRepository.findById(dto.getIdMatricula())
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("No se encontró la matrícula con ID %d. Verifique que la matrícula exista antes de registrar el pago.", dto.getIdMatricula())));

        MetodoPago metodoPago = metodoPagoRepository.findById(dto.getIdMetodoPago())
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("El método de pago con ID %d no existe. Por favor, seleccione un método válido.", dto.getIdMetodoPago())));

        //detecto automaticamente si hay cuotas automaticas pendientes para priorizar su pago
        List<Pago> todasLasCuotas = pagoRepository.findByMatriculaIdMatricula(matricula.getIdMatricula());
        Pago cuotaPendiente = todasLasCuotas.stream()
                .filter(p -> Boolean.TRUE.equals(p.getEsAutomatico()))
                .filter(p -> EstadoCuota.PENDIENTE.equals(p.getEstadoCuota()))
                .findFirst()
                .orElse(null);

        //determino el monto real a pagar
        BigDecimal montoPagar;
        if (cuotaPendiente != null) {
            //si hay cuota automática y no se envió monto, usa el monto de la cuota
            montoPagar = (dto.getMonto() != null) ? dto.getMonto() : cuotaPendiente.getMonto();
        } else {
            //para pagos manuales el monto es obligatorio
            if (dto.getMonto() == null) {
                throw new BadRequestException(
                    "El monto es obligatorio para pagos manuales. " +
                    "Solo es opcional cuando hay cuotas automáticas pendientes.");
            }
            montoPagar = dto.getMonto();
        }

        //valido que el pago no exceda el saldo disponible de la matricula
        BigDecimal montoTotal = matricula.getMonto();
        BigDecimal totalYaPagado = calcularTotalPagado(matricula.getIdMatricula());
        BigDecimal montoDisponible = montoTotal.subtract(totalYaPagado);

        if (montoDisponible.compareTo(BigDecimal.ZERO) == 0) {
            throw new BadRequestException("La matrícula ya está completamente pagada. No se pueden registrar más pagos.");
        }

        if (montoPagar.compareTo(montoDisponible) > 0) {
            throw new BadRequestException(
                String.format("No se puede registrar el pago de $%.2f. El monto excede el saldo pendiente de $%.2f. " +
                             "Total matrícula: $%.2f, Ya pagado: $%.2f", 
                             montoPagar, montoDisponible, montoTotal, totalYaPagado));
        }

        Pago pago;
        
        if (cuotaPendiente != null) {
            //pago la primera cuota automatica pendiente encontrada
            pago = cuotaPendiente;
            pago.setMetodoPago(metodoPago);
            pago.setMontoPagado(montoPagar);
            pago.setFechaPago(LocalDateTime.now());
            pago.setEstadoCuota(EstadoCuota.PAGADO);
            
            log.info("✓ Pagando cuota automática #{} de matrícula {} - Monto: ${}", 
                     pago.getNumeroCuota(), matricula.getIdMatricula(), montoPagar);
        } else {
            //creo un pago manual porque no hay cuotas automaticas pendientes
            pago = new Pago();
            pago.setMatricula(matricula);
            pago.setMetodoPago(metodoPago);
            pago.setMonto(montoPagar);
            pago.setMontoPagado(montoPagar);
            pago.setFechaPago(LocalDateTime.now());
            pago.setEstadoCuota(EstadoCuota.PAGADO);
            pago.setEsAutomatico(false);
            
            //calculo el numero de cuota siguiente para el pago manual
            Integer ultimaCuota = pagoRepository.findMaxNumeroCuotaByMatriculaId(matricula.getIdMatricula());
            short siguienteNumeroCuota = (short) ((ultimaCuota == null ? 0 : ultimaCuota) + 1);
            pago.setNumeroCuota(siguienteNumeroCuota);
            
            log.info("○ Registrando pago manual (cuota #{}) para matrícula {}", 
                     siguienteNumeroCuota, matricula.getIdMatricula());
        }

        Pago saved = pagoRepository.save(pago);
        actualizarEstadoMatricula(matricula);

        return pagoMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponseDTO> listarPagosPorMatricula(Integer idMatricula) {
        return pagoMapper.toResponseDTOList(pagoRepository.findByMatriculaIdMatricula(idMatricula));
    }

    @Override
    @Transactional
    @CacheEvict(value = "matriculas-detalle", key = "#id")
    public PagoResponseDTO actualizarPago(Integer id, PagoUpdateDTO dto) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("No se encontró el pago con ID %d que desea actualizar.", id)));
        MetodoPago metodoPago = metodoPagoRepository.findById(dto.getIdMetodoPago())
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("El método de pago con ID %d no existe.", dto.getIdMetodoPago()))); 
        pago.setMetodoPago(metodoPago);
        pago.setMonto(dto.getMonto());
        pago.setNumeroCuota(dto.getNumeroCuota());

        Pago updatedPago = pagoRepository.save(pago);
        return pagoMapper.toResponseDTO(updatedPago);
    }



    /**
     * calculo el total ya pagado de una matricula sumando todos los pagos efectivos
     */
    private BigDecimal calcularTotalPagado(Integer idMatricula) {
        List<Pago> pagos = pagoRepository.findByMatriculaIdMatricula(idMatricula);
        return pagos.stream()
                .filter(p -> EstadoCuota.PAGADO.equals(p.getEstadoCuota()) || p.getFechaPago() != null)
                .map(p -> p.getMontoPagado() != null ? p.getMontoPagado() : p.getMonto())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * actualizo el estado de la matricula comparando el total pagado con el monto total
     */
    private void actualizarEstadoMatricula(Matricula matricula) {
        //calculo el total ya pagado
        BigDecimal totalPagado = calcularTotalPagado(matricula.getIdMatricula());
        BigDecimal montoTotal = matricula.getMonto();
        
        //actualizo el estado segun corresponda
        if (totalPagado.compareTo(montoTotal) >= 0) {
            matricula.setEstado(EstadoMatricula.PAGADO);
            log.info("✓ Matrícula {} marcada como PAGADO (Total: {} / {})", 
                     matricula.getIdMatricula(), totalPagado, montoTotal);
        } else if (totalPagado.compareTo(BigDecimal.ZERO) > 0) {
            matricula.setEstado(EstadoMatricula.EN_PROCESO);
        }
        
        matriculaRepository.save(matricula);
    }
}
