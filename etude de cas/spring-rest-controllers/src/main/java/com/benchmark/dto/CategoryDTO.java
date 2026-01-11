package com.benchmark.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) pour les catégories
 * Utilisé pour transférer les données entre les couches de l'application
 */
@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CategoryDTO() {}

    public CategoryDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}