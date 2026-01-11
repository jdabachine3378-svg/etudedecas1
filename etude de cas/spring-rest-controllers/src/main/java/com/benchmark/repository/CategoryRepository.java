package com.benchmark.repository;

import com.benchmark.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository pour les opérations de base de données sur les catégories
 * Étend JpaRepository pour bénéficier des méthodes CRUD standards
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Trouve une catégorie par son ID avec ses items (eager loading)
     * 
     * @param id ID de la catégorie
     * @return Catégorie avec ses items chargés
     */
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.items WHERE c.id = :id")
    Category findByIdWithItems(@Param("id") Long id);

    /**
     * Récupère toutes les catégories avec pagination
     * 
     * @param pageable Paramètres de pagination
     * @return Page de catégories
     */
    Page<Category> findAll(Pageable pageable);
}