package com.benchmark.repository;

import com.benchmark.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository pour les opérations de base de données sur les items
 * Étend JpaRepository pour bénéficier des méthodes CRUD standards
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * Récupère tous les items avec pagination
     * 
     * @param pageable Paramètres de pagination
     * @return Page d'items
     */
    Page<Item> findAll(Pageable pageable);

    /**
     * Récupère les items d'une catégorie avec pagination
     * 
     * @param categoryId ID de la catégorie
     * @param pageable Paramètres de pagination
     * @return Page d'items de la catégorie
     */
    Page<Item> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * Récupère les items d'une catégorie avec la catégorie chargée (eager loading)
     * 
     * @param categoryId ID de la catégorie
     * @param pageable Paramètres de pagination
     * @return Page d'items avec leur catégorie chargée
     */
    @Query("SELECT i FROM Item i JOIN FETCH i.category WHERE i.category.id = :categoryId")
    Page<Item> findByCategoryIdWithCategory(@Param("categoryId") Long categoryId, Pageable pageable);
}