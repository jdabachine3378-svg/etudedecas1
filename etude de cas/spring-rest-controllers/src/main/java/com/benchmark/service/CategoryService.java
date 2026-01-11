package com.benchmark.service;

import com.benchmark.dto.CategoryDTO;
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
 * Service métier pour les opérations sur les catégories
 * Gère la logique métier et orchestre les opérations du repository
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    /**
     * Récupère toutes les catégories avec pagination
     * 
     * @param pageable Paramètres de pagination
     * @return Page de DTOs de catégories
     */
    public Page<CategoryDTO> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    /**
     * Récupère une catégorie par son ID
     * 
     * @param id ID de la catégorie
     * @return DTO de la catégorie
     * @throws RuntimeException si la catégorie n'est pas trouvée
     */
    public CategoryDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return convertToDTO(category);
    }

    /**
     * Crée une nouvelle catégorie
     * 
     * @param categoryDTO DTO contenant les données de la catégorie
     * @return DTO de la catégorie créée
     */
    public CategoryDTO create(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        Category saved = categoryRepository.save(category);
        return convertToDTO(saved);
    }

    /**
     * Met à jour une catégorie existante
     * 
     * @param id ID de la catégorie
     * @param categoryDTO DTO contenant les données mises à jour
     * @return DTO de la catégorie mise à jour
     * @throws RuntimeException si la catégorie n'est pas trouvée
     */
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        Category updated = categoryRepository.save(category);
        return convertToDTO(updated);
    }

    /**
     * Supprime une catégorie
     * 
     * @param id ID de la catégorie à supprimer
     */
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    /**
     * Récupère les items d'une catégorie avec pagination
     * 
     * @param categoryId ID de la catégorie
     * @param pageable Paramètres de pagination
     * @return Page de DTOs d'items
     */
    public Page<ItemDTO> getCategoryItems(Long categoryId, Pageable pageable) {
        return itemRepository.findByCategoryId(categoryId, pageable)
                .map(this::convertItemToDTO);
    }

    /**
     * Convertit une entité Category en DTO
     * 
     * @param category Entité Category
     * @return DTO Category
     */
    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }

    /**
     * Convertit une entité Item en DTO
     * 
     * @param item Entité Item
     * @return DTO Item
     */
    private ItemDTO convertItemToDTO(Item item) {
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