#!/bin/bash

echo "=== Vérification InfluxDB ==="
docker ps | grep influxdb && echo "✓ InfluxDB tourne" || echo "✗ InfluxDB ne tourne pas"

echo ""
echo "=== Vérification Grafana ==="
curl -s http://localhost:3000/api/health > /dev/null && echo "✓ Grafana répond" || echo "✗ Grafana ne répond pas"

echo ""
echo "=== Token Grafana ==="
docker exec influxdb influx auth list --org jmeter 2>/dev/null | grep "Grafana Token" | awk '{print "Token: " $3}' || echo "Générer un token avec: docker exec influxdb influx auth create --org jmeter --all-access --description 'Grafana Token'"

echo ""
echo "=== Buckets InfluxDB ==="
docker exec influxdb influx bucket list --org jmeter 2>/dev/null | grep -E "(ID|jmeter)" || echo "Aucun bucket trouvé"
