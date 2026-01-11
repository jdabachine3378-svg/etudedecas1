# 📊 Fichiers JMeter pour Variante D

## ✅ Fichiers Créés

### Scénarios de Test (.jmx)
1. **scenario1-read-heavy-variante-d.jmx** - Scénario READ-heavy avec relations
2. **scenario2-join-filter-variante-d.jmx** - Scénario JOIN-filter ciblé
3. **scenario3-mixed-variante-d.jmx** - Scénario MIXED avec écritures
4. **scenario4-heavy-body-variante-d.jmx** - Scénario HEAVY-body (payload 5 KB)

### Fichiers de Données
- **ids.csv** - Fichier CSV avec categoryId,itemId (100 lignes)
- **ids-categories.csv** - Liste des IDs de catégories
- **ids-items.csv** - Liste des IDs d'items

### Documentation
- **GUIDE_JMETER.md** - Guide d'utilisation détaillé
- **README.md** - Ce fichier

## 🚀 Utilisation Rapide

### 1. Ouvrir JMeter
```bash
jmeter
```

### 2. Ouvrir un scénario
- **File → Open**
- Sélectionner un fichier `.jmx` dans ce dossier

### 3. Vérifier la Configuration
- **Port** : `8083` (Variante D)
- **CSV Data Set Config** : Chemin vers `ids.csv`

### 4. Lancer le Test
- Cliquer sur **▶️ (Run)**
- Consulter les résultats dans **Summary Report** et **Aggregate Report**

## 📋 Détails des Scénarios

### Scénario 1 : READ-heavy (relation incluse)
- **Mix** : 50% GET /items, 20% GET /items?categoryId, 20% GET /categories/{id}/items, 10% GET /categories
- **Threads** : 50
- **Durée** : 10 min
- **Fichier** : `scenario1-read-heavy-variante-d.jmx`

### Scénario 2 : JOIN-filter ciblé
- **Mix** : 70% GET /items?categoryId, 30% GET /items/{id}
- **Threads** : 60
- **Durée** : 8 min
- **Fichier** : `scenario2-join-filter-variante-d.jmx`

### Scénario 3 : MIXED (écritures)
- **Mix** : 40% GET /items/{id}, 20% POST /items, 20% PUT /items/{id}, 10% DELETE /items/{id}, 10% GET /categories/{id}
- **Threads** : 50
- **Durée** : 10 min
- **Fichier** : `scenario3-mixed-variante-d.jmx`

### Scénario 4 : HEAVY-body (payload 5 KB)
- **Mix** : 50% POST /items (5 KB), 50% PUT /items/{id} (5 KB)
- **Threads** : 30
- **Durée** : 8 min
- **Fichier** : `scenario4-heavy-body-variante-d.jmx`

## 📊 Métriques à Noter

Pour chaque scénario, noter dans le tableau T2 :

| Métrique | Où trouver |
|----------|------------|
| **RPS** | Summary Report → Throughput |
| **p50** | Aggregate Report → Median |
| **p95** | Aggregate Report → 90th pct |
| **p99** | Aggregate Report → 99th pct |
| **Err %** | Summary Report → Error % |

## ⚠️ Points Importants

1. **Désactiver View Results Tree** pendant les runs (trop lourd)
2. **Garder Summary Report et Aggregate Report** activés
3. **Vérifier que l'application tourne** sur le port 8083
4. **Un test à la fois** pour éviter la surcharge

## 🔄 Pour Tester les Autres Variantes

Pour tester les variantes A et C :
1. Ouvrir le fichier `.jmx` dans JMeter
2. Modifier le **Port Number** dans les requêtes HTTP :
   - Variante A : `8081`
   - Variante C : `8082`
   - Variante D : `8083` ✅
3. Sauvegarder avec un nom différent
4. Relancer le test

## 📝 Exemple de Résultats

Après un test, dans **Summary Report** vous verrez :

```
Label                          Samples  Average  Min  Max  Error %  Throughput
GET /items (50%)               12500    45ms    12   234  0.0%     208.3/sec
GET /items?categoryId= (20%)   5000     38ms    10   189  0.0%     83.3/sec
...
```

## 🎯 Prochaines Étapes

1. ✅ Fichiers JMeter créés
2. ⏭️ Exécuter les tests sur la variante D
3. ⏭️ Répéter pour les variantes A et C
4. ⏭️ Remplir les tableaux T0→T7
5. ⏭️ Analyser et comparer les résultats

Bon test ! 🚀

