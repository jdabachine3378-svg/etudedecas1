# Configuration de la Base de Données

## Problème Résolu ✅

L'erreur `FATAL: role "postgres" does not exist` a été corrigée.

Sur macOS avec PostgreSQL installé via Homebrew, l'utilisateur par défaut est votre nom d'utilisateur système (dans votre cas : `mac`), et non `postgres`.

## Configuration Actuelle

Le fichier `application.properties` a été mis à jour avec :
- **Username** : `mac`
- **Password** : (vide - pas de mot de passe requis)

## Vérification

Pour vérifier votre configuration PostgreSQL :

```bash
# Vérifier les utilisateurs PostgreSQL
psql -U mac -d postgres -c "\du"

# Vérifier que la base de données existe
psql -U mac -d postgres -c "SELECT datname FROM pg_database WHERE datname = 'benchmark_db';"
```

## Si vous avez un autre utilisateur PostgreSQL

Si votre utilisateur PostgreSQL est différent (par exemple `postgres` avec un mot de passe), modifiez `application.properties` :

```properties
spring.datasource.username=votre_utilisateur
spring.datasource.password=votre_mot_de_passe
```

## Création de la Base de Données (si nécessaire)

```bash
# Créer la base de données
createdb -U mac benchmark_db

# Créer les tables
psql -U mac -d benchmark_db -f src/main/resources/db/schema.sql
```

## Génération des Données

Le script Python a également été mis à jour pour utiliser l'utilisateur `mac` :

```bash
python3 src/main/resources/db/generate_data.py
```

## Relancer l'Application

Maintenant vous pouvez relancer l'application :

```bash
mvn spring-boot:run
```

L'application devrait démarrer correctement sur **http://localhost:8083** ! 🚀

