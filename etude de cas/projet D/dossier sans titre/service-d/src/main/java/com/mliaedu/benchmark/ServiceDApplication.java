package com.mliaedu.benchmark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * Application principale Spring Boot pour le service D
 * Utilise Spring Data REST pour exposer automatiquement les repositories
 */
@SpringBootApplication
@ComponentScan(
    basePackages = "com.mliaedu.benchmark",
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = com.mliaedu.benchmark.util.DataGenerator.class
    )
)
public class ServiceDApplication {

    /**
     * Point d'entrée principal de l'application
     * 
     * @param args Arguments de la ligne de commande
     */
    public static void main(String[] args) {
        SpringApplication.run(ServiceDApplication.class, args);
    }
}

