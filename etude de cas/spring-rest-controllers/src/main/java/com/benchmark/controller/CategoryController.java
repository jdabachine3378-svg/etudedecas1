package com.benchmark.controller;

import com.benchmark.dto.CategoryDTO;
import com.benchmark.dto.ItemDTO;
import com.benchmark.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour les opérations sur les catégories
 * Gère les requêtes HTTP et délègue la logique métier à CategoryService
 */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * GET /categories - Récupère toutes les catégories avec pagination
     * 
     * @param page Numéro de page (par défaut: 0)
     * @param size Taille de page (par défaut: 50)
     * @return Page de catégories
     */
    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryDTO> categories = categoryService.findAll(pageable);
        return ResponseEntity.ok(categories);
    }

    /**
     * GET /categories/{id} - Récupère une catégorie par son ID
     * 
     * @param id ID de la catégorie
     * @return Détails de la catégorie
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = categoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    /**
     * POST /categories - Crée une nouvelle catégorie
     * 
     * @param categoryDTO Données de la catégorie
     * @return Catégorie créée
     */
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO created = categoryService.create(categoryDTO);
        return ResponseEntity.ok(created);
    }

    /**
     * PUT /categories/{id} - Met à jour une catégorie existante
     * 
     * @param id ID de la catégorie
     * @param categoryDTO Données mises à jour de la catégorie
     * @return Catégorie mise à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDTO categoryDTO) {

        CategoryDTO updated = categoryService.update(id, categoryDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /categories/{id} - Supprime une catégorie
     * 
     * @param id ID de la catégorie
     * @return Réponse sans contenu en cas de succès
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /categories/{id}/items - Récupère les items d'une catégorie avec pagination
     * 
     * @param id ID de la catégorie
     * @param page Numéro de page (par défaut: 0)
     * @param size Taille de page (par défaut: 50)
     * @return Page d'items de la catégorie
     */
    @GetMapping("/{id}/items")
    public ResponseEntity<Page<ItemDTO>> getCategoryItems(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ItemDTO> items = categoryService.getCategoryItems(id, pageable);
        return ResponseEntity.ok(items);
    }
}