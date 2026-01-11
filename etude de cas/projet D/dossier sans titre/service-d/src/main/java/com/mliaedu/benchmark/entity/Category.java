package com.mliaedu.benchmark.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité JPA représentant une catégorie
 * Une catégorie peut contenir plusieurs items (relation OneToMany)
 */
@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    /** Identifiant unique de la catégorie */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Code unique de la catégorie */
    @Column(name = "code", unique = true, nullable = false, length = 32)
    private String code;

    /** Nom de la catégorie */
    @Column(name = "name", nullable = false, length = 128)
    private String name;

    /** Date de dernière mise à jour */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /** Liste des items appartenant à cette catégorie (relation OneToMany) */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> items = new ArrayList<>();

    /**
     * Méthode appelée avant la persistance ou la mise à jour d'une entité
     * Met à jour la date de modification
     */
    @PrePersist
    @PreUpdate
    protected void onCreate() {
        updatedAt = LocalDateTime.now();
    }
}

