package com.example.cepsacbackend.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cepsacbackend.dto.Pago.PagoCreateDTO;
import com.example.cepsacbackend.dto.Pago.PagoResponseDTO;
import com.example.cepsacbackend.dto.Pago.PagoUpdateDTO;
import com.example.cepsacbackend.enums.EstadoMatricula;
import com.example.cepsacbackend.enums.Rol;
import com.example.cepsacbackend.exception.BadRequestException;
import com.example.cepsacbackend.exception.ResourceNotFoundException;
import com.example.cepsacbackend.mapper.PagoMapper;
import com.example.cepsacbackend.model.Matricula;
import com.example.cepsacbackend.model.MetodoPago;
import com.example.cepsacbackend.model.Pago;
import com.example.cepsacbackend.model.Usuario;
import com.example.cepsacbackend.repository.MatriculaRepository;
import com.example.cepsacbackend.repository.MetodoPagoRepository;
import com.example.cepsacbackend.repository.PagoRepository;
import com.example.cepsacbackend.repository.UsuarioRepository;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class PagoService {

    private final PagoRepository pagoRepository;
    private final MatriculaRepository matriculaRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PagoMapper pagoMapper;

    @Transactional
    @CacheEvict(value = "matriculas-detalle", key = "#dto.idMatricula()")
    public PagoResponseDTO registrarPago(@Valid PagoCreateDTO dto) {
        Matricula matricula = matriculaRepository.findById(dto.idMatricula())
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula no encontrada: " + dto.idMatricula()));

        MetodoPago metodoPago = metodoPagoRepository.findById(dto.idMetodoPago())
                .orElseThrow(() -> new ResourceNotFoundException("Método de pago no encontrado: " + dto.idMetodoPago()));

        Usuario admin = usuarioRepository.findById(dto.idUsuarioRegistro())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario que registro el pago no encontrado: " + dto.idUsuarioRegistro()));

        if (admin.getRol() != Rol.ADMINISTRADOR) {
            throw new BadRequestException("El usuario que registra el pago debe ser ADMINISTRADOR"); 
        }

        Pago pago = new Pago();
        pago.setMatricula(matricula);
        pago.setMetodoPago(metodoPago);
        pago.setMonto(dto.monto());
        // calculamos automatico el n de cuota
        Integer ultimaCuota = pagoRepository.findMaxNumeroCuotaByMatriculaId(matricula.getIdMatricula());
        short siguienteNumeroCuota = (short) ((ultimaCuota == null ? 0 : ultimaCuota) + 1);
        pago.setNumeroCuota(siguienteNumeroCuota);

        Pago saved = pagoRepository.save(pago);
        matricula.setEstado(EstadoMatricula.EN_PROCESO);
        matriculaRepository.save(matricula);

        return pagoMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<PagoResponseDTO> listarPagosPorMatricula(Integer idMatricula) {
        return pagoMapper.toResponseDTOList(pagoRepository.findByMatriculaIdMatricula(idMatricula));
    }

    @Transactional
    @CacheEvict(value = "matriculas-detalle", key = "#id")
    public PagoResponseDTO actualizarPago(Integer id, PagoUpdateDTO dto) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado: " + id));

        MetodoPago metodoPago = metodoPagoRepository.findById(dto.idMetodoPago())
                .orElseThrow(() -> new ResourceNotFoundException("Método de pago no encontrado: " + dto.idMetodoPago())); 

        Usuario admin = usuarioRepository.findById(dto.idUsuarioRegistro())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + dto.idUsuarioRegistro())); 

        pago.setMetodoPago(metodoPago);
        pago.setMonto(dto.monto());
        pago.setNumeroCuota(dto.numeroCuota());
        pago.setUsuarioRegistro(admin);

        Pago updatedPago = pagoRepository.save(pago);
        return pagoMapper.toResponseDTO(updatedPago);
    }
}
