package com.benchmark.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Entité JPA représentant un item
 * Un item appartient à une catégorie (relation ManyToOne)
 */
@Entity
@Table(name = "items")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Item {
    /** Identifiant unique de l'item */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nom de l'item */
    @Column(nullable = false)
    private String name;

    /** Prix de l'item */
    @Column(nullable = false)
    private Double price;

    /** Quantité en stock de l'item */
    @Column(nullable = false)
    private Integer stock;

    /** Description de l'item */
    @Column(columnDefinition = "TEXT")
    private String description;

    /** Catégorie à laquelle appartient l'item (relation ManyToOne) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties({"items", "hibernateLazyInitializer", "handler"})
    private Category category;

    /** Date de création de l'item */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** Date de dernière mise à jour de l'item */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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