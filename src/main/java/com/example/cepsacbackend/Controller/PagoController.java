package com.example.cepsacbackend.Controller;

import com.example.cepsacbackend.Dto.Pago.*;
import com.example.cepsacbackend.Service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    @PostMapping
    public ResponseEntity<PagoResponseDTO> registrar(@RequestBody @Valid PagoCreateDTO dto) {
        PagoResponseDTO response = pagoService.registrarPago(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/matricula/{idMatricula}")
    public List<PagoResponseDTO> listarPagosPorMatricula(@PathVariable Integer idMatricula) {
        return pagoService.listarPagosPorMatricula(idMatricula);
    }
}
