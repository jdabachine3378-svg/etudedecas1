# Instructions de Test - Variante D (Spring Data REST)

## 🚀 Démarrage Rapide

### 1. Prérequis

Assurez-vous d'avoir installé :
- Java 21
- Maven 3.8+
- PostgreSQL 14+
- Python 3 (pour le générateur de données)

### 2. Configuration de la Base de Données

```bash
# Créer la base de données
createdb -U postgres benchmark_db

# Exécuter le script SQL
psql -U postgres -d benchmark_db -f src/main/resources/db/schema.sql
```

### 3. Génération des Données

```bash
# Installer psycopg2 si nécessaire
pip install psycopg2-binary

# Générer les données (2000 catégories, 100000 items)
cd service-d
python3 src/main/resources/db/generate_data.py
```

### 4. Configuration de l'Application

Vérifiez/modifiez `src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/benchmark_db
spring.datasource.username=postgres
spring.datasource.password=postgres
server.port=8083
```

### 5. Compilation et Lancement

```bash
# Compiler
mvn clean package

# Lancer l'application
mvn spring-boot:run

# OU avec le JAR
java -jar target/service-d-1.0.0.jar
```

L'application démarre sur **http://localhost:8083**

## ✅ Tests Manuels

### Test 1: Vérifier que l'application démarre

```bash
curl http://localhost:8083/actuator/health
```

Réponse attendue :
```json
{"status":"UP"}
```

### Test 2: Liste des catégories

```bash
curl "http://localhost:8083/categories?page=0&size=5"
```

Vous devriez voir une réponse HAL avec :
- `_embedded.categories` : tableau de catégories
- `_links` : liens de navigation
- `page` : informations de pagination

### Test 3: Détail d'une catégorie

```bash
curl http://localhost:8083/categories/1
```

### Test 4: Liste des items

```bash
curl "http://localhost:8083/items?page=0&size=5"
```

### Test 5: Items par catégorie (endpoint personnalisé)

```bash
curl "http://localhost:8083/items?categoryId=1&page=0&size=5"
```

### Test 6: Relation inverse (items d'une catégorie)

```bash
curl "http://localhost:8083/categories/1/items?page=0&size=5"
```

### Test 7: Création d'une catégorie

```bash
curl -X POST http://localhost:8083/categories \
  -H "Content-Type: application/json" \
  -d '{
    "code": "CAT_TEST_001",
    "name": "Test Category"
  }'
```

### Test 8: Création d'un item

```bash
curl -X POST http://localhost:8083/items \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "SKU_TEST_001",
    "name": "Test Item",
    "price": 99.99,
    "stock": 50,
    "category": "http://localhost:8083/categories/1"
  }'
```

**Note** : Dans Spring Data REST, les relations sont référencées par URL HAL.

### Test 9: Modification d'un item

```bash
curl -X PUT http://localhost:8083/items/1 \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "SKU_TEST_001",
    "name": "Updated Item",
    "price": 149.99,
    "stock": 100,
    "category": "http://localhost:8083/categories/1"
  }'
```

### Test 10: Suppression

```bash
curl -X DELETE http://localhost:8083/items/1
curl -X DELETE http://localhost:8083/categories/1
```

## 📊 Tests avec JMeter

### Configuration de Base

1. **Ouvrir JMeter**
2. **Créer un Thread Group** :
   - Nom : "Test Variante D"
   - Number of Threads : 50
   - Ramp-up Period : 60
   - Loop Count : Forever
   - Duration : 600 secondes (10 min)

3. **Ajouter HTTP Request Defaults** :
   - Server Name : `localhost`
   - Port Number : `8083`
   - Protocol : `http`

4. **Créer un CSV Data Set Config** :
   - Filename : `ids.csv` (créer ce fichier avec categoryId et itemId)
   - Variable Names : `categoryId,itemId`
   - Delimiter : `,`

### Scénario 1: READ-heavy (relation incluse)

Créer les requêtes suivantes avec les poids indiqués :

#### 50% - GET /items?page=&size=50
- **HTTP Request** :
  - Method : GET
  - Path : `/items`
  - Parameters :
    - `page` : `${__Random(0,100)}`
    - `size` : `50`

#### 20% - GET /items?categoryId=...&page=&size=
- **HTTP Request** :
  - Method : GET
  - Path : `/items`
  - Parameters :
    - `categoryId` : `${categoryId}`
    - `page` : `${__Random(0,10)}`
    - `size` : `20`

#### 20% - GET /categories/{id}/items?page=&size=
- **HTTP Request** :
  - Method : GET
  - Path : `/categories/${categoryId}/items`
  - Parameters :
    - `page` : `${__Random(0,10)}`
    - `size` : `20`

#### 10% - GET /categories?page=&size=
- **HTTP Request** :
  - Method : GET
  - Path : `/categories`
  - Parameters :
    - `page` : `${__Random(0,100)}`
    - `size` : `20`

**Configuration du Thread Group** :
- Threads : 50 → 100 → 200 (utiliser Stepping Thread Group)
- Ramp-up : 60s
- Durée par palier : 10 min

### Scénario 2: JOIN-filter ciblé

#### 70% - GET /items?categoryId=...&page=&size=
- **HTTP Request** :
  - Method : GET
  - Path : `/items`
  - Parameters :
    - `categoryId` : `${categoryId}`
    - `page` : `${__Random(0,10)}`
    - `size` : `20`

#### 30% - GET /items/{id}
- **HTTP Request** :
  - Method : GET
  - Path : `/items/${itemId}`

**Configuration** :
- Threads : 60 → 120
- Ramp-up : 60s
- Durée : 8 min/palier

