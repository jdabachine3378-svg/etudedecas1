# 📋 Commandes Essentielles - Variante D

## ✅ Tout est prêt ! Voici ce que vous devez faire :

### 1. Générer les données (2-3 minutes)

**Option A : Avec le script shell (recommandé)**
```bash
cd service-d
./generate-data.sh
```

**Option B : Avec Maven exec**
```bash
cd service-d
mvn exec:java -Dexec.mainClass="com.mliaedu.benchmark.util.DataGenerator" -Dspring.profiles.active=generator
```

**Option C : Avec Spring Boot run**
```bash
cd service-d
mvn spring-boot:run -Dspring-boot.run.main-class=com.mliaedu.benchmark.util.DataGenerator -Dspring-boot.run.profiles=generator
```

Cela génère :
- ✅ 2000 catégories
- ✅ ~100 000 items

### 2. Lancer l'application (1 minute)

```bash
cd service-d
mvn spring-boot:run
```

L'application démarre sur **http://localhost:8083**

### 3. Tester rapidement (30 secondes)

Dans un autre terminal :

```bash
# Vérifier la santé
curl http://localhost:8083/actuator/health

# Tester les endpoints
curl "http://localhost:8083/categories?page=0&size=5"
curl "http://localhost:8083/items?page=0&size=5"
curl "http://localhost:8083/items?categoryId=1&page=0&size=5"
```

## 🔧 Résolution des problèmes

### Problème : Erreur "role postgres does not exist"

**Solution** : C'est déjà corrigé ! Le fichier `application.properties` utilise l'utilisateur `mac`.

### Problème : Erreur de compilation avec les setters

**Solution** : C'est déjà corrigé ! Le générateur utilise la réflexion pour définir les champs.

### Problème : Maven trouve deux classes principales

**Solution** : C'est déjà corrigé ! Le `pom.xml` spécifie maintenant `ServiceDApplication` comme classe principale par défaut.

## 📝 Checklist

- [x] Code compilé avec succès
- [x] Configuration PostgreSQL corrigée (utilisateur `mac`)
- [x] Générateur Java fonctionnel (utilise la réflexion)
- [ ] Données générées (à faire maintenant)
- [ ] Application lancée (après génération des données)
- [ ] Tests effectués (après lancement)

## 🚀 Commandes rapides (copier-coller)

```bash
# 1. Aller dans le dossier
cd service-d

# 2. Générer les données
./generate-data.sh

# 3. Lancer l'application (dans un autre terminal ou après avoir arrêté le générateur)
mvn spring-boot:run

# 4. Tester (dans un autre terminal)
curl http://localhost:8083/actuator/health
```

Tout est prêt ! Commencez par générer les données avec `./generate-data.sh` 🎉

