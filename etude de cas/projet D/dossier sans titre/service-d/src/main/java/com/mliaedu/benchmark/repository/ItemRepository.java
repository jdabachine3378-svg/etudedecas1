package com.mliaedu.benchmark.repository;

import com.mliaedu.benchmark.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Repository pour les opérations de base de données sur les items
 * Exposé automatiquement via Spring Data REST sur le chemin /items
 */
@RepositoryRestResource(collectionResourceRel = "items", path = "items")
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Recherche les items d'une catégorie avec pagination
     * Exposé via REST sur /items/search/by-category?categoryId=123&page=0&size=20
     * 
     * @param categoryId ID de la catégorie
     * @param pageable Paramètres de pagination
     * @return Page d'items de la catégorie
     */
    @RestResource(path = "by-category", rel = "byCategory")
    Page<Item> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    /**
     * Version optimisée avec JOIN FETCH pour éviter le problème N+1
     * Charge la catégorie en même temps que les items
     * Exposé via REST sur /items/search/by-category-optimized?categoryId=123&page=0&size=20
     * 
     * @param categoryId ID de la catégorie
     * @param pageable Paramètres de pagination
     * @return Page d'items avec leur catégorie chargée
     */
    @Query("SELECT i FROM Item i JOIN FETCH i.category WHERE i.category.id = :categoryId")
    @RestResource(path = "by-category-optimized", rel = "byCategoryOptimized")
    Page<Item> findByCategoryIdWithCategory(@Param("categoryId") Long categoryId, Pageable pageable);

    /**
     * Recherche un item par son SKU
     * Non exposé via REST (utilisé uniquement en interne)
     * 
     * @param sku SKU de l'item
     * @return Item trouvé
     */
    @RestResource(exported = false)
    Item findBySku(String sku);
}

