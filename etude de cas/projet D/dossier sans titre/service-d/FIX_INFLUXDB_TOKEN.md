# 🔧 Corriger l'erreur "unauthorized access" InfluxDB

## Problème
Grafana retourne : "unauthorized access error reading influxDB"

## Solution : Générer un token valide

### Étape 1 : Accéder à InfluxDB

1. Ouvrir dans le navigateur : **http://localhost:8086**
2. Se connecter :
   - Username : `admin`
   - Password : `admin123456`

### Étape 2 : Générer un token API

1. Dans InfluxDB, aller dans le menu de gauche
2. Cliquer sur **"Load Data"** → **"Tokens"**
3. Cliquer sur **"Generate API Token"** → **"All Access Token"**
4. Nommer le token : `Grafana Token`
5. Cliquer sur **"Save"**
6. **IMPORTANT** : Copier le token immédiatement (il ne sera plus visible après)

### Étape 3 : Vérifier Organization et Bucket

Dans InfluxDB :
- **Organization** : `jmeter`
- **Bucket** : `jmeter`

### Étape 4 : Configurer Grafana

Dans Grafana, sur la page de configuration InfluxDB :

1. **Query language** : Sélectionner **"Flux"** (pas InfluxQL)
2. **URL** : `http://localhost:8086`
3. **Organization** : `jmeter`
4. **Token** : Coller le token généré à l'étape 2
5. **Default Bucket** : `jmeter`
6. Cliquer sur **"Save & test"**

## Alternative : Créer un token via ligne de commande

Si l'interface web ne fonctionne pas :

```bash
# Se connecter au conteneur InfluxDB
docker exec -it influxdb influx setup --help

# Ou créer un token via l'API
docker exec -it influxdb influx auth create \
  --org jmeter \
  --all-access \
  --description "Grafana Token"
```

## Vérification

Pour vérifier que le token fonctionne :

```bash
# Tester avec curl
curl -X GET "http://localhost:8086/api/v2/buckets?org=jmeter" \
  -H "Authorization: Token VOTRE_TOKEN_ICI"
```

Si ça retourne une liste de buckets, le token fonctionne !

## Configuration Grafana complète

**Settings à vérifier :**
- ✅ Query language : **Flux**
- ✅ URL : `http://localhost:8086`
- ✅ Organization : `jmeter`
- ✅ Token : (token généré)
- ✅ Default Bucket : `jmeter`
- ✅ Timeout : (peut rester vide ou mettre 60)

## Si ça ne fonctionne toujours pas

1. Vérifier que InfluxDB tourne :
   ```bash
   docker ps | grep influxdb
   ```

2. Vérifier les logs :
   ```bash
   docker logs influxdb | tail -20
   ```

3. Redémarrer InfluxDB :
   ```bash
   docker restart influxdb
   ```

