package com.mliaedu.benchmark.repository;

import com.mliaedu.benchmark.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Repository pour les opérations de base de données sur les catégories
 * Exposé automatiquement via Spring Data REST sur le chemin /categories
 */
@RepositoryRestResource(collectionResourceRel = "categories", path = "categories")
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Vérifie si une catégorie existe par son code
     * Non exposé via REST (utilisé uniquement en interne)
     * 
     * @param code Code de la catégorie
     * @return true si la catégorie existe
     */
    @RestResource(exported = false)
    boolean existsByCode(String code);

    /**
     * Recherche une catégorie par son code
     * Exposé via REST sur /categories/search/by-code?code=...
     * 
     * @param code Code de la catégorie
     * @return Catégorie trouvée
     */
    @RestResource(path = "by-code", rel = "byCode")
    Category findByCode(@Param("code") String code);
}

