package com.benchmark.service;

import com.benchmark.dto.ItemDTO;
import com.benchmark.entity.Category;
import com.benchmark.entity.Item;
import com.benchmark.repository.CategoryRepository;
import com.benchmark.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service métier pour les opérations sur les items
 * Gère la logique métier et orchestre les opérations du repository
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Récupère tous les items avec pagination
     * 
     * @param pageable Paramètres de pagination
     * @return Page de DTOs d'items
     */
    public Page<ItemDTO> findAll(Pageable pageable) {
        return itemRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    /**
     * Récupère un item par son ID
     * 
     * @param id ID de l'item
     * @return DTO de l'item
     * @throws RuntimeException si l'item n'est pas trouvé
     */
    public ItemDTO findById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        return convertToDTO(item);
    }

    /**
     * Récupère les items d'une catégorie avec pagination
     * 
     * @param categoryId ID de la catégorie
     * @param pageable Paramètres de pagination
     * @return Page de DTOs d'items
     */
    public Page<ItemDTO> findByCategoryId(Long categoryId, Pageable pageable) {
        return itemRepository.findByCategoryId(categoryId, pageable)
                .map(this::convertToDTO);
    }

    /**
     * Crée un nouvel item
     * 
     * @param itemDTO DTO contenant les données de l'item
     * @return DTO de l'item créé
     * @throws RuntimeException si la catégorie n'est pas trouvée
     */
    public ItemDTO create(ItemDTO itemDTO) {
        Category category = categoryRepository.findById(itemDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + itemDTO.getCategoryId()));

        Item item = new Item();
        item.setName(itemDTO.getName());
        item.setPrice(itemDTO.getPrice());
        item.setStock(itemDTO.getStock());
        item.setDescription(itemDTO.getDescription());
        item.setCategory(category);

        Item saved = itemRepository.save(item);
        return convertToDTO(saved);
    }

    /**
     * Met à jour un item existant
     * 
     * @param id ID de l'item
     * @param itemDTO DTO contenant les données mises à jour
     * @return DTO de l'item mis à jour
     * @throws RuntimeException si l'item ou la catégorie n'est pas trouvé
     */
    public ItemDTO update(Long id, ItemDTO itemDTO) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        // Met à jour la catégorie si elle a changé
        if (!item.getCategory().getId().equals(itemDTO.getCategoryId())) {
            Category category = categoryRepository.findById(itemDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + itemDTO.getCategoryId()));
            item.setCategory(category);
        }

        item.setName(itemDTO.getName());
        item.setPrice(itemDTO.getPrice());
        item.setStock(itemDTO.getStock());
        item.setDescription(itemDTO.getDescription());

        Item updated = itemRepository.save(item);
        return convertToDTO(updated);
    }

    /**
     * Supprime un item
     * 
     * @param id ID de l'item à supprimer
     */
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    /**
     * Convertit une entité Item en DTO
     * 
     * @param item Entité Item
     * @return DTO Item
     */
    private ItemDTO convertToDTO(Item item) {
        ItemDTO dto = new ItemDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setPrice(item.getPrice());
        dto.setStock(item.getStock());
        dto.setDescription(item.getDescription());
        dto.setCategoryId(item.getCategory().getId());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());
        return dto;
    }
}