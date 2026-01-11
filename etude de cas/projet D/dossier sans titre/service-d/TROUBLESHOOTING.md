# 🔧 Guide de Dépannage - Variante D

## Problèmes Résolus ✅

### 1. Erreur "role postgres does not exist"
**Solution** : Configuration corrigée dans `application.properties` avec utilisateur `mac`

### 2. Erreur de compilation avec les setters Lombok
**Solution** : Utilisation de la réflexion (reflection) pour définir les champs directement

### 3. DataGenerator s'exécute automatiquement
**Solution** : Exclusion du scan de composants dans `ServiceDApplication`

### 4. Erreur "duplicate key" lors de la génération
**Solution** : Utilisation de `TRUNCATE` au lieu de `deleteAll()` pour nettoyer les données

## Problèmes Courants et Solutions

### Problème : Erreur "duplicate key value violates unique constraint"

**Cause** : Les données existent déjà dans la base de données

**Solution** : Nettoyer manuellement la base de données :

```bash
psql -U mac -d benchmark_db -c "TRUNCATE TABLE item CASCADE; TRUNCATE TABLE category CASCADE;"
```

Puis relancer le générateur :

```bash
./generate-data.sh
```

### Problème : Le générateur s'exécute automatiquement au démarrage

**Cause** : DataGenerator était détecté comme CommandLineRunner

**Solution** : Déjà corrigé ! `ServiceDApplication` exclut maintenant `DataGenerator` du scan.

### Problème : Erreur de connexion à la base de données

**Vérifications** :
1. PostgreSQL est démarré : `pg_isready -U mac`
2. La base de données existe : `psql -U mac -d benchmark_db -c "\l"`
3. Les tables existent : `psql -U mac -d benchmark_db -c "\dt"`

**Solution** : Si les tables n'existent pas :
```bash
psql -U mac -d benchmark_db -f src/main/resources/db/schema.sql
```

### Problème : Port 8083 déjà utilisé

**Solution** : Modifier `server.port` dans `application.properties` ou arrêter l'application qui utilise le port 8083

## Commandes Utiles

### Nettoyer la base de données manuellement

```bash
psql -U mac -d benchmark_db -c "TRUNCATE TABLE item CASCADE; TRUNCATE TABLE category CASCADE;"
```

### Vérifier les données

```bash
psql -U mac -d benchmark_db -c "SELECT COUNT(*) FROM category;"
psql -U mac -d benchmark_db -c "SELECT COUNT(*) FROM item;"
```

### Vérifier que l'application fonctionne

```bash
curl http://localhost:8083/actuator/health
curl "http://localhost:8083/categories?page=0&size=5"
```

## Checklist de Vérification

- [ ] PostgreSQL est démarré
- [ ] Base de données `benchmark_db` existe
- [ ] Tables `category` et `item` existent
- [ ] Configuration `application.properties` utilise l'utilisateur `mac`
- [ ] Le projet compile sans erreur (`mvn clean compile`)
- [ ] Les données sont générées (`./generate-data.sh`)
- [ ] L'application démarre (`mvn spring-boot:run`)
- [ ] Les endpoints répondent correctement

## Si Rien Ne Fonctionne

1. **Nettoyer complètement** :
   ```bash
   cd service-d
   mvn clean
   psql -U mac -d benchmark_db -c "TRUNCATE TABLE item CASCADE; TRUNCATE TABLE category CASCADE;"
   ```

2. **Recompiler** :
   ```bash
   mvn clean compile
   ```

3. **Générer les données** :
   ```bash
   ./generate-data.sh
   ```

4. **Lancer l'application** :
   ```bash
   mvn spring-boot:run
   ```

