package com.example.cepsacbackend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cepsacbackend.dto.DescuentoAplicacion.DescuentoAplicacionCreateDTO;
import com.example.cepsacbackend.dto.DescuentoAplicacion.DescuentoAplicacionResponseDTO;
import com.example.cepsacbackend.mapper.DescuentoAplicacionMapper;
import com.example.cepsacbackend.model.Categoria;
import com.example.cepsacbackend.model.CursoDiplomado;
import com.example.cepsacbackend.model.Descuento;
import com.example.cepsacbackend.model.DescuentoAplicacion;
import com.example.cepsacbackend.model.Matricula;
import com.example.cepsacbackend.model.Usuario;
import com.example.cepsacbackend.repository.CategoriaRepository;
import com.example.cepsacbackend.repository.CursoDiplomadoRepository;
import com.example.cepsacbackend.repository.DescuentoAplicacionRepository;
import com.example.cepsacbackend.repository.DescuentoRepository;
import com.example.cepsacbackend.repository.MatriculaRepository;
import com.example.cepsacbackend.repository.UsuarioRepository;
import com.example.cepsacbackend.service.DescuentoAplicacionService;
import com.example.cepsacbackend.enums.TipoAplicacionDescuento;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DescuentoAplicacionServiceImpl implements DescuentoAplicacionService {

    private final DescuentoAplicacionRepository repository;
    private final DescuentoRepository descuentoRepository;
    private final CategoriaRepository categoriaRepository;
    private final CursoDiplomadoRepository cursoDiplomadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final MatriculaRepository matriculaRepository;
    private final DescuentoAplicacionMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<DescuentoAplicacionResponseDTO> listar() {
        return mapper.toResponseDtoList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public DescuentoAplicacionResponseDTO obtenerPorId(Integer id) {
        DescuentoAplicacion entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Regla de aplicación de descuento no encontrada: " + id));
        return mapper.toResponseDto(entity);
    }

    @Override
    @Transactional
    public DescuentoAplicacionResponseDTO crear(DescuentoAplicacionCreateDTO dto) {
        DescuentoAplicacion entity = mapper.toEntity(dto);

        Descuento descuento = descuentoRepository.findById(dto.getIdDescuento())
                .orElseThrow(() -> new RuntimeException("Descuento no encontrado: " + dto.getIdDescuento()));
        entity.setDescuento(descuento);

        if (dto.getTipoAplicacion() == TipoAplicacionDescuento.CATEGORIA) {
            Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + dto.getIdCategoria()));
            entity.setCategoria(categoria);
        } else if (dto.getTipoAplicacion() == TipoAplicacionDescuento.CURSO) {
            CursoDiplomado curso = cursoDiplomadoRepository.findById(dto.getIdCursoDiplomado())
                    .orElseThrow(() -> new RuntimeException("Curso/Diplomado no encontrado: " + dto.getIdCursoDiplomado()));
            entity.setCursoDiplomado(curso);
        }

        if (dto.getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + dto.getIdUsuario()));
            entity.setUsuario(usuario);
        }

        if (dto.getIdMatricula() != null) {
            Matricula matricula = matriculaRepository.findById(dto.getIdMatricula())
                    .orElseThrow(() -> new RuntimeException("Matrícula no encontrada: " + dto.getIdMatricula()));
            entity.setMatricula(matricula);
        }

        DescuentoAplicacion guardado = repository.save(entity);
        return mapper.toResponseDto(guardado);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Regla de aplicación de descuento no encontrada: " + id);
        }
        repository.deleteById(id);
    }
}