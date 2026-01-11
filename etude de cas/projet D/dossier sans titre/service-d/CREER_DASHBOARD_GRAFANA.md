# 📊 Créer un Dashboard Grafana pour JMeter

## ⚠️ Important : Envoyer les données depuis JMeter d'abord

Avant de créer un dashboard, vous devez configurer JMeter pour envoyer les métriques vers InfluxDB.

## Étape 1 : Configurer JMeter pour InfluxDB

### 1.1 Télécharger le plugin JMeter InfluxDB

1. Aller sur : https://jmeter-plugins.org/
2. Télécharger :
   - `JMeterPlugins-Standard` (1.4.0 ou plus récent)
   - `JMeterPlugins-Extras` (1.4.0 ou plus récent)
3. Extraire les fichiers `.jar` dans :
   ```
   $JMETER_HOME/lib/ext/
   ```
   (Généralement : `/opt/homebrew/Cellar/jmeter/5.6.3/libexec/lib/ext/`)

### 1.2 Ajouter Backend Listener dans JMeter

Dans votre fichier `.jmx` :

1. Clic droit sur **Test Plan** → **Add** → **Listener** → **Backend Listener**
2. Configurer :
   - **Backend Listener Implementation** : `InfluxDBBackendListenerClient`
   - **influxdbUrl** : `http://localhost:8086/write?db=jmeter&u=admin&p=admin123456`
   - **application** : `Variante-D-Benchmark`
   - **measurement** : `jmeter`
   - **summaryOnly** : `false`
   - **samplersRegex** : `.*` (tous les samplers)

### 1.3 Lancer le test JMeter

Quand vous lancez le test, les métriques seront automatiquement envoyées vers InfluxDB.

## Étape 2 : Créer un Dashboard dans Grafana

### 2.1 Créer un nouveau Dashboard

1. Dans Grafana, cliquer sur **"+"** (en haut à droite)
2. Sélectionner **"Create"** → **"Dashboard"**
3. Cliquer sur **"Add visualization"**

### 2.2 Ajouter un Panel : Throughput (RPS)

1. **Data source** : Sélectionner `influxdb`
2. **Query** (Flux) :
   ```flux
   from(bucket: "jmeter")
     |> range(start: v.timeRangeStart, stop: v.timeRangeStop)
     |> filter(fn: (r) => r["_measurement"] == "jmeter")
     |> filter(fn: (r) => r["_field"] == "throughput")
     |> aggregateWindow(every: v.windowPeriod, fn: mean, createEmpty: false)
     |> yield(name: "mean")
   ```
3. **Panel options** :
   - **Title** : `Throughput (RPS)`
   - **Unit** : `req/sec`
4. **Visualization** : `Time series`

### 2.3 Ajouter un Panel : Response Time

1. Cliquer sur **"Add panel"** → **"Add new panel"**
2. **Query** (Flux) :
   ```flux
   from(bucket: "jmeter")
     |> range(start: v.timeRangeStart, stop: v.timeRangeStop)
     |> filter(fn: (r) => r["_measurement"] == "jmeter")
     |> filter(fn: (r) => r["_field"] == "responseTime")
     |> aggregateWindow(every: v.windowPeriod, fn: mean, createEmpty: false)
     |> yield(name: "mean")
   ```
3. **Panel options** :
   - **Title** : `Response Time`
   - **Unit** : `ms`
4. **Visualization** : `Time series`

### 2.4 Ajouter un Panel : Error Rate

1. Cliquer sur **"Add panel"** → **"Add new panel"**
2. **Query** (Flux) :
   ```flux
   from(bucket: "jmeter")
     |> range(start: v.timeRangeStart, stop: v.timeRangeStop)
     |> filter(fn: (r) => r["_measurement"] == "jmeter")
     |> filter(fn: (r) => r["_field"] == "errorCount")
     |> aggregateWindow(every: v.windowPeriod, fn: sum, createEmpty: false)
     |> yield(name: "sum")
   ```
3. **Panel options** :
   - **Title** : `Error Count`
   - **Unit** : `short`
4. **Visualization** : `Time series`

### 2.5 Ajouter un Panel : Active Threads

1. Cliquer sur **"Add panel"** → **"Add new panel"**
2. **Query** (Flux) :
   ```flux
   from(bucket: "jmeter")
     |> range(start: v.timeRangeStart, stop: v.timeRangeStop)
     |> filter(fn: (r) => r["_measurement"] == "jmeter")
     |> filter(fn: (r) => r["_field"] == "activeThreads")
     |> aggregateWindow(every: v.windowPeriod, fn: mean, createEmpty: false)
     |> yield(name: "mean")
   ```
3. **Panel options** :
   - **Title** : `Active Threads`
   - **Unit** : `threads`
4. **Visualization** : `Time series`

## Étape 3 : Sauvegarder le Dashboard

1. Cliquer sur **"Save dashboard"** (en haut à droite)
2. Nommer : `JMeter Benchmark - Variante D`
3. Cliquer sur **"Save"**

## 📊 Panels Recommandés pour le Benchmark

### Panels Essentiels :

1. **Throughput (RPS)** - Graphique en ligne
2. **Response Time (p50, p95, p99)** - Graphique en ligne avec plusieurs séries
3. **Error Rate (%)** - Stat panel
4. **Active Threads** - Graphique en ligne
5. **Requests per Endpoint** - Table

## ⚠️ Si vous ne voyez pas de données

1. **Vérifier que JMeter envoie les données** :
   - Vérifier les logs InfluxDB : `docker logs influxdb | tail -20`
   - Vérifier que le Backend Listener est activé dans JMeter

2. **Vérifier les données dans InfluxDB** :
   - Aller sur http://localhost:8086
   - Data Explorer → Sélectionner le bucket `jmeter`
   - Voir si des données sont présentes

3. **Vérifier la requête Flux** :
   - Dans Grafana, utiliser "Query Inspector" pour voir les données brutes

## 🎯 Alternative Simple : Utiliser les Rapports JMeter

Si Grafana est trop complexe, utilisez simplement :
- **Summary Report** dans JMeter
- **Aggregate Report** dans JMeter
- Exporter en CSV et créer des graphiques dans Excel

C'est suffisant pour un benchmark académique !

