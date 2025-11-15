package com.example.cepsacbackend.controller;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {

    private final MeterRegistry meterRegistry;

    public MonitorController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @GetMapping("/metrics")
    public Map<String, Object> obtenerMetricas() {
        Map<String, Object> metricas = new HashMap<>();

        double memoriaUsada = meterRegistry.get("jvm.memory.used")
                .tag("area", "heap")
                .gauge().value();

        double memoriaMaxima = meterRegistry.get("jvm.memory.max")
                .tag("area", "heap")
                .gauge().value();

        double usoPorcentaje = (memoriaUsada / memoriaMaxima) * 100;

        double cpuUso = meterRegistry.get("system.cpu.usage")
                .gauge().value() * 100;

        Double cacheSize = meterRegistry.find("cache.size")
                .tag("name", "usuarios")
                .gauge() != null ? meterRegistry.find("cache.size")
                .tag("name", "usuarios")
                .gauge().value() : 0.0;

        metricas.put("memoriaUsadaMB", memoriaUsada / (1024 * 1024));
        metricas.put("memoriaMaximaMB", memoriaMaxima / (1024 * 1024));
        metricas.put("usoMemoriaPorcentaje", usoPorcentaje);
        metricas.put("usoCpuPorcentaje", cpuUso);
        metricas.put("tamañoCacheUsuarios", cacheSize);

        return metricas;
    }
}
