package com.danieldev87.demo.domain.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.danieldev87.demo.domain.model.TemperatureLog;
import com.danieldev87.demo.domain.model.YogurtBatch;
import com.danieldev87.demo.domain.repository.TemperatureLogRepository;
import com.danieldev87.demo.domain.repository.YogurtBatchRepository;
import com.danieldev87.demo.domain.service.TemperatureControlService;
import com.danieldev87.demo.dto.MonitoringDTO;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/monitoring")
@RequiredArgsConstructor

@Tag(name = "Monitoreo", description = "API para monitorear el estado y temperatura de los lotes")
public class MonitoringController {

    private final YogurtBatchRepository batchRepository;
    private final TemperatureLogRepository temperatureLogRepository;
    private final TemperatureControlService temperatureControlService;

    @Operation(summary = "Obtener lotes activos",
               description = "Devuelve todos los lotes que están en proceso (calentando, enfriando, incubando, etc.)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de lotes activos obtenida correctamente")
    })
    @GetMapping("/batches/active")
    public ResponseEntity<List<YogurtBatch>> getActiveBatches() {
        List<YogurtBatch> activeBatches = batchRepository.findByStatus(YogurtBatch.BatchStatus.INCUBATING);
        activeBatches.addAll(batchRepository.findByStatus(YogurtBatch.BatchStatus.HEATING));
        activeBatches.addAll(batchRepository.findByStatus(YogurtBatch.BatchStatus.COOLING));
        activeBatches.addAll(batchRepository.findByStatus(YogurtBatch.BatchStatus.REFRIGERATING));
        return ResponseEntity.ok(activeBatches);
    }

    @Operation(summary = "Resumen de temperatura de un lote",
               description = "Obtiene temperatura actual, máxima, mínima y promedio de un lote")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resumen obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    })
    @GetMapping("/batches/{batchId}/temperature")
    public ResponseEntity<MonitoringDTO.TemperatureSummary> getBatchTemperatureSummary(@PathVariable Long batchId) {

        Double currentTemp = temperatureControlService.getCurrentTemperature(batchId);
        Double maxTemp = temperatureLogRepository.getMaxTemperatureByBatch(batchId);
        Double minTemp = temperatureLogRepository.getMinTemperatureByBatch(batchId);
        Double avgTemp = temperatureLogRepository.getAverageTemperatureByBatchAndType(
            batchId, TemperatureLog.LogType.INCUBATION);

        MonitoringDTO.TemperatureSummary summary = MonitoringDTO.TemperatureSummary.builder()
            .currentTemperature(currentTemp)
            .maximumTemperature(maxTemp)
            .minimumTemperature(minTemp)
            .averageTemperature(avgTemp)
            .build();

        return ResponseEntity.ok(summary);
    }

    @Operation(summary = "Obtener historial de temperaturas",
               description = "Devuelve los registros de temperatura de un lote, opcionalmente filtrados por fecha")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registros obtenidos correctamente"),
        @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    })
    @GetMapping("/batches/{batchId}/temperature-logs")
    public ResponseEntity<List<TemperatureLog>> getTemperatureLogs(
            @PathVariable Long batchId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        if (start != null && end != null) {
            return ResponseEntity.ok(temperatureLogRepository.findByBatchAndTimeRange(batchId, start, end));
        }

        YogurtBatch batch = batchRepository.findById(batchId).orElseThrow();
        return ResponseEntity.ok(temperatureLogRepository.findByBatch(batch));
    }

    @Operation(summary = "Dashboard general",
               description = "Devuelve estadísticas generales del sistema (lotes activos, completados, fallidos, etc.)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dashboard generado correctamente")
    })
    @GetMapping("/dashboard")
    public ResponseEntity<MonitoringDTO.Dashboard> getDashboard() {

        long preparingCount = batchRepository.countByStatus(YogurtBatch.BatchStatus.PREPARING);
        long heatingCount = batchRepository.countByStatus(YogurtBatch.BatchStatus.HEATING);
        long coolingCount = batchRepository.countByStatus(YogurtBatch.BatchStatus.COOLING);
        long incubatingCount = batchRepository.countByStatus(YogurtBatch.BatchStatus.INCUBATING);
        long refrigeratingCount = batchRepository.countByStatus(YogurtBatch.BatchStatus.REFRIGERATING);
        long completedCount = batchRepository.countByStatus(YogurtBatch.BatchStatus.COMPLETED);
        long failedCount = batchRepository.countByStatus(YogurtBatch.BatchStatus.FAILED);

        Map<String, Long> batchCounts = new HashMap<>();
        batchCounts.put("PREPARING", preparingCount);
        batchCounts.put("HEATING", heatingCount);
        batchCounts.put("COOLING", coolingCount);
        batchCounts.put("INCUBATING", incubatingCount);
        batchCounts.put("REFRIGERATING", refrigeratingCount);
        batchCounts.put("COMPLETED", completedCount);
        batchCounts.put("FAILED", failedCount);

        MonitoringDTO.Dashboard dashboard = MonitoringDTO.Dashboard.builder()
            .batchCounts(batchCounts)
            .activeBatchesCount(preparingCount + heatingCount + coolingCount + incubatingCount + refrigeratingCount)
            .completedToday(batchRepository.findByStatusAndDateRange(
                YogurtBatch.BatchStatus.COMPLETED,
                LocalDateTime.now().withHour(0).withMinute(0),
                LocalDateTime.now()).size())
            .build();

        return ResponseEntity.ok(dashboard);
    }
}