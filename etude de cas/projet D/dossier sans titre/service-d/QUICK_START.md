# 🚀 Guide de Démarrage Rapide - Variante D (Java uniquement)

## Étapes Minimales pour Tester

### 1. Base de Données (déjà fait ✅)

Les tables sont déjà créées. Si besoin :
```bash
psql -U mac -d benchmark_db -f src/main/resources/db/schema.sql
```

### 2. Générer les Données avec Java (2-3 minutes)

**Option 1 : Avec le script shell (recommandé)**
```bash
cd service-d
./generate-data.sh
```

**Option 2 : Avec Maven directement**
```bash
cd service-d
mvn spring-boot:run -Dspring-boot.run.main-class=com.mliaedu.benchmark.util.DataGenerator -Dspring-boot.run.profiles=generator
```

**Option 3 : Avec exec:java**
```bash
cd service-d
mvn exec:java -Dexec.mainClass="com.mliaedu.benchmark.util.DataGenerator" -Dexec.classpathScope=compile
```

Cela génère :
- ✅ 2000 catégories
- ✅ ~100 000 items

### 3. Lancer l'Application (1 minute)

```bash
mvn spring-boot:run
```

L'application démarre sur **http://localhost:8083**

### 4. Test Rapide (30 secondes)

```bash
# Dans un autre terminal
curl http://localhost:8083/actuator/health
curl "http://localhost:8083/categories?page=0&size=5"
curl "http://localhost:8083/items?page=0&size=5"
```

Si vous voyez des réponses JSON avec `_embedded`, `_links`, et `page`, c'est que Spring Data REST fonctionne correctement !

## 📊 Endpoints Principaux à Tester

| Endpoint | Description |
|----------|-------------|
| `GET /categories?page=0&size=20` | Liste des catégories |
| `GET /categories/1` | Détail d'une catégorie |
| `GET /items?page=0&size=20` | Liste des items |
| `GET /items/1` | Détail d'un item |
| `GET /items?categoryId=1&page=0&size=20` | Items par catégorie |
| `GET /categories/1/items?page=0&size=20` | Relation inverse |

## 🔧 Configuration Minimale

Le fichier `application.properties` est déjà configuré avec :
- Port : **8083**
- Base de données : `benchmark_db`
- User : `mac`
- Password : (vide)

**Modifiez uniquement si vos paramètres diffèrent.**

## ⚠️ Problèmes Courants

**Erreur de connexion DB** :
```bash
# Vérifier que PostgreSQL tourne
pg_isready -U mac
```

**Port déjà utilisé** :
- Modifiez `server.port` dans `application.properties`

**Pas de données** :
- Relancez `./generate-data.sh`

## 📖 Documentation Complète

Voir `README.md` et `INSTRUCTIONS_TEST.md` pour plus de détails.

## ✅ Checklist Rapide

- [x] Base de données `benchmark_db` existe
- [x] Tables `category` et `item` créées
- [ ] Données générées avec Java (2000 catégories, 100000 items)
- [ ] Application lancée sur port 8083
- [ ] Endpoints répondent correctement
- [ ] Tests JMeter configurés (optionnel)
