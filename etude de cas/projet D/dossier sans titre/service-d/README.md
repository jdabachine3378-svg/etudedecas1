# Service D - Spring Data REST

Variante D du benchmark de performances des Web Services REST.

## Stack Technique

- **Framework REST**: Spring Boot 3.2.0 + Spring Data REST
- **ORM**: Hibernate (via Spring Data JPA)
- **Base de données**: PostgreSQL 14+
- **Sérialisation JSON**: Jackson
- **Type de contrôleurs**: Exposition automatique (Spring Data REST)

## Architecture

```
Clients HTTP
    ↓
Spring Data REST (Automatic Exposure)
    ↓
Repository Interfaces (JpaRepository)
    ↓
Hibernate/JPA
    ↓
PostgreSQL
```

## Endpoints REST Exposés

### Catégories (`/categories`)

- `GET /categories` - Liste paginée des catégories
- `GET /categories/{id}` - Détail d'une catégorie
- `POST /categories` - Création d'une catégorie
- `PUT /categories/{id}` - Modification d'une catégorie
- `DELETE /categories/{id}` - Suppression d'une catégorie
- `GET /categories/{id}/items` - Items d'une catégorie (relation inverse)

### Items (`/items`)

- `GET /items` - Liste paginée des items
- `GET /items/{id}` - Détail d'un item
- `GET /items?categoryId={id}&page={page}&size={size}` - Items par catégorie (via contrôleur personnalisé)
- `POST /items` - Création d'un item
- `PUT /items/{id}` - Modification d'un item
- `DELETE /items/{id}` - Suppression d'un item
- `GET /items/{id}/category` - Catégorie d'un item (relation)

### Recherches Spring Data REST

- `GET /items/search/findByCategoryId?categoryId={id}` - Recherche par catégorie
- `GET /items/search/findByCategoryIdWithCategory?categoryId={id}` - Version optimisée avec JOIN FETCH

## Format de Réponse (HAL)

Spring Data REST utilise le format HAL (Hypertext Application Language) :

```json
{
  "_embedded": {
    "items": [
      {
        "id": 1,
        "name": "Item 1",
        "price": 29.99,
        "stock": 100,
        "_links": {
          "self": { "href": "http://localhost:8083/items/1" },
          "category": { "href": "http://localhost:8083/items/1/category" }
        }
      }
    ]
  },
  "_links": {
    "self": { "href": "http://localhost:8083/items?page=0&size=20" },
    "next": { "href": "http://localhost:8083/items?page=1&size=20" }
  },
  "page": {
    "size": 20,
    "totalElements": 100000,
    "totalPages": 5000,
    "number": 0
  }
}
```

## Prérequis

- Java 21
- Maven 3.8+
- PostgreSQL 14+
- Python 3 (pour le générateur de données)

## Installation et Configuration

### 1. Base de données PostgreSQL

```bash
# Créer la base de données
createdb -U postgres benchmark_db

# Exécuter le script SQL
psql -U postgres -d benchmark_db -f src/main/resources/db/schema.sql
```

### 2. Génération des données

```bash
# Installer psycopg2 si nécessaire
pip install psycopg2-binary

# Générer les données (2000 catégories, 100000 items)
python3 src/main/resources/db/generate_data.py
```

### 3. Configuration

Modifier `src/main/resources/application.properties` si nécessaire :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/benchmark_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 4. Compilation et exécution

```bash
# Compiler le projet
mvn clean package

# Exécuter l'application
mvn spring-boot:run

# Ou avec le JAR
java -jar target/service-d-1.0.0.jar
```

L'application démarre sur le port **8083** par défaut.

## Tests Manuels

### 1. Test des catégories

```bash
# Liste des catégories (page 0, taille 20)
curl http://localhost:8083/categories?page=0&size=20

# Détail d'une catégorie
curl http://localhost:8083/categories/1

# Création d'une catégorie
curl -X POST http://localhost:8083/categories \
  -H "Content-Type: application/json" \
  -d '{"code":"CAT_TEST","name":"Test Category"}'

# Modification
curl -X PUT http://localhost:8083/categories/1 \
  -H "Content-Type: application/json" \
  -d '{"code":"CAT_TEST","name":"Updated Category"}'

# Suppression
curl -X DELETE http://localhost:8083/categories/1
```

### 2. Test des items