### Scénario 3: MIXED (écritures)

#### 40% - GET /items?page=...
- **HTTP Request** : GET `/items?page=${__Random(0,100)}&size=20`

#### 20% - POST /items (1 KB)
- **HTTP Request** :
  - Method : POST
  - Path : `/items`
  - Body Data :
```json
{
  "sku": "SKU_${__Random(1,999999)}",
  "name": "Item ${__Random(1,9999)}",
  "price": ${__Random(10,1000)},
  "stock": ${__Random(0,1000)},
  "category": "http://localhost:8083/categories/${categoryId}"
}
```

#### 10% - PUT /items/{id} (1 KB)
- **HTTP Request** :
  - Method : PUT
  - Path : `/items/${itemId}`
  - Body Data : (similaire au POST)

#### 10% - DELETE /items/{id}
- **HTTP Request** :
  - Method : DELETE
  - Path : `/items/${itemId}`

#### 10% - POST /categories (0.5-1 KB)
- **HTTP Request** :
  - Method : POST
  - Path : `/categories`
  - Body Data :
```json
{
  "code": "CAT_${__Random(1,999999)}",
  "name": "Category ${__Random(1,9999)}"
}
```

#### 10% - PUT /categories/{id}
- **HTTP Request** :
  - Method : PUT
  - Path : `/categories/${categoryId}`
  - Body Data : (similaire au POST)

**Configuration** :
- Threads : 50 → 100
- Ramp-up : 60s
- Durée : 10 min/palier

### Scénario 4: HEAVY-body (payload 5 KB)

#### 50% - POST /items (5 KB)
- Créer un payload JSON avec une description textuelle de ~5 KB

#### 50% - PUT /items/{id} (5 KB)
- Même payload que POST

**Configuration** :
- Threads : 30 → 60
- Ramp-up : 60s
- Durée : 8 min/palier

### Configuration Backend Listener (InfluxDB)

1. **Ajouter Backend Listener** :
   - Backend Listener Implementation : `InfluxDBBackendListenerClient`
   - InfluxDB URL : `http://localhost:8086`
   - InfluxDB Database : `jmeter`
   - InfluxDB Organization : `perf`
   - InfluxDB Bucket : `jmeter`
   - InfluxDB Token : (votre token)

### Listeners à Désactiver

Pendant les runs de performance, **désactiver** :
- View Results Tree
- Summary Report
- Graph Results

Garder uniquement :
- Backend Listener (InfluxDB)
- Aggregate Report (pour un résumé rapide)

## 📈 Monitoring avec Prometheus

### Métriques Disponibles

Les métriques Prometheus sont disponibles sur :
```
http://localhost:8083/actuator/prometheus
```

### Métriques Importantes

- `jvm_memory_used_bytes` : Mémoire utilisée
- `jvm_memory_max_bytes` : Mémoire maximale
- `process_cpu_usage` : Utilisation CPU
- `http_server_requests_seconds` : Temps de réponse HTTP
- `hikari_connections_active` : Connexions HikariCP actives
- `hikari_connections_idle` : Connexions HikariCP inactives

### Configuration Grafana

1. Ajouter Prometheus comme source de données
2. URL : `http://localhost:9090` (Prometheus)
3. Créer des dashboards pour :
   - JVM Memory (Heap)
   - CPU Usage
   - HTTP Request Duration
   - HikariCP Connections

## 🔍 Vérifications Importantes

### Format HAL

Les réponses Spring Data REST utilisent le format HAL. Vérifiez que :
- Les réponses incluent `_links` et `_embedded`
- La pagination utilise `page` avec `totalElements`, `totalPages`, etc.

### Relations

Vérifiez que les relations fonctionnent :
- `GET /items/1/category` retourne la catégorie
- `GET /categories/1/items` retourne les items

### Performance N+1

Surveillez les logs SQL (si activés) pour détecter les problèmes N+1. Utilisez les méthodes optimisées avec `JOIN FETCH` si nécessaire.

## 🐛 Dépannage

### Erreur : "Connection refused"
- Vérifiez que PostgreSQL est démarré
- Vérifiez les credentials dans `application.properties`

### Erreur : "404 Not Found"
- Vérifiez que `spring.data.rest.base-path=/` dans `application.properties`
- Vérifiez le port (8083)

### Erreur : "415 Unsupported Media Type"
- Utilisez `Content-Type: application/json` pour POST/PUT
- Pour les relations, utilisez l'URL HAL : `"category": "http://localhost:8083/categories/1"`

### Problème N+1 Queries
- Utilisez `findByCategoryIdWithCategory` au lieu de `findByCategoryId`
- Ou configurez `@EntityGraph` sur les repositories

## 📝 Notes Importantes

1. **Format HAL** : Plus verbeux que JSON classique, peut impacter les performances
2. **Relations automatiques** : Spring Data REST expose automatiquement les relations JPA
3. **Pagination** : Automatique et standardisée
4. **Port** : 8083 pour éviter les conflits avec A et C

## ✅ Checklist de Test

- [ ] Application démarre sans erreur
- [ ] Base de données connectée
- [ ] Données générées (2000 catégories, 100000 items)
- [ ] Endpoints GET fonctionnent
- [ ] Endpoints POST/PUT/DELETE fonctionnent
- [ ] Relations fonctionnent (`/categories/{id}/items`)
- [ ] Pagination fonctionne
- [ ] Format HAL correct
- [ ] Prometheus metrics disponibles
- [ ] Tests JMeter configurés
- [ ] Backend Listener InfluxDB configuré

Bon test ! 🚀

