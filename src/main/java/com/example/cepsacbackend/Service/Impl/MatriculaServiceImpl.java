package com.example.cepsacbackend.Service.Impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.example.cepsacbackend.Dto.Matricula.MatriculaCreateDTO;
import com.example.cepsacbackend.Dto.Matricula.MatriculaResponseDTO;
import com.example.cepsacbackend.Dto.Matricula.MatriculaDetalleResponseDTO;
import com.example.cepsacbackend.Dto.Pago.PagoResponseDTO;
import com.example.cepsacbackend.Entity.Descuento;
import com.example.cepsacbackend.Entity.Matricula;
import com.example.cepsacbackend.Entity.ProgramacionCurso;
import com.example.cepsacbackend.Entity.Usuario;
import com.example.cepsacbackend.Enums.EstadoMatricula;
import com.example.cepsacbackend.Enums.Rol;
import com.example.cepsacbackend.Enums.TipoDescuento;
import com.example.cepsacbackend.Entity.Pago;
import com.example.cepsacbackend.Mapper.MatriculaMapper;
import com.example.cepsacbackend.Mapper.PagoMapper;
import com.example.cepsacbackend.Repository.MatriculaRepository;
import com.example.cepsacbackend.Repository.ProgramacionCursoRepository;
import com.example.cepsacbackend.Repository.UsuarioRepository;
import com.example.cepsacbackend.Repository.PagoRepository;
import com.example.cepsacbackend.Repository.DescuentoAplicacionRepository;
import com.example.cepsacbackend.Service.MatriculaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
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
        Descuento descuentoAplicado = encontrarMejorDescuento(programacion);
        BigDecimal montoDescontado = calcularMontoDescontado(montoBase, descuentoAplicado);

        BigDecimal montoFinal = montoBase.subtract(montoDescontado);
        if (montoFinal.compareTo(BigDecimal.ZERO) < 0) {
            montoFinal = BigDecimal.ZERO;
        }
        montoFinal = montoFinal.setScale(2, RoundingMode.HALF_UP);
        // mapeamos campos con el mapper
        Matricula m = matriculaMapper.toEntity(dto);
        // set relaciones y montos calculados
        m.setProgramacionCurso(programacion);
        m.setAlumno(alumno);
        m.setMontoBase(montoBase.setScale(2, RoundingMode.HALF_UP));
        m.setMontoDescontado(montoDescontado);
        m.setMonto(montoFinal);
        return matriculaRepository.save(m);
    }

    private Descuento encontrarMejorDescuento(ProgramacionCurso programacion) {
        if (programacion.getCursoDiplomado() == null) {
            return null;
        }
        Short idCurso = programacion.getCursoDiplomado().getIdCursoDiplomado();
        Short idCategoria = (programacion.getCursoDiplomado().getCategoria() != null)
                ? programacion.getCursoDiplomado().getCategoria().getIdCategoria()
                : null;
                List<Descuento> descuentos = descuentoAplicacionRepository.findDescuentosVigentes(idCurso, idCategoria);
        // retorna el primero , el mas alto
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
    public Matricula aprobarMatricula(Integer idMatricula) {
        Matricula m = matriculaRepository.findById(idMatricula)
                .orElseThrow(() -> new RuntimeException("Matrícula no encontrada: " + idMatricula));
        // obtenermos username desde autenticacion
        String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario admin = usuarioRepository.findByCorreo(adminEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Administrador no encontrado en el contexto de seguridad: " + adminEmail));
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
    public List<MatriculaResponseDTO> listarMatriculas() {
        List<Matricula> matriculas = matriculaRepository.findAll();
        return matriculaMapper.toResponseDTOList(matriculas);
    }

    @Override
    @Transactional
    public Matricula cancelarMatricula(Integer idMatricula) {
        Matricula m = matriculaRepository.findById(idMatricula)
                .orElseThrow(() -> new RuntimeException("Matrícula no encontrada: " + idMatricula));

        // Opcional: Validar que no se pueda cancelar una matrícula ya pagada.
        if (m.getEstado() == EstadoMatricula.PAGADO) {
            throw new IllegalStateException("No se puede cancelar una matrícula que ya ha sido pagada.");
        }

        m.setEstado(EstadoMatricula.CANCELADO);
        return matriculaRepository.save(m);
    }

    @Override
    @Transactional(readOnly = true)
    public MatriculaDetalleResponseDTO obtenerDetalle(Integer idMatricula) {
        Matricula matricula = matriculaRepository.findById(idMatricula)
                .orElseThrow(() -> new RuntimeException("Matrícula no encontrada: " + idMatricula));
        MatriculaDetalleResponseDTO dtoSinPagos = matriculaMapper.toDetalleResponseDTO(matricula);
        //mapeamos pagos al id matricula
        List<Pago> pagos = pagoRepository.findByMatriculaIdMatricula(idMatricula);
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
