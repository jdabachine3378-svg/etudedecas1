# 📊 Guide d'Utilisation JMeter - Variante D

## 🎯 Fichiers Créés

- `scenario1-read-heavy-variante-d.jmx` - Scénario READ-heavy
- `scenario2-join-filter-variante-d.jmx` - Scénario JOIN-filter
- `scenario3-mixed-variante-d.jmx` - Scénario MIXED
- `scenario4-heavy-body-variante-d.jmx` - Scénario HEAVY-body
- `ids.csv` - Fichier CSV avec les IDs pour les tests

## 🚀 Comment Utiliser les Fichiers JMeter

### Étape 1 : Ouvrir JMeter

```bash
jmeter
```

### Étape 2 : Ouvrir un fichier de test

1. Dans JMeter : **File → Open**
2. Naviguer vers : `service-d/jmeter/`
3. Sélectionner un fichier `.jmx` (ex: `scenario1-read-heavy-variante-d.jmx`)

### Étape 3 : Vérifier la Configuration

1. **HTTP Request Defaults** :
   - Server Name : `localhost`
   - Port Number : `8083` ✅ (Variante D)

2. **CSV Data Set Config** :
   - Filename : Vérifier que le chemin vers `ids.csv` est correct
   - Variable Names : `categoryId,itemId`

3. **Thread Group** :
   - Vérifier les paramètres (threads, ramp-up, durée)

### Étape 4 : Lancer le Test

1. **Désactiver les listeners lourds** (View Results Tree) pendant les runs
2. Cliquer sur le bouton **▶️ (Run)** en haut
3. Attendre la fin du test (selon la durée configurée)

### Étape 5 : Consulter les Résultats

Après le test, consulter :
- **Summary Report** : Vue d'ensemble avec RPS, latence moyenne, erreurs
- **Aggregate Report** : Statistiques détaillées (min, max, moyenne, p50, p95, p99)

## 📋 Les 4 Scénarios

### Scénario 1 : READ-heavy (relation incluse)
- **Fichier** : `scenario1-read-heavy-variante-d.jmx`
- **Mix** : 50% items list, 20% items by category, 20% cat->items, 10% cat list
- **Threads** : 50 → 100 → 200 (utiliser Stepping Thread Group)
- **Ramp-up** : 60s
- **Durée** : 10 min/palier

### Scénario 2 : JOIN-filter ciblé
- **Fichier** : `scenario2-join-filter-variante-d.jmx`
- **Mix** : 70% items?categoryId, 30% item id
- **Threads** : 60 → 120
- **Ramp-up** : 60s
- **Durée** : 8 min/palier

### Scénario 3 : MIXED (écritures)
- **Fichier** : `scenario3-mixed-variante-d.jmx`
- **Mix** : GET/POST/PUT/DELETE sur items + categories
- **Threads** : 50 → 100
- **Ramp-up** : 60s
- **Durée** : 10 min/palier

### Scénario 4 : HEAVY-body
- **Fichier** : `scenario4-heavy-body-variante-d.jmx`
- **Mix** : POST/PUT items 5 KB
- **Threads** : 30 → 60
- **Ramp-up** : 60s
- **Durée** : 8 min/palier

## 📊 Métriques à Noter

Pour chaque scénario, noter dans le tableau T2 :

- **RPS** (Requests Per Second) : Dans Summary Report → Throughput
- **p50** (ms) : 50th percentile latency
- **p95** (ms) : 95th percentile latency  
- **p99** (ms) : 99th percentile latency
- **Err %** : Error percentage

## ⚠️ Points Importants

1. **Désactiver View Results Tree** pendant les runs (trop lourd)
2. **Garder Summary Report et Aggregate Report** activés
3. **Vérifier que l'application tourne** sur le port 8083 avant de lancer
4. **Un test à la fois** pour éviter la surcharge

## 🔄 Répéter pour les Autres Variantes

Pour tester les variantes A et C :
1. Modifier le **Port Number** dans HTTP Request Defaults :
   - Variante A : `8081`
   - Variante C : `8082`
   - Variante D : `8083` ✅
2. Sauvegarder avec un nom différent
3. Relancer le test

## 📝 Exemple de Résultats Attendus

Dans **Summary Report**, vous devriez voir :
- **Label** : Nom de la requête
- **Samples** : Nombre de requêtes
- **Average** : Temps moyen (ms)
- **Min/Max** : Temps min/max (ms)
- **Error %** : Pourcentage d'erreurs
- **Throughput** : RPS (Requests Per Second)

Bon test ! 🚀

