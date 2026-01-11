#!/bin/bash

# Script pour générer les données avec Java
# Usage: ./generate-data.sh

echo "=== Générateur de données Java ==="
echo ""

# Compiler le projet si nécessaire
if [ ! -d "target/classes" ]; then
    echo "Compilation du projet..."
    mvn clean compile
    echo ""
fi

# Exécuter le générateur avec exec:java
echo "Génération des données..."
echo "Cela peut prendre 2-3 minutes..."
echo ""

mvn exec:java -Dexec.mainClass="com.mliaedu.benchmark.util.DataGenerator" -Dspring.profiles.active=generator

echo ""
echo "=== Terminé ==="
