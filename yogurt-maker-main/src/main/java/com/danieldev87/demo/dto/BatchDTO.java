package com.danieldev87.demo.dto;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

public class BatchDTO {
    
    @Data
    public static class StartBatchRequest {

        @Schema(description = "ID de la receta utilizada para el lote", example = "1")
        private Long recipeId;

        @Schema(description = "Cantidad de leche personalizada en litros", example = "2.5")
        private Double customMilkVolume;

        @Schema(description = "Cantidad de cultivo iniciador en litros", example = "0.1")
        private Double customStarterAmount;
    }
    
    @Data
    public static class FailRequest {

        @Schema(description = "Motivo por el cual el lote falló", example = "Temperatura incorrecta")
        private String reason;
    }
}