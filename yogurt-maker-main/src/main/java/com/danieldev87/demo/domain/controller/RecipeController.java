package com.danieldev87.demo.domain.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.danieldev87.demo.domain.model.Recipe;
import com.danieldev87.demo.domain.service.RecipeService;
import com.danieldev87.demo.dto.RecipeDTO;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor

@Tag(name = "Gestión de Recetas", description = "Operaciones relacionadas con recetas de yogurt")
public class RecipeController {
    
    private final RecipeService recipeService;

    @Operation(summary = "Crear receta", description = "Crea una nueva receta de yogurt")
    @ApiResponse(responseCode = "201", description = "Receta creada correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@RequestBody RecipeDTO recipeDTO) {
        Recipe recipe = recipeService.createRecipe(recipeDTO);
        return new ResponseEntity<>(recipe, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar receta", description = "Actualiza una receta existente")
    @ApiResponse(responseCode = "200", description = "Receta actualizada correctamente")
    @ApiResponse(responseCode = "404", description = "Receta no encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @RequestBody RecipeDTO recipeDTO) {
        return ResponseEntity.ok(recipeService.updateRecipe(id, recipeDTO));
    }

    @Operation(summary = "Obtener receta por ID", description = "Devuelve una receta específica")
    @ApiResponse(responseCode = "200", description = "Receta encontrada")
    @ApiResponse(responseCode = "404", description = "Receta no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.getRecipe(id));
    }

    @Operation(summary = "Listar recetas", description = "Obtiene todas las recetas activas")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return ResponseEntity.ok(recipeService.getAllActiveRecipes());
    }

    @Operation(summary = "Buscar recetas", description = "Busca recetas por palabra clave")
    @ApiResponse(responseCode = "200", description = "Resultados encontrados")
    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipes(@RequestParam String keyword) {
        return ResponseEntity.ok(recipeService.searchRecipes(keyword));
    }

    @Operation(summary = "Desactivar receta", description = "Desactiva una receta existente")
    @ApiResponse(responseCode = "200", description = "Receta desactivada")
    @ApiResponse(responseCode = "404", description = "Receta no encontrada")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateRecipe(@PathVariable Long id) {
        recipeService.deactivateRecipe(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Activar receta", description = "Activa una receta existente")
    @ApiResponse(responseCode = "200", description = "Receta activada")
    @ApiResponse(responseCode = "404", description = "Receta no encontrada")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateRecipe(@PathVariable Long id) {
        recipeService.activateRecipe(id);
        return ResponseEntity.ok().build();
    }
}