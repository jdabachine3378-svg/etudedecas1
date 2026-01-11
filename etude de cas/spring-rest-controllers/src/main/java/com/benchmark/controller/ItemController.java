package com.benchmark.controller;

import com.benchmark.dto.ItemDTO;
import com.benchmark.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour les opérations sur les items
 * Gère les requêtes HTTP et délègue la logique métier à ItemService
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     * GET /items - Récupère tous les items avec pagination
     * 
     * @param page Numéro de page (par défaut: 0)
     * @param size Taille de page (par défaut: 50)
     * @param categoryId ID de la catégorie (optionnel) pour filtrer les items
     * @return Page d'items
     */
    @GetMapping
    public ResponseEntity<Page<ItemDTO>> getAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) Long categoryId) {

        Pageable pageable = PageRequest.of(page, size);

        if (categoryId != null) {
            Page<ItemDTO> items = itemService.findByCategoryId(categoryId, pageable);
            return ResponseEntity.ok(items);
        } else {
            Page<ItemDTO> items = itemService.findAll(pageable);
            return ResponseEntity.ok(items);
        }
    }

    /**
     * GET /items/{id} - Récupère un item par son ID
     * 
     * @param id ID de l'item
     * @return Détails de l'item
     */
    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getItemById(@PathVariable Long id) {
        ItemDTO item = itemService.findById(id);
        return ResponseEntity.ok(item);
    }

    /**
     * POST /items - Crée un nouvel item
     * 
     * @param itemDTO Données de l'item
     * @return Item créé
     */
    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@RequestBody ItemDTO itemDTO) {
        ItemDTO created = itemService.create(itemDTO);
        return ResponseEntity.ok(created);
    }

    /**
     * PUT /items/{id} - Met à jour un item existant
     * 
     * @param id ID de l'item
     * @param itemDTO Données mises à jour de l'item
     * @return Item mis à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<ItemDTO> updateItem(
            @PathVariable Long id,
            @RequestBody ItemDTO itemDTO) {

        ItemDTO updated = itemService.update(id, itemDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /items/{id} - Supprime un item
     * 
     * @param id ID de l'item
     * @return Réponse sans contenu en cas de succès
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}