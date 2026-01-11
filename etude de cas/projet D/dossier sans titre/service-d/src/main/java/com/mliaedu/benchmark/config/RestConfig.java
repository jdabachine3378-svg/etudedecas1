package com.mliaedu.benchmark.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * Configuration pour Spring Data REST
 * Configure l'exposition des IDs et les paramètres CORS
 */
@Configuration
public class RestConfig implements RepositoryRestConfigurer {

    /**
     * Configure Spring Data REST
     * Expose les IDs des entités dans les réponses JSON et configure CORS
     * 
     * @param config Configuration de Spring Data REST
     * @param cors Registre CORS pour la configuration des origines autorisées
     */
    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config, CorsRegistry cors) {
        
        // Exposer les IDs dans les réponses JSON (par défaut, Spring Data REST ne les expose pas)
        config.exposeIdsFor(
            com.mliaedu.benchmark.entity.Category.class,
            com.mliaedu.benchmark.entity.Item.class
        );

        // Configuration CORS pour autoriser toutes les origines (à restreindre en production)
        cors.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .allowedHeaders("*");
    }
}

