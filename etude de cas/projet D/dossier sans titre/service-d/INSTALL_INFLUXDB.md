# 🚀 Installation InfluxDB - Guide Rapide

## ⚠️ Problème avec Homebrew

La formule `influxdb` n'est plus disponible dans Homebrew. Utilisez une des méthodes ci-dessous.

## Méthode 1 : Docker (Recommandé - Plus Simple)

### Installation

```bash
# Vérifier que Docker est installé
docker --version

# Si Docker n'est pas installé, installer Docker Desktop pour macOS
# https://www.docker.com/products/docker-desktop

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

### Vérifier que ça fonctionne

```bash
# Vérifier que le conteneur tourne
docker ps | grep influxdb

# Accéder à l'interface web
open http://localhost:8086
```

**Identifiants :**
- Username : `admin`
- Password : `admin123456`
- Organization : `jmeter`
- Bucket : `jmeter`

## Méthode 2 : Téléchargement Direct

### Installation

```bash
# Aller dans un dossier temporaire
cd ~/Downloads

# Télécharger InfluxDB pour macOS
curl -O https://dl.influxdata.com/influxdb/releases/influxdb2-2.7.4-darwin-amd64.tar.gz

# Extraire
tar xvzf influxdb2-2.7.4-darwin-amd64.tar.gz

# Aller dans le dossier
cd influxdb2-2.7.4-darwin-amd64

# Lancer InfluxDB
./influxd
```

### Configuration initiale

1. Ouvrir http://localhost:8086 dans le navigateur
2. Suivre l'assistant de configuration :
   - Username : `admin`
   - Password : `admin123456`
   - Organization : `jmeter`
   - Bucket : `jmeter`

## Méthode 3 : Utiliser Prometheus (Alternative)

Si InfluxDB est trop compliqué, utilisez Prometheus :

```bash
# Installer Prometheus
brew install prometheus

# Démarrer Prometheus
brew services start prometheus

# Vérifier
curl http://localhost:9090/-/healthy
```

## 🎯 Configuration JMeter avec InfluxDB

Une fois InfluxDB installé :

### Dans JMeter :

1. **Télécharger le plugin InfluxDB** :
   - Aller sur https://jmeter-plugins.org/
   - Télécharger `JMeterPlugins-Standard` et `JMeterPlugins-Extras`
   - Extraire dans `$JMETER_HOME/lib/ext/`

2. **Ajouter Backend Listener** :
   - Clic droit sur **Test Plan** → **Add** → **Listener** → **Backend Listener**
   - **Backend Listener Implementation** : `InfluxDBBackendListenerClient`
   - **influxdbUrl** : `http://localhost:8086/write?db=jmeter&u=admin&p=admin123456`

## ⚠️ Alternative Simple : Pas de Grafana !

Pour votre benchmark académique, **vous n'avez pas besoin de Grafana** :

1. ✅ Utilisez les rapports JMeter (Summary Report, Aggregate Report)
2. ✅ Exportez les résultats en CSV
3. ✅ Créez des graphiques dans Excel/LibreOffice
4. ✅ Remplissez les tableaux T0→T7

**Grafana est optionnel** et seulement utile pour des visualisations avancées !

## 📝 Commandes Utiles

```bash
# Arrêter InfluxDB (Docker)
docker stop influxdb

# Redémarrer InfluxDB (Docker)
docker start influxdb

# Voir les logs (Docker)
docker logs influxdb

# Supprimer InfluxDB (Docker)
docker rm -f influxdb
```

