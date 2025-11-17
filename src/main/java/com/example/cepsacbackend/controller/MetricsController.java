package com.example.cepsacbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import com.example.cepsacbackend.dto.Metrics.MetricsResponseDTO;
import com.example.cepsacbackend.service.MetricsService;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final MetricsService metricsService;


    @GetMapping
    public ResponseEntity<MetricsResponseDTO> getMetrics() {
        MetricsResponseDTO dto = metricsService.getMetrics();
        return ResponseEntity.ok(dto);
    }
}
