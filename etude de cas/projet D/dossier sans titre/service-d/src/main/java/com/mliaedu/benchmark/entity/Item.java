package com.mliaedu.benchmark.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entité JPA représentant un item
 * Un item appartient à une catégorie (relation ManyToOne)
 * Table indexée pour améliorer les performances des requêtes
 */
@Entity
@Table(name = "item", indexes = {
    @Index(name = "idx_item_category", columnList = "category_id"),
    @Index(name = "idx_item_updated_at", columnList = "updated_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    /** Identifiant unique de l'item */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Code SKU unique de l'item */
    @Column(name = "sku", unique = true, nullable = false, length = 64)
    private String sku;

    /** Nom de l'item */
    @Column(name = "name", nullable = false, length = 128)
    private String name;

    /** Prix de l'item (10 chiffres au total, 2 décimales) */
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /** Quantité en stock */
    @Column(name = "stock", nullable = false)
    private Integer stock;

    /** Catégorie à laquelle appartient l'item (relation ManyToOne) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /** Date de dernière mise à jour */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

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

