package com.example.cepsacbackend.Controller;

import com.example.cepsacbackend.Entity.TipoIdentificacion;
import com.example.cepsacbackend.Repository.TipoIdentificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tiposidentificacion")
public class TipoIdentificacionController {

    private final TipoIdentificacionRepository tipoRepository;

    @GetMapping("/listar")
    public List<TipoIdentificacion> listarTiposIdentificacion() {
        return tipoRepository.findAll();
    }

    @PostMapping("/obtener")
    public TipoIdentificacion obtenerTipoIdentificacion(@RequestBody Short idTipoIdentificacion) {
        Optional<TipoIdentificacion> tipoIdentificacion = tipoRepository.findById(idTipoIdentificacion);
        return tipoIdentificacion.orElse(null);
    }

    @PostMapping("/crear")
    public TipoIdentificacion crearTipoIdentificacion(@RequestBody TipoIdentificacion tipoIdentificacion) {
        return tipoRepository.save(tipoIdentificacion);
    }

    @PostMapping("/actualizar")
    public TipoIdentificacion actualizarTipoIdentificacion(@RequestBody TipoIdentificacion tipoIdentificacion) {
        return tipoRepository.save(tipoIdentificacion);
    }

    @PostMapping("/eliminar")
    public String eliminarTipoIdentificacion(@RequestBody Short idTipoIdentificacion) {
        tipoRepository.deleteById(idTipoIdentificacion);
        return "Tipo de identificación eliminado correctamente";
    }
}