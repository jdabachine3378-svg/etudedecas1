# 📊 Configuration Grafana pour le Benchmark

## 🚀 Installation Rapide

### 1. Installer Grafana

```bash
# Avec Homebrew (macOS)
brew install grafana

# Démarrer Grafana
brew services start grafana
```

### 2. Accéder à Grafana

Ouvrir dans le navigateur : **http://localhost:3000**

**Identifiants par défaut :**
- Username : `admin`
- Password : `admin` (vous devrez changer au premier login)

## 📈 Configuration pour JMeter

### Option 1 : Utiliser InfluxDB (Recommandé)

#### Étape 1 : Installer InfluxDB

**Option A : Avec Docker (Recommandé)**

```bash
# Lancer InfluxDB avec Docker
docker run -d -p 8086:8086 \
  -v influxdb-storage:/var/lib/influxdb2 \
  -v influxdb-config:/etc/influxdb2 \
  -e DOCKER_INFLUXDB_INIT_MODE=setup \
  -e DOCKER_INFLUXDB_INIT_USERNAME=admin \
  -e DOCKER_INFLUXDB_INIT_PASSWORD=admin123456 \
  -e DOCKER_INFLUXDB_INIT_ORG=jmeter \
  -e DOCKER_INFLUXDB_INIT_BUCKET=jmeter \
  --name influxdb \
  influxdb:2.7
```

**Option B : Téléchargement direct**

```bash
# Télécharger InfluxDB pour macOS
curl -O https://dl.influxdata.com/influxdb/releases/influxdb2-2.7.4-darwin-amd64.tar.gz

# Extraire
tar xvzf influxdb2-2.7.4-darwin-amd64.tar.gz

# Lancer
cd influxdb2-2.7.4-darwin-amd64
./influxd
```

#### Étape 2 : Créer la base de données

```bash
# Se connecter à InfluxDB
influx

# Créer la base de données
CREATE DATABASE jmeter
USE jmeter
exit
```

#### Étape 3 : Configurer JMeter Backend Listener

Dans JMeter :
1. Clic droit sur **Test Plan** → **Add** → **Listener** → **Backend Listener**
2. Configurer :
   - **Backend Listener Implementation** : `InfluxDBBackendListenerClient`
   - **influxdbUrl** : `http://localhost:8086/write?db=jmeter`
   - **application** : `Variante-D-Benchmark`

#### Étape 4 : Configurer Grafana

1. Dans Grafana : **Configuration** → **Data Sources** → **Add data source**
2. Sélectionner **InfluxDB**
3. Configurer :
   - **URL** : `http://localhost:8086`
   - **Database** : `jmeter`
   - **User** : (laisser vide)
   - **Password** : (laisser vide)
4. Cliquer sur **Save & Test**

### Option 2 : Utiliser Prometheus

#### Étape 1 : Installer Prometheus

```bash
# Installer Prometheus
brew install prometheus

# Démarrer Prometheus
brew services start prometheus
```

#### Étape 2 : Configurer JMeter Backend Listener

Dans JMeter :
1. Clic droit sur **Test Plan** → **Add** → **Listener** → **Backend Listener**
2. Configurer :
   - **Backend Listener Implementation** : `PrometheusBackendListenerClient`
   - **prometheusUrl** : `http://localhost:9090`

#### Étape 3 : Configurer Grafana

1. Dans Grafana : **Configuration** → **Data Sources** → **Add data source**
2. Sélectionner **Prometheus**
3. Configurer :
   - **URL** : `http://localhost:9090`
4. Cliquer sur **Save & Test**

## 📊 Créer un Dashboard

### Dashboard Simple pour JMeter

1. Dans Grafana : **+** → **Create** → **Dashboard**
2. Cliquer sur **Add visualization**
3. Sélectionner votre source de données (InfluxDB ou Prometheus)
4. Configurer la requête pour afficher :
   - **Throughput (RPS)** : `SELECT mean("throughput") FROM "jmeter" WHERE $timeFilter GROUP BY time($__interval)`
   - **Response Time** : `SELECT mean("responseTime") FROM "jmeter" WHERE $timeFilter GROUP BY time($__interval)`
   - **Error Rate** : `SELECT mean("errorCount") FROM "jmeter" WHERE $timeFilter GROUP BY time($__interval)`

### Panels Recommandés

1. **Throughput (Graph)**
   - Requête : `SELECT mean("throughput") FROM "jmeter"`
   - Unit : `req/sec`

2. **Response Time (Graph)**
   - Requête : `SELECT mean("responseTime") FROM "jmeter"`
   - Unit : `ms`

3. **Error Rate (Stat)**
   - Requête : `SELECT mean("errorCount") FROM "jmeter"`
   - Unit : `percent`

4. **Active Threads (Graph)**
   - Requête : `SELECT mean("activeThreads") FROM "jmeter"`
   - Unit : `threads`

## 🎯 Alternative Simple : Exporter les Résultats JMeter

Si Grafana est trop complexe, vous pouvez exporter les résultats JMeter en CSV :

### Dans JMeter :

1. **Summary Report** → Cliquer sur **Save Table Data**
2. Sauvegarder comme `scenario1-results.csv`
3. Répéter pour chaque scénario

### Analyser dans Excel/LibreOffice :

- Ouvrir le CSV
- Créer des graphiques pour :
  - Throughput vs Temps
  - Response Time vs Temps
  - Error Rate vs Temps

## 📝 Commandes Utiles

```bash
# Vérifier que Grafana tourne
curl http://localhost:3000/api/health

# Vérifier que InfluxDB tourne
curl http://localhost:8086/ping

# Vérifier que Prometheus tourne
curl http://localhost:9090/-/healthy
```

## ⚠️ Note Importante

Pour votre benchmark académique, **les rapports JMeter intégrés sont suffisants** :
- Summary Report
- Aggregate Report
- Graph Results

Grafana est utile si vous avez besoin de :
- Visualisations en temps réel
- Dashboards partagés
- Intégration avec d'autres outils

## 🎓 Pour votre TP

Vous pouvez simplement :
1. Utiliser les rapports JMeter (Summary Report, Aggregate Report)
2. Exporter les résultats en CSV
3. Créer des graphiques dans Excel/LibreOffice
4. Remplir les tableaux T0→T7 avec les données

Grafana n'est **pas obligatoire** pour un benchmark académique !

