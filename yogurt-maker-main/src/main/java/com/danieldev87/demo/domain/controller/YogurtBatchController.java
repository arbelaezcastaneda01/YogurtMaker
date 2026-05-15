package com.danieldev87.demo.domain.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.danieldev87.demo.domain.model.YogurtBatch;
import com.danieldev87.demo.domain.service.YogurtMakingService;
import com.danieldev87.demo.dto.BatchDTO;
import com.danieldev87.demo.dto.TemperatureRecordDTO;

import lombok.RequiredArgsConstructor;

// 🔹 IMPORTS SWAGGER
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/batches")
@RequiredArgsConstructor

@Tag(name = "Gestión de Lotes", description = "Operaciones relacionadas con la producción de yogurt")
public class YogurtBatchController {
    
    private final YogurtMakingService yogurtMakingService;

    @Operation(
        summary = "Crear nuevo lote",
        description = "Inicia un nuevo proceso de producción de yogurt"
    )
    @ApiResponse(responseCode = "201", description = "Lote creado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @PostMapping
    public ResponseEntity<YogurtBatch> startNewBatch(@RequestBody BatchDTO.StartBatchRequest request) {
        YogurtBatch batch = yogurtMakingService.startNewBatch(
            request.getRecipeId(), 
            request.getCustomMilkVolume(), 
            request.getCustomStarterAmount()
        );
        return new ResponseEntity<>(batch, HttpStatus.CREATED);
    }

    @Operation(summary = "Iniciar calentamiento", description = "Cambia el estado del lote a calentamiento")
    @ApiResponse(responseCode = "200", description = "Proceso iniciado correctamente")
    @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    @PostMapping("/{batchId}/heating")
    public ResponseEntity<YogurtBatch> startHeating(@PathVariable Long batchId) {
        return ResponseEntity.ok(yogurtMakingService.startHeating(batchId));
    }

    @Operation(summary = "Iniciar inoculación", description = "Agrega cultivo al yogurt")
    @ApiResponse(responseCode = "200", description = "Proceso iniciado correctamente")
    @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    @PostMapping("/{batchId}/inoculating")
    public ResponseEntity<YogurtBatch> startInoculating(@PathVariable Long batchId) {
        return ResponseEntity.ok(yogurtMakingService.startInoculating(batchId));
    }

    @Operation(summary = "Iniciar incubación", description = "Inicia el proceso de incubación")
    @ApiResponse(responseCode = "200", description = "Incubación iniciada")
    @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    @PostMapping("/{batchId}/incubation")
    public ResponseEntity<YogurtBatch> startIncubation(@PathVariable Long batchId) {
        return ResponseEntity.ok(yogurtMakingService.startIncubation(batchId));
    }

    @Operation(summary = "Iniciar refrigeración", description = "Comienza el enfriamiento del lote")
    @ApiResponse(responseCode = "200", description = "Refrigeración iniciada")
    @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    @PostMapping("/{batchId}/refrigeration")
    public ResponseEntity<YogurtBatch> startRefrigeration(@PathVariable Long batchId) {
        return ResponseEntity.ok(yogurtMakingService.startRefrigeration(batchId));
    }

    @Operation(summary = "Finalizar lote", description = "Marca el lote como completado")
    @ApiResponse(responseCode = "200", description = "Lote completado")
    @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    @PostMapping("/{batchId}/complete")
    public ResponseEntity<YogurtBatch> completeBatch(@PathVariable Long batchId) {
        return ResponseEntity.ok(yogurtMakingService.completeBatch(batchId));
    }

    @Operation(summary = "Marcar lote como fallido", description = "Indica que el lote falló")
    @ApiResponse(responseCode = "200", description = "Lote marcado como fallido")
    @ApiResponse(responseCode = "400", description = "Error en la solicitud")
    @PostMapping("/{batchId}/fail")
    public ResponseEntity<YogurtBatch> markAsFailed(
            @PathVariable Long batchId, 
            @RequestBody BatchDTO.FailRequest request) {
        return ResponseEntity.ok(yogurtMakingService.markAsFailed(batchId, request.getReason()));
    }

    @Operation(summary = "Listar lotes", description = "Obtiene todos los lotes o filtra por estado")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<YogurtBatch>> getAllBatches(
            @RequestParam(required = false) YogurtBatch.BatchStatus status) {
        if (status != null) {
            return ResponseEntity.ok(yogurtMakingService.getBatchesByStatus(status));
        }
        return ResponseEntity.ok(yogurtMakingService.getAllBatches());
    }

    @Operation(summary = "Obtener lote por ID", description = "Devuelve un lote específico")
    @ApiResponse(responseCode = "200", description = "Lote encontrado")
    @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    @GetMapping("/{batchId}")
    public ResponseEntity<YogurtBatch> getBatch(@PathVariable Long batchId) {
        return ResponseEntity.ok(yogurtMakingService.getBatch(batchId));
    }

    @Operation(summary = "Registrar temperatura", description = "Guarda un registro de temperatura del lote")
    @ApiResponse(responseCode = "200", description = "Temperatura registrada")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @PostMapping("/{batchId}/temperature")
    public ResponseEntity<Void> recordTemperature(
            @PathVariable Long batchId, 
            @RequestBody TemperatureRecordDTO request) {
        yogurtMakingService.recordTemperature(batchId, request.getTemperature(), request.getType());
        return ResponseEntity.ok().build();
    }
}