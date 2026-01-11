#!/bin/bash

# Script de test rapide pour la variante D
# Port par défaut: 8083

BASE_URL="http://localhost:8083"

echo "=== Test des endpoints de la variante D ==="
echo ""

# Test 1: Liste des catégories
echo "1. GET /categories?page=0&size=5"
curl -s "$BASE_URL/categories?page=0&size=5" | jq '.page' 2>/dev/null || echo "Réponse reçue"
echo ""

# Test 2: Détail d'une catégorie
echo "2. GET /categories/1"
curl -s "$BASE_URL/categories/1" | jq '.id, .name' 2>/dev/null || echo "Réponse reçue"
echo ""

# Test 3: Liste des items
echo "3. GET /items?page=0&size=5"
curl -s "$BASE_URL/items?page=0&size=5" | jq '.page' 2>/dev/null || echo "Réponse reçue"
echo ""

# Test 4: Détail d'un item
echo "4. GET /items/1"
curl -s "$BASE_URL/items/1" | jq '.id, .name, .price' 2>/dev/null || echo "Réponse reçue"
echo ""

# Test 5: Items par catégorie (endpoint personnalisé)
echo "5. GET /items?categoryId=1&page=0&size=5"
curl -s "$BASE_URL/items?categoryId=1&page=0&size=5" | jq '.page' 2>/dev/null || echo "Réponse reçue"
echo ""

# Test 6: Items d'une catégorie (relation Spring Data REST)
echo "6. GET /categories/1/items?page=0&size=5"
curl -s "$BASE_URL/categories/1/items?page=0&size=5" | jq '.page' 2>/dev/null || echo "Réponse reçue"
echo ""

# Test 7: Health check
echo "7. GET /actuator/health"
curl -s "$BASE_URL/actuator/health" | jq '.' 2>/dev/null || echo "Réponse reçue"
echo ""

echo "=== Tests terminés ==="

