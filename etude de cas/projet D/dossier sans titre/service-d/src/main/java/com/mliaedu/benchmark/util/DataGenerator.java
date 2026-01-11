package com.mliaedu.benchmark.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import com.mliaedu.benchmark.entity.Category;
import com.mliaedu.benchmark.entity.Item;
import com.mliaedu.benchmark.repository.CategoryRepository;
import com.mliaedu.benchmark.repository.ItemRepository;

import jakarta.persistence.EntityManager;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

/**
 * Générateur de données pour le benchmark
 * Génère 2000 catégories et 100000 items (environ 50 par catégorie)
 * 
 * Usage: 
 *   mvn exec:java -Dexec.mainClass="com.mliaedu.benchmark.util.DataGenerator" -Dspring.profiles.active=generator
 *   OU
 *   ./generate-data.sh
 */
@SpringBootApplication
@EntityScan("com.mliaedu.benchmark.entity")
@EnableJpaRepositories("com.mliaedu.benchmark.repository")
public class DataGenerator implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private EntityManager entityManager;

    private final Random random = new Random();

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DataGenerator.class);
        // Désactiver le serveur web et Spring Data REST pour le générateur
        app.setWebApplicationType(org.springframework.boot.WebApplicationType.NONE);
        app.setAdditionalProfiles("generator");
        app.run(args);
    }

    @Override
    @Transactional
    public void run(String... args) {
        System.out.println("=== Générateur de données pour le benchmark ===");
        System.out.println();

        // Nettoyer les données existantes avec TRUNCATE (plus efficace)
        System.out.println("Nettoyage des données existantes...");
        try {
            // Utiliser EntityManager pour TRUNCATE (plus rapide et ignore les contraintes)
            entityManager.createNativeQuery("TRUNCATE TABLE item CASCADE").executeUpdate();
            entityManager.createNativeQuery("TRUNCATE TABLE category CASCADE").executeUpdate();
            // Réinitialiser les séquences pour que les IDs commencent à 1
            entityManager.createNativeQuery("ALTER SEQUENCE category_id_seq RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE item_id_seq RESTART WITH 1").executeUpdate();
            System.out.println("✓ Données nettoyées\n");
        } catch (Exception e) {
            // Fallback sur deleteAll si TRUNCATE échoue
            System.out.println("  Utilisation de deleteAll()...");
            itemRepository.deleteAll();
            categoryRepository.deleteAll();
            System.out.println("✓ Données nettoyées\n");
        }

        // Générer les catégories
        int categoryCount = 2000;
        System.out.println("Génération de " + categoryCount + " catégories...");
        generateCategories(categoryCount);
        System.out.println("✓ " + categoryCount + " catégories créées\n");

        // Générer les items
        int itemsPerCategory = 50;
        System.out.println("Génération des items (environ " + itemsPerCategory + " par catégorie)...");
        long totalItems = generateItems(categoryCount, itemsPerCategory);
        System.out.println("✓ " + totalItems + " items créés\n");

        // Statistiques
        long finalCategoryCount = categoryRepository.count();
        long finalItemCount = itemRepository.count();
        System.out.println("=== Statistiques finales ===");
        System.out.println("Catégories: " + finalCategoryCount);
        System.out.println("Items: " + finalItemCount);
        System.out.println("\n✓ Génération terminée avec succès!");
    }

    private void generateCategories(int count) {
        for (int i = 1; i <= count; i++) {
            Category category = new Category();
            setField(category, "code", String.format("CAT_%04d", i));
            setField(category, "name", "Category " + i);
            setField(category, "updatedAt", LocalDateTime.now());
            setField(category, "items", new ArrayList<>());
            categoryRepository.saveAndFlush(category);

            if (i % 100 == 0) {
                System.out.println("  " + i + " catégories créées...");
            }
        }
    }

    private long generateItems(int categoryCount, int itemsPerCategory) {
        long itemCount = 0;
        
        // Récupérer toutes les catégories créées (plus fiable que par ID)
        var categories = categoryRepository.findAll();
        var categoryList = categories.stream().toList();
        
        if (categoryList.size() != categoryCount) {
            throw new RuntimeException("Expected " + categoryCount + " categories but found " + categoryList.size());
        }
        
        // Générer les items pour chaque catégorie
        for (int catIndex = 0; catIndex < categoryList.size(); catIndex++) {
            Category category = categoryList.get(catIndex);
            // Obtenir l'ID via réflexion
            Long catId = (Long) getField(category, "id");
            
            // Nombre variable d'items par catégorie (entre 45 et 55)
            int numItems = random.nextInt(11) + 45; // 45 à 55

            for (int j = 0; j < numItems; j++) {
                Item item = new Item();
                setField(item, "sku", String.format("SKU_%04d_%03d", catId, j));
                setField(item, "name", "Item " + (++itemCount));
                setField(item, "price", BigDecimal.valueOf(round(random.nextDouble() * 990 + 10, 2)));
                setField(item, "stock", random.nextInt(1001));
                setField(item, "category", category);
                setField(item, "updatedAt", LocalDateTime.now());
                
                itemRepository.save(item);
            }

            // Flush périodique pour améliorer les performances
            if (catId % 100 == 0) {
                itemRepository.flush();
                System.out.println("  " + itemCount + " items créés...");
            }
        }

        return itemCount;
    }

    private void setField(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException("Error setting field " + fieldName, e);
        }
    }

    private Object getField(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException("Error getting field " + fieldName, e);
        }
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}

