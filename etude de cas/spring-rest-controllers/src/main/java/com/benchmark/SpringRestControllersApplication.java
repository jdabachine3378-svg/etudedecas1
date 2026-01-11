package com.benchmark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application principale Spring Boot pour le benchmark des contrôleurs REST
 * Cette classe démarre l'application et configure Spring Boot
 */
@SpringBootApplication
public class SpringRestControllersApplication {
    /**
     * Point d'entrée principal de l'application
     * 
     * @param args Arguments de la ligne de commande
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringRestControllersApplication.class, args);
    }
}