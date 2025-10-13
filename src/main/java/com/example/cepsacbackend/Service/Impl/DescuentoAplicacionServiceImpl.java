package com.example.cepsacbackend.Service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cepsacbackend.Dto.DescuentoAplicacion.DescuentoAplicacionCreateDTO;
import com.example.cepsacbackend.Dto.DescuentoAplicacion.DescuentoAplicacionResponseDTO;
import com.example.cepsacbackend.Entity.Categoria;
import com.example.cepsacbackend.Entity.CursoDiplomado;
import com.example.cepsacbackend.Entity.Descuento;
import com.example.cepsacbackend.Entity.DescuentoAplicacion;
import com.example.cepsacbackend.Entity.Usuario;
import com.example.cepsacbackend.Enums.TipoAplicacionDescuento;
import com.example.cepsacbackend.Mapper.DescuentoAplicacionMapper;
import com.example.cepsacbackend.Repository.CategoriaRepository;
import com.example.cepsacbackend.Repository.CursoDiplomadoRepository;
import com.example.cepsacbackend.Repository.DescuentoAplicacionRepository;
import com.example.cepsacbackend.Repository.DescuentoRepository;
import com.example.cepsacbackend.Repository.UsuarioRepository;
import com.example.cepsacbackend.Service.DescuentoAplicacionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DescuentoAplicacionServiceImpl implements DescuentoAplicacionService {

    private final DescuentoAplicacionRepository repository;
    private final DescuentoRepository descuentoRepository;
    private final CategoriaRepository categoriaRepository;
    private final CursoDiplomadoRepository cursoDiplomadoRepository;
    private final UsuarioRepository usuarioRepository;
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