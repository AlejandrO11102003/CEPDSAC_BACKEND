package com.example.cepsacbackend.Service;

import com.example.cepsacbackend.Dto.Pago.PagoCreateDTO;
import com.example.cepsacbackend.Dto.Pago.PagoResponseDTO;
import com.example.cepsacbackend.Dto.Pago.PagoUpdateDTO;
import com.example.cepsacbackend.Entity.*;
import com.example.cepsacbackend.Enums.EstadoMatricula;
import com.example.cepsacbackend.Enums.Rol;
import com.example.cepsacbackend.Mapper.PagoMapper;
import com.example.cepsacbackend.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final MatriculaRepository matriculaRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PagoMapper pagoMapper;

    @Transactional
    @CacheEvict(value = "matriculas-detalle", key = "#dto.idMatricula()")
    public PagoResponseDTO registrarPago(PagoCreateDTO dto) {
        Matricula matricula = matriculaRepository.findById(dto.idMatricula())
                .orElseThrow(() -> new RuntimeException("Matrícula no encontrada: " + dto.idMatricula()));

        MetodoPago metodoPago = metodoPagoRepository.findById(dto.idMetodoPago())
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado: " + dto.idMetodoPago()));

        Usuario admin = usuarioRepository.findById(dto.idUsuarioRegistro())
                .orElseThrow(() -> new RuntimeException("Usuario (registro) no encontrado: " + dto.idUsuarioRegistro()));

        if (admin.getRol() != Rol.ADMINISTRADOR) {
            throw new RuntimeException("El usuario que registra el pago debe ser ADMINISTRADOR");
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
                .orElseThrow(() -> new RuntimeException("Pago no encontrado: " + id));

        MetodoPago metodoPago = metodoPagoRepository.findById(dto.idMetodoPago())
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado: " + dto.idMetodoPago()));

        Usuario admin = usuarioRepository.findById(dto.idUsuarioRegistro())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + dto.idUsuarioRegistro()));

        if (admin.getRol() != Rol.ADMINISTRADOR) {
            throw new RuntimeException("El usuario que registra el pago debe ser ADMINISTRADOR");
        }

        pago.setMetodoPago(metodoPago);
        pago.setMonto(dto.monto());
        pago.setNumeroCuota(dto.numeroCuota());
        pago.setUsuarioRegistro(admin);

        Pago updatedPago = pagoRepository.save(pago);
        return pagoMapper.toResponseDTO(updatedPago);
    }
}
