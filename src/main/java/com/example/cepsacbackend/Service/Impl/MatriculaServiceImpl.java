package com.example.cepsacbackend.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cepsacbackend.dto.Matricula.AplicarDescuentoDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaCreateDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaDetalleResponseDTO;
import com.example.cepsacbackend.dto.Matricula.MatriculaListResponseDTO;
import com.example.cepsacbackend.dto.Pago.PagoResponseDTO;
import com.example.cepsacbackend.enums.EstadoCuota;
import com.example.cepsacbackend.enums.EstadoMatricula;
import com.example.cepsacbackend.enums.Rol;
import com.example.cepsacbackend.enums.TipoDescuento;
import com.example.cepsacbackend.exception.BadRequestException;
import com.example.cepsacbackend.exception.ResourceNotFoundException;
import com.example.cepsacbackend.mapper.MatriculaMapper;
import com.example.cepsacbackend.mapper.PagoMapper;
import com.example.cepsacbackend.model.Descuento;
import com.example.cepsacbackend.model.Matricula;
import com.example.cepsacbackend.model.Pago;
import com.example.cepsacbackend.model.ProgramacionCurso;
import com.example.cepsacbackend.model.Usuario;
import com.example.cepsacbackend.repository.DescuentoAplicacionRepository;
import com.example.cepsacbackend.repository.MatriculaRepository;
import com.example.cepsacbackend.repository.DescuentoRepository;
import com.example.cepsacbackend.repository.PagoRepository;
import com.example.cepsacbackend.repository.ProgramacionCursoRepository;
import com.example.cepsacbackend.repository.UsuarioRepository;
import com.example.cepsacbackend.service.MatriculaService;
import lombok.NonNull;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatriculaServiceImpl implements MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProgramacionCursoRepository programacionCursoRepository;
    private final DescuentoAplicacionRepository descuentoAplicacionRepository;
    private final DescuentoRepository descuentoRepository;
    private final MatriculaMapper matriculaMapper;
    private final PagoRepository pagoRepository;
    private final PagoMapper pagoMapper;

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "matriculas", key = "'all'"),
        @CacheEvict(value = "matriculas", key = "'alumno_' + #dto.idAlumno")
    })
    public Matricula crearMatricula(MatriculaCreateDTO dto) {
        if (dto.getIdAlumno() == null || dto.getIdProgramacionCurso() == null) {
            throw new BadRequestException(
                    "Faltan datos obligatorios. Debe proporcionar el ID del alumno y el ID de la programación del curso.");
        }
 
        // verificar duplicacion en matricula
        List<EstadoMatricula> estadosActivos = List.of(EstadoMatricula.PENDIENTE, EstadoMatricula.PAGADO);
        matriculaRepository.findMatriculaActivaByAlumnoAndProgramacion(
                dto.getIdAlumno(), 
                dto.getIdProgramacionCurso(), 
                estadosActivos
        ).ifPresent(matriculaExistente -> {
            throw new BadRequestException(
                String.format(
                    "Ya existe una matrícula %s (ID: %d) para este alumno en esta programación. " +
                    "No se permiten matrículas duplicadas mientras haya una activa.",
                    matriculaExistente.getEstado().name(),
                    matriculaExistente.getIdMatricula()
                )
            );
        });

        // valido que el alumno exista y tenga rol de alumno
        Usuario alumno = usuarioRepository.findById(dto.getIdAlumno())
                .orElseThrow(() -> new BadRequestException(
                        String.format("El alumno con ID %d no existe en el sistema.", dto.getIdAlumno())));
        if (alumno.getRol() != Rol.ALUMNO) {
            throw new BadRequestException(
                    String.format("El usuario '%s' no tiene el rol de ALUMNO. Solo los alumnos pueden matricularse.",
                            alumno.getCorreo()));
        }
        // cargo la programacion del curso y obtengo el precio base
        ProgramacionCurso programacion = programacionCursoRepository.findById(dto.getIdProgramacionCurso())
                .orElseThrow(() -> new BadRequestException(
                        String.format(
                                "La programación de curso con ID %d no existe. Verifique la programación seleccionada.",
                                dto.getIdProgramacionCurso())));
        BigDecimal montoBase = programacion.getMonto();
        if (montoBase == null) {
            montoBase = BigDecimal.ZERO; // uso cero si no hay monto configurado en la programacion
        }
        // busco y asigno automaticamente el mejor descuento disponible
        Matricula m = matriculaMapper.toEntity(dto);
        m.setProgramacionCurso(programacion);
        m.setAlumno(alumno);
        m.setMontoBase(montoBase.setScale(2, RoundingMode.HALF_UP));
        // calculo y aplico el mejor descuento disponible antes de guardar la matricula
        Descuento descuentoAplicado = encontrarMejorDescuento(programacion, null); // paso null porque aun no tiene id
        BigDecimal montoDescontado = calcularMontoDescontado(montoBase, descuentoAplicado);
        BigDecimal montoFinal = montoBase.subtract(montoDescontado);
        if (montoFinal.compareTo(BigDecimal.ZERO) < 0) {
            montoFinal = BigDecimal.ZERO;
        }
        montoFinal = montoFinal.setScale(2, RoundingMode.HALF_UP);
        m.setMontoDescontado(montoDescontado);
        m.setMonto(montoFinal);
        m.setDescuento(descuentoAplicado);
        Matricula matriculaGuardada = matriculaRepository.save(m);
        // detectar automaticamente si se debe geenerar cuotas segun duracionMeses 
        if (!Boolean.TRUE.equals(matriculaGuardada.getPagoPersonalizado()) && 
            debeGenerarCuotas(programacion)) {
            List<Pago> cuotas = generarCuotasAutomaticas(matriculaGuardada, montoFinal);
            if (!cuotas.isEmpty()) {
                pagoRepository.saveAll(cuotas);
            }
        }
        return matriculaGuardada;
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
        // paso null si no existe idMatricula todavia
        List<Descuento> descuentos = descuentoAplicacionRepository.findDescuentosVigentes(idCurso, idCategoria,
                idMatricula);
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
        } else { // aplico descuento de monto fijo
            montoDescontado = valor;
        }
        // el descuento no puede ser mayor que el monto base
        if (montoDescontado.compareTo(montoBase) > 0) {
            montoDescontado = montoBase;
        }
        return montoDescontado.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "matriculas", key = "'all'")
    public List<MatriculaListResponseDTO> listarMatriculas() {
        return matriculaRepository.findAllAsListDTO();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "matriculas", key = "'alumno_' + #idAlumno")
    public List<MatriculaListResponseDTO> listarMatriculasPorAlumno(Integer idAlumno) {
        // Verificar que el usuario existe
        usuarioRepository.findById(idAlumno)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("No se encontró el alumno con ID %d.", idAlumno)));
        return matriculaRepository.findByAlumnoIdAsListDTO(idAlumno);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "matriculas", key = "'all'"),
            @CacheEvict(value = "matriculas", key = "'alumno_' + #result.alumno.idUsuario"),
            @CacheEvict(value = "matriculas-detalle", key = "#idMatricula")
    })
    public Matricula cancelarMatricula(Integer idMatricula) {
        Matricula m = matriculaRepository.findById(idMatricula)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("No se encontró la matrícula con ID %d que desea cancelar.", idMatricula)));
        // verifico que no se cancele una matricula ya pagada completamente
        if (m.getEstado() == EstadoMatricula.PAGADO) {
            throw new BadRequestException(
                    String.format("No se puede cancelar la matrícula ID %d porque ya ha sido pagada completamente.",
                            idMatricula));
        }
        m.setEstado(EstadoMatricula.CANCELADO);
        return matriculaRepository.save(m);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "matriculas-detalle", key = "#idMatricula")
    public MatriculaDetalleResponseDTO obtenerDetalle(Integer idMatricula) {
        Matricula matricula = matriculaRepository.findByIdWithDetails(idMatricula)
                .orElseThrow(() -> new com.example.cepsacbackend.exception.ResourceNotFoundException(
                        String.format("No se encontró ninguna matrícula con ID %d.", idMatricula)));
        MatriculaDetalleResponseDTO dtoSinPagos = matriculaMapper.toDetalleResponseDTO(matricula);
        // mapeamos pagos al id matricula
        List<Pago> pagos = pagoRepository.findByMatriculaIdMatricula(idMatricula);
        List<PagoResponseDTO> pagosDTO = pagoMapper.toResponseDTOList(pagos);
        // nueva dto seteando pagos
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
                pagosDTO);
    }

    // verifico si debo generar cuotas automaticas segun la configuracion de la
    // programacion
    private boolean debeGenerarCuotas(ProgramacionCurso programacion) {
        return programacion.getDuracionMeses() != null && programacion.getDuracionMeses() > 0;
    }

    // genero las cuotas automaticas dividiendo el monto total en cuotas mensuales
    // con fechas de vencimiento
    private List<Pago> generarCuotasAutomaticas(Matricula matricula, BigDecimal montoTotal) {
        List<Pago> cuotas = new ArrayList<>();
        ProgramacionCurso programacion = matricula.getProgramacionCurso();
        Short duracionMeses = programacion.getDuracionMeses();

        if (duracionMeses == null || duracionMeses <= 0) {
            return cuotas;
        }
        // calculo el monto de cada cuota dividiendo el total entre la cantidad de meses
        BigDecimal montoPorCuota = montoTotal.divide(
                BigDecimal.valueOf(duracionMeses),
                2,
                RoundingMode.HALF_UP);
        // ajusto la diferencia de redondeo en la ultima cuota para que sume exacto
        BigDecimal sumaTemporalCuotas = montoPorCuota.multiply(BigDecimal.valueOf(duracionMeses));
        BigDecimal diferencia = montoTotal.subtract(sumaTemporalCuotas);
        // obtengo la fecha de inicio del curso para calcular los vencimientos
        LocalDate fechaInicio = programacion.getFechaInicio();
        if (fechaInicio == null) {
            fechaInicio = LocalDate.now(); // uso fecha actual si no hay fecha de inicio configurada
        }
        // genero una cuota por cada mes de duracion del curso
        for (short i = 1; i <= duracionMeses; i++) {
            Pago cuota = new Pago();
            cuota.setMatricula(matricula);
            cuota.setNumeroCuota(i);
            // ajusto el monto de la ultima cuota si hay diferencia por redondeo
            BigDecimal montoCuota = montoPorCuota;
            if (i == duracionMeses && diferencia.compareTo(BigDecimal.ZERO) != 0) {
                montoCuota = montoCuota.add(diferencia);
            }
            cuota.setMonto(montoCuota);
            cuota.setFechaVencimiento(fechaInicio.plusMonths(i - 1)); // vencimiento mensual desde el inicio
            cuota.setEstadoCuota(EstadoCuota.PENDIENTE);
            cuota.setMontoPagado(BigDecimal.ZERO);
            cuota.setEsAutomatico(true);
            cuota.setFechaPago(null); // se asigna cuando se pague
            cuota.setMetodoPago(null); // se asigna cuando se pague
            cuotas.add(cuota);
        }
        return cuotas;
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "matriculas", key = "'all'"),
            @CacheEvict(value = "matriculas-detalle", key = "#idMatricula")
    })
    public Matricula aplicarDescuentoAMatricula(@NonNull Integer idMatricula, AplicarDescuentoDTO dto) {
        // cargo la matricula con todos sus detalles
        Matricula matricula = matriculaRepository.findByIdWithDetails(idMatricula)
                .orElseThrow(() -> new com.example.cepsacbackend.exception.ResourceNotFoundException(
                        String.format("No se encontró la matrícula con ID %d.", idMatricula)));
        // validar estado de la matricula
        validarMatriculaParaDescuento(matricula);
        // cargar el descuento que exista y este vigente
        Descuento nuevoDescuento = descuentoRepository.findById(dto.idDescuento())
                .orElseThrow(() -> new BadRequestException(
                        String.format("El descuento con ID %d no existe en el sistema.", dto.idDescuento())));
        validarVigenciaDescuento(nuevoDescuento);
        BigDecimal montoBase = matricula.getMontoBase();
        BigDecimal nuevoMontoDescontado = calcularMontoDescontado(montoBase, nuevoDescuento);
        BigDecimal nuevoMontoFinal = montoBase.subtract(nuevoMontoDescontado);
        if (nuevoMontoFinal.compareTo(BigDecimal.ZERO) < 0) {
            nuevoMontoFinal = BigDecimal.ZERO;
        }
        nuevoMontoFinal = nuevoMontoFinal.setScale(2, RoundingMode.HALF_UP);
        // actualizo los montos de la matricula
        matricula.setDescuento(nuevoDescuento);
        matricula.setMontoDescontado(nuevoMontoDescontado);
        matricula.setMonto(nuevoMontoFinal);
        // cargo todas las cuotas y calculo saldo pendiente
        List<Pago> todasLasCuotas = pagoRepository.findByMatriculaIdMatricula(idMatricula);
        BigDecimal saldoPendiente = calcularSaldoPendiente(todasLasCuotas, nuevoMontoFinal);
        // redistribuyo el saldo entre las cuotas pendientes
        List<Pago> cuotasPendientes = todasLasCuotas.stream()
                .filter(p -> p.getEstadoCuota() != EstadoCuota.PAGADO)
                .toList();
        redistribuirCuotasPendientes(cuotasPendientes, saldoPendiente);
        // si el saldo es cero, marco la matricula como pagada
        if (saldoPendiente.compareTo(BigDecimal.ZERO) == 0) {
            matricula.setEstado(EstadoMatricula.PAGADO);
        }
        Matricula matriculaActualizada = matriculaRepository.save(matricula);
        return matriculaActualizada;
    }

    // redistribuir el saldo pendiente en cuotas restantes
    private void redistribuirCuotasPendientes(List<Pago> cuotasPendientes, BigDecimal saldoPendiente) {
        if (cuotasPendientes.isEmpty() || saldoPendiente.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        int cantidadCuotasPendientes = cuotasPendientes.size();
        BigDecimal montoPorCuota = saldoPendiente.divide(
                BigDecimal.valueOf(cantidadCuotasPendientes),
                2,
                RoundingMode.HALF_UP);
        // redondear para ajustar la ultima cuota
        BigDecimal sumaTemporalCuotas = montoPorCuota.multiply(BigDecimal.valueOf(cantidadCuotasPendientes));
        BigDecimal diferencia = saldoPendiente.subtract(sumaTemporalCuotas);
        // actualizar montos de cuotas pendientes
        for (int i = 0; i < cantidadCuotasPendientes; i++) {
            Pago cuota = cuotasPendientes.get(i);
            BigDecimal nuevoMontoCuota = montoPorCuota;
            if (i == cantidadCuotasPendientes - 1 && diferencia.compareTo(BigDecimal.ZERO) != 0) {
                nuevoMontoCuota = nuevoMontoCuota.add(diferencia);
            }
            cuota.setMonto(nuevoMontoCuota);
        }
        pagoRepository.saveAll(cuotasPendientes);
    }

    private void validarMatriculaParaDescuento(Matricula matricula) {
        if (matricula.getEstado() == EstadoMatricula.CANCELADO) {
            throw new BadRequestException(
                    String.format("No se puede aplicar descuento a la matrícula ID %d porque está cancelada.",
                            matricula.getIdMatricula()));
        }
        if (matricula.getEstado() == EstadoMatricula.PAGADO) {
            throw new BadRequestException(
                    String.format("No se puede aplicar descuento a la matrícula ID %d porque ya está completamente pagada.",
                            matricula.getIdMatricula()));
        }
    }

    private void validarVigenciaDescuento(Descuento descuento) {
        LocalDate hoy = LocalDate.now();
        if (descuento.getFechaInicio() != null && hoy.isBefore(descuento.getFechaInicio())) {
            throw new BadRequestException(
                    String.format("El descuento con ID %d aún no está vigente. Inicia el %s.",
                            descuento.getIdDescuento(), descuento.getFechaInicio()));
        }
        if (descuento.getFechaFin() != null && hoy.isAfter(descuento.getFechaFin())) {
            throw new BadRequestException(
                    String.format("El descuento con ID %d ya expiró el %s.",
                            descuento.getIdDescuento(), descuento.getFechaFin()));
        }
    }

    private BigDecimal calcularSaldoPendiente(List<Pago> todasLasCuotas, BigDecimal nuevoMontoFinal) {
        BigDecimal totalPagado = todasLasCuotas.stream()
                .filter(p -> p.getEstadoCuota() == EstadoCuota.PAGADO)
                .map(Pago::getMontoPagado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldoPendiente = nuevoMontoFinal.subtract(totalPagado);
        return saldoPendiente.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : saldoPendiente;
    }
}