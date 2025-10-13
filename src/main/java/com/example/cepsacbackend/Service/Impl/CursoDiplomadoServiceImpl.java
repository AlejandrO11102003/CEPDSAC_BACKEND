package com.example.cepsacbackend.Service.Impl;

import com.example.cepsacbackend.Dto.CursoDiplomado.CursoDiplomadoCreateDTO;
import com.example.cepsacbackend.Dto.CursoDiplomado.CursoDiplomadoResponseDTO;
import com.example.cepsacbackend.Entity.Categoria;
import com.example.cepsacbackend.Entity.CursoDiplomado;
import com.example.cepsacbackend.Entity.Usuario;
import com.example.cepsacbackend.Mapper.CursoDiplomadoMapper;
import com.example.cepsacbackend.Repository.CategoriaRepository;
import com.example.cepsacbackend.Repository.CursoDiplomadoRepository;
import com.example.cepsacbackend.Repository.UsuarioRepository;
import com.example.cepsacbackend.Service.CursoDiplomadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoDiplomadoServiceImpl implements CursoDiplomadoService {

    private final CursoDiplomadoRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoDiplomadoMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CursoDiplomadoResponseDTO> listar() {
        return mapper.toResponseDtoList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public CursoDiplomadoResponseDTO obtenerPorId(Short id) {
        CursoDiplomado entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso/Diplomado no encontrado: " + id));
        return mapper.toResponseDto(entity);
    }

    @Override
    @Transactional
    public CursoDiplomadoResponseDTO crear(CursoDiplomadoCreateDTO dto) {
        CursoDiplomado entity = mapper.toEntity(dto);

        if (dto.getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + dto.getIdCategoria()));
            entity.setCategoria(categoria);
        }

        if (dto.getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + dto.getIdUsuario()));
            entity.setUsuario(usuario);
        }

        return mapper.toResponseDto(repository.save(entity));
    }

    @Override
    @Transactional
    public void eliminar(Short id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Curso/Diplomado no encontrado: " + id);
        }
        repository.deleteById(id);
    }
}