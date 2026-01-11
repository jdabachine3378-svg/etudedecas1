package com.mliaedu.benchmark.controller;

import com.mliaedu.benchmark.entity.Item;
import com.mliaedu.benchmark.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur personnalisé pour l'endpoint GET /items?categoryId=...
 * Spring Data REST expose déjà findByCategoryId, mais cet endpoint
 * permet d'avoir exactement la même URL que les variantes A et C
 */
@RestController
@RequestMapping("/items")
public class ItemCustomController {

    private final ItemRepository itemRepository;
    private final PagedResourcesAssembler<Item> assembler;

    public ItemCustomController(ItemRepository itemRepository, 
                                PagedResourcesAssembler<Item> assembler) {
        this.itemRepository = itemRepository;
        this.assembler = assembler;
    }

    /**
     * GET /items?categoryId=123&page=0&size=20
     * Compatible avec les variantes A et C
     * Retourne un format HAL pour être cohérent avec Spring Data REST
     */
    @GetMapping(params = "categoryId")
    public ResponseEntity<PagedModel<EntityModel<Item>>> getItemsByCategoryId(
            @RequestParam Long categoryId,
            Pageable pageable) {
        Page<Item> items = itemRepository.findByCategoryId(categoryId, pageable);
        return ResponseEntity.ok(assembler.toModel(items));
    }
}

