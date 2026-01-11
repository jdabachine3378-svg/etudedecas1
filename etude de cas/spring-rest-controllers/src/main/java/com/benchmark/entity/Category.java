package com.benchmark.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité JPA représentant une catégorie
 * Une catégorie peut contenir plusieurs items (relation OneToMany)
 */
@Entity
@Table(name = "categories")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category {
    /** Identifiant unique de la catégorie */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nom de la catégorie */
    @Column(nullable = false)
    private String name;

    /** Description de la catégorie */
    @Column(columnDefinition = "TEXT")
    private String description;

    /** Date de création de la catégorie */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** Date de dernière mise à jour de la catégorie */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** Liste des items appartenant à cette catégorie (relation OneToMany) */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Item> items = new ArrayList<>();

    /**
     * Méthode appelée avant la persistance d'une nouvelle entité
     * Initialise les dates de création et de mise à jour
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Méthode appelée avant la mise à jour d'une entité
     * Met à jour la date de modification
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}