package com.benchmark.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) pour les items
 * Utilisé pour transférer les données entre les couches de l'application
 */
@Data
public class ItemDTO {
    private Long id;
    private String name;
    private Double price;
    private Integer stock;
    private String description;
    private Long categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ItemDTO() {}

    public ItemDTO(Long id, String name, Double price, Integer stock, String description, Long categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.categoryId = categoryId;
    }
}