```bash
# Liste des items
curl http://localhost:8083/items?page=0&size=50

# Détail d'un item
curl http://localhost:8083/items/1

# Items par catégorie (endpoint personnalisé)
curl "http://localhost:8083/items?categoryId=1&page=0&size=20"

# Items d'une catégorie (relation Spring Data REST)
curl http://localhost:8083/categories/1/items?page=0&size=20

# Création d'un item
curl -X POST http://localhost:8083/items \
  -H "Content-Type: application/json" \
  -d '{
    "sku":"SKU_TEST_001",
    "name":"Test Item",
    "price":99.99,
    "stock":50,
    "category":"http://localhost:8083/categories/1"
  }'

# Modification
curl -X PUT http://localhost:8083/items/1 \
  -H "Content-Type: application/json" \
  -d '{
    "sku":"SKU_TEST_001",
    "name":"Updated Item",
    "price":149.99,
    "stock":100,
    "category":"http://localhost:8083/categories/1"
  }'

# Suppression
curl -X DELETE http://localhost:8083/items/1
```

### 3. Test des relations

```bash
# Catégorie d'un item
curl http://localhost:8083/items/1/category

# Items d'une catégorie
curl http://localhost:8083/categories/1/items?page=0&size=20
```

## Tests avec JMeter

### Configuration JMeter

1. **HTTP Request Defaults** :
   - Server Name: `localhost`
   - Port: `8083`
   - Protocol: `http`

2. **CSV Data Set Config** :
   - Créer un fichier CSV avec les IDs existants :
     ```csv
     categoryId,itemId
     1,1
     1,2
     2,51
     ...
     ```

### Scénarios de Test

#### Scénario 1: READ-heavy (relation incluse)

- 50% `GET /items?page=0&size=50`
- 20% `GET /items?categoryId={categoryId}&page=0&size=20`
- 20% `GET /categories/{id}/items?page=0&size=20`
- 10% `GET /categories?page=0&size=20`

#### Scénario 2: JOIN-filter ciblé

- 70% `GET /items?categoryId={categoryId}&page=0&size=20`
- 30% `GET /items/{id}`

#### Scénario 3: MIXED (écritures)

- 40% `GET /items?page=0&size=20`
- 20% `POST /items` (payload 1 KB)
- 10% `PUT /items/{id}` (payload 1 KB)
- 10% `DELETE /items/{id}`
- 10% `POST /categories` (payload 0.5-1 KB)
- 10% `PUT /categories/{id}`

#### Scénario 4: HEAVY-body

- 50% `POST /items` (payload 5 KB)
- 50% `PUT /items/{id}` (payload 5 KB)

## Monitoring

### Prometheus Metrics

Les métriques sont disponibles sur :
```
http://localhost:8083/actuator/prometheus
```

### Health Check

```
http://localhost:8083/actuator/health
```

### Métriques JVM

```
http://localhost:8083/actuator/metrics
```

## Points d'Attention pour les Tests

1. **Format HAL** : Les réponses incluent des liens hypermédia, ce qui peut augmenter la taille des réponses comparé aux variantes A et C.

2. **Relations automatiques** : Spring Data REST expose automatiquement les relations, ce qui peut causer des problèmes N+1 si non optimisé.

3. **Optimisation JOIN FETCH** : Utiliser `findByCategoryIdWithCategory` pour éviter les requêtes N+1.

4. **Pagination** : La pagination est automatique et suit le format HAL.

## Comparaison avec les Variantes A et C

| Aspect | Variante D (Spring Data REST) |
|--------|------------------------------|
| Code à écrire | Très peu (repositories uniquement) |
| Format réponse | HAL/JSON (plus verbeux) |
| Relations | Automatiques |
| Performance | À mesurer (overhead HAL possible) |
| Flexibilité | Limitée (conventions Spring Data REST) |

## Dépannage

### Erreur de connexion à la base de données

Vérifier que PostgreSQL est démarré et que les credentials dans `application.properties` sont corrects.

### Erreur 404 sur les endpoints

Vérifier que le `base-path` dans `application.properties` est `/` (pas `/api`).

### Problème N+1 queries

Utiliser les méthodes optimisées avec `JOIN FETCH` ou configurer `@EntityGraph`.

## Livrables

- ✅ Code source de la variante D
- ✅ Configuration Spring Data REST
- ✅ Script SQL de référence
- ✅ Générateur de données Python
- ✅ Documentation de test

## Auteur

MLIAEdu Platform - Étude de cas Benchmark REST

