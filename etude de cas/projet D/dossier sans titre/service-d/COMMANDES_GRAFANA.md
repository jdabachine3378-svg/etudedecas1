# 🖥️ Commandes Terminal pour Grafana et InfluxDB

## ✅ Vérifier que tout fonctionne

```bash
# 1. Vérifier que InfluxDB tourne
docker ps | grep influxdb

# 2. Vérifier que Grafana tourne
curl http://localhost:3000/api/health

# 3. Vérifier la santé d'InfluxDB
curl http://localhost:8086/health
```

## 🔑 Générer un token InfluxDB

```bash
# Créer un token pour Grafana
docker exec influxdb influx auth create \
  --org jmeter \
  --all-access \
  --description "Grafana Token"

# Voir tous les tokens
docker exec influxdb influx auth list --org jmeter
```

## 📊 Vérifier les données dans InfluxDB

```bash
# Se connecter à InfluxDB
docker exec -it influxdb influx

# Dans le shell InfluxDB :
use jmeter
show measurements
show series
exit
```

## 🔍 Vérifier les buckets

```bash
# Lister les buckets
docker exec influxdb influx bucket list --org jmeter

# Vérifier que le bucket "jmeter" existe
docker exec influxdb influx bucket list --org jmeter | grep jmeter
```

## 📈 Tester la connexion avec le token

```bash
# Remplacer VOTRE_TOKEN par le token généré
TOKEN="4nW70bBTPjALi3IiwHMuwQcBzl07KevpjuxaTFXMumCqbA2WfBJfH2NU1kIPaeKNQstgD3s7ffyKmSRcJ7F6Vg=="

# Tester l'accès aux buckets
curl -X GET "http://localhost:8086/api/v2/buckets?org=jmeter" \
  -H "Authorization: Token $TOKEN"

# Tester l'accès aux données
curl -X POST "http://localhost:8086/api/v2/query?org=jmeter" \
  -H "Authorization: Token $TOKEN" \
  -H "Content-Type: application/vnd.flux" \
  -d 'from(bucket:"jmeter") |> range(start: -1h) |> limit(n: 10)'
```

## 🚀 Commandes pour démarrer/arrêter

```bash
# Démarrer InfluxDB
docker start influxdb

# Arrêter InfluxDB
docker stop influxdb

# Redémarrer InfluxDB
docker restart influxdb

# Voir les logs InfluxDB
docker logs influxdb | tail -50

# Démarrer Grafana (si installé avec Homebrew)
brew services start grafana

# Arrêter Grafana
brew services stop grafana
```

## 📝 Exporter les données JMeter (Alternative)

Si vous voulez exporter les résultats JMeter en CSV :

```bash
# Aller dans le dossier jmeter
cd "/Users/mac/Desktop/ S3/MLIAEdu/Étude de cas – Benchmark de performances des Web Services REST/dossier sans titre/service-d/jmeter"

# Lancer JMeter en mode non-GUI et sauvegarder les résultats
jmeter -n -t scenario1-read-heavy-variante-d.jmx \
  -l results-scenario1.jtl \
  -e -o results-html-scenario1

# Les résultats seront dans :
# - results-scenario1.jtl (fichier CSV)
# - results-html-scenario1/ (rapport HTML)
```

## 🔧 Commandes de dépannage

```bash
# Vérifier les ports utilisés
lsof -i :8086  # InfluxDB
lsof -i :3000  # Grafana

# Vérifier les conteneurs Docker
docker ps -a | grep influxdb

# Supprimer et recréer InfluxDB (si problème)
docker rm -f influxdb
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

## 📊 Commandes pour voir les données dans InfluxDB

```bash
# Se connecter et voir les données
docker exec -it influxdb influx query \
  --org jmeter \
  --token "4nW70bBTPjALi3IiwHMuwQcBzl07KevpjuxaTFXMumCqbA2WfBJfH2NU1kIPaeKNQstgD3s7ffyKmSRcJ7F6Vg==" \
  'from(bucket:"jmeter") |> range(start: -1h) |> limit(n: 10)'
```

## 🎯 Commandes rapides (copier-coller)

```bash
# Vérifier tout d'un coup
echo "=== InfluxDB ===" && docker ps | grep influxdb && \
echo "=== Grafana ===" && curl -s http://localhost:3000/api/health | head -1 && \
echo "=== Token ===" && docker exec influxdb influx auth list --org jmeter | grep "Grafana Token" | awk '{print $3}'
```

