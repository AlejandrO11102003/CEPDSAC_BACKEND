package com.example.cepsacbackend.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cepsacbackend.dto.Matricula.MatriculaCreateDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaDetalleResponseDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaResponseDTO;
import com.example.cepsacbackend.dto.Pago.PagoResponseDTO;
import com.example.cepsacbackend.mapper.MatriculaMapper;
import com.example.cepsacbackend.mapper.PagoMapper;
import com.example.cepsacbackend.model.Descuento;
import com.example.cepsacbackend.model.Matricula;
import com.example.cepsacbackend.model.Pago;
import com.example.cepsacbackend.model.ProgramacionCurso;
import com.example.cepsacbackend.model.Usuario;
import com.example.cepsacbackend.repository.DescuentoAplicacionRepository;
import com.example.cepsacbackend.repository.MatriculaRepository;
import com.example.cepsacbackend.repository.PagoRepository;
import com.example.cepsacbackend.repository.ProgramacionCursoRepository;
import com.example.cepsacbackend.repository.UsuarioRepository;
import com.example.cepsacbackend.service.MatriculaService;
import com.example.cepsacbackend.enums.EstadoMatricula;
import com.example.cepsacbackend.enums.Rol;
import com.example.cepsacbackend.enums.TipoDescuento;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatriculaServiceImpl implements MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProgramacionCursoRepository programacionCursoRepository;
    private final DescuentoAplicacionRepository descuentoAplicacionRepository;
    private final MatriculaMapper matriculaMapper;
    private final PagoRepository pagoRepository;
    private final PagoMapper pagoMapper;

    @Override
    @Transactional
    @CacheEvict(value = "matriculas", key = "'all'")
    public Matricula crearMatricula(MatriculaCreateDTO dto) {
        // validar alumno
        Usuario alumno = usuarioRepository.findById(dto.idAlumno())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado: " + dto.idAlumno()));
        if (alumno.getRol() != Rol.ALUMNO) {
            throw new RuntimeException("El usuario no tiene rol ALUMNO");
        }
        // cargar programacion y obtener precio base
        ProgramacionCurso programacion = programacionCursoRepository.findById(dto.idProgramacionCurso())
                .orElseThrow(() -> new RuntimeException("Programación de curso no encontrada: " + dto.idProgramacionCurso()));
        BigDecimal montoBase = programacion.getMonto();
        if (montoBase == null) {
            montoBase = BigDecimal.ZERO; // si no hay monto configurado aun
        }
        // automatizacion para encontrar y asignar el mejor descuento
        // Primero guardamos la matrícula para obtener su ID
        Matricula m = matriculaMapper.toEntity(dto);
        m.setProgramacionCurso(programacion);
        m.setAlumno(alumno);
        m.setMontoBase(montoBase.setScale(2, RoundingMode.HALF_UP));
        m.setMontoDescontado(BigDecimal.ZERO); // Inicialmente sin descuento
        m.setMonto(montoBase.setScale(2, RoundingMode.HALF_UP)); // Inicialmente monto base
        Matricula matriculaGuardada = matriculaRepository.save(m);

        Descuento descuentoAplicado = encontrarMejorDescuento(programacion, matriculaGuardada.getIdMatricula());
        BigDecimal montoDescontado = calcularMontoDescontado(montoBase, descuentoAplicado);

        BigDecimal montoFinal = montoBase.subtract(montoDescontado);
        if (montoFinal.compareTo(BigDecimal.ZERO) < 0) {
            montoFinal = BigDecimal.ZERO;
        }
        montoFinal = montoFinal.setScale(2, RoundingMode.HALF_UP);

        // Actualizamos la matrícula con el descuento y monto final
        matriculaGuardada.setMontoDescontado(montoDescontado);
        matriculaGuardada.setMonto(montoFinal);
        matriculaGuardada.setDescuento(descuentoAplicado);
        return matriculaRepository.save(matriculaGuardada);
    }

    @Cacheable(value = "descuentos", key = "#programacion.idProgramacionCurso")
    private Descuento encontrarMejorDescuento(ProgramacionCurso programacion, Integer idMatricula) {
        if (programacion.getCursoDiplomado() == null) {
            return null;
        }
        Short idCurso = programacion.getCursoDiplomado().getIdCursoDiplomado();
        Short idCategoria = (programacion.getCursoDiplomado().getCategoria() != null)
                ? programacion.getCursoDiplomado().getCategoria().getIdCategoria()
                : null;
        List<Descuento> descuentos = descuentoAplicacionRepository.findDescuentosVigentes(idCurso, idCategoria, idMatricula);
        return descuentos.isEmpty() ? null : descuentos.get(0);
    }

    private BigDecimal calcularMontoDescontado(BigDecimal montoBase, Descuento descuento) {
        if (descuento == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal montoDescontado;
        BigDecimal valor = descuento.getValor() != null ? descuento.getValor() : BigDecimal.ZERO;
        if (descuento.getTipoDescuento() == TipoDescuento.PORCENTAJE) {
            BigDecimal porcentaje = valor.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
            montoDescontado = montoBase.multiply(porcentaje);
        } else { // MONTO
            montoDescontado = valor;
        }
        // el descuento no puede ser mayor que el monto base
        if (montoDescontado.compareTo(montoBase) > 0) {
            montoDescontado = montoBase;
        }
        return montoDescontado.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "matriculas", key = "'all'"),
        @CacheEvict(value = "matriculas-detalle", key = "#idMatricula")
    })
    public Matricula aprobarMatricula(Integer idMatricula) {
        Matricula m = matriculaRepository.findById(idMatricula)
                .orElseThrow(() -> new RuntimeException("Matrícula no encontrada: " + idMatricula));
        String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario admin = usuarioRepository.findByCorreo(adminEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Administrador no encontrado: " + adminEmail));
        if (admin.getRol() != Rol.ADMINISTRADOR) {
            throw new RuntimeException("El usuario aprobador no tiene rol ADMINISTRADOR");
        }
        m.setAdministradorAprobador(admin);
        // seteamos estado
        m.setEstado(EstadoMatricula.EN_PROCESO);
        return matriculaRepository.save(m);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "matriculas", key = "'all'")
    public List<MatriculaResponseDTO> listarMatriculas() {
        List<Matricula> matriculas = matriculaRepository.findAll();
        return matriculaMapper.toResponseDTOList(matriculas);
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "matriculas", key = "'all'"),
        @CacheEvict(value = "matriculas-detalle", key = "#idMatricula")
    })
    public Matricula cancelarMatricula(Integer idMatricula) {
        Matricula m = matriculaRepository.findById(idMatricula)
                .orElseThrow(() -> new RuntimeException("Matrícula no encontrada: " + idMatricula));
        // no cancelar si ya esta pagada
        if (m.getEstado() == EstadoMatricula.PAGADO) {
            throw new IllegalStateException("No se puede cancelar una matrícula que ya ha sido pagada.");
        }

        m.setEstado(EstadoMatricula.CANCELADO);
        return matriculaRepository.save(m);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "matriculas-detalle", key = "#idMatricula")
    public MatriculaDetalleResponseDTO obtenerDetalle(Integer idMatricula) {
        Matricula matricula = matriculaRepository.findById(idMatricula)
                .orElseThrow(() -> new RuntimeException("Matrícula no encontrada: " + idMatricula));
        MatriculaDetalleResponseDTO dtoSinPagos = matriculaMapper.toDetalleResponseDTO(matricula);
        //mapeamos pagos al id matricula
        List<Pago> pagos = pagoRepository.findByMatriculaIdMatricula(idMatricula);
        log.info("Pagos encontrados para la matrícula {}: {}", idMatricula, pagos.size());
        List<PagoResponseDTO> pagosDTO = pagoMapper.toResponseDTOList(pagos);
        //nueva dto seteando pagos
        return new MatriculaDetalleResponseDTO(
                dtoSinPagos.idMatricula(),
                dtoSinPagos.estado(),
                dtoSinPagos.fechaMatricula(),
                dtoSinPagos.montoBase(),
                dtoSinPagos.montoDescontado(),
                dtoSinPagos.monto(),
                dtoSinPagos.alumno(),
                dtoSinPagos.cursoDiplomado(),
                dtoSinPagos.descuento(),
                pagosDTO
        );
    }
}