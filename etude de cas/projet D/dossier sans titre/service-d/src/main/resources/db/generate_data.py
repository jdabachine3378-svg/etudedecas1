#!/usr/bin/env python3
"""
Générateur de données pour le benchmark
Génère 2000 catégories et 100000 items (environ 50 par catégorie)
"""

import psycopg2
import random
import string
from datetime import datetime

# Configuration de la base de données
DB_CONFIG = {
    'host': 'localhost',
    'port': 5432,
    'database': 'benchmark_db',
    'user': 'mac',
    'password': ''
}

def generate_random_string(length=10):
    """Génère une chaîne aléatoire"""
    return ''.join(random.choices(string.ascii_letters + string.digits, k=length))

def generate_categories(cursor, count=2000):
    """Génère les catégories"""
    print(f"Génération de {count} catégories...")
    for i in range(1, count + 1):
        code = f"CAT_{i:04d}"
        name = f"Category {i}"
        cursor.execute(
            "INSERT INTO category (code, name, updated_at) VALUES (%s, %s, NOW())",
            (code, name)
        )
        if i % 100 == 0:
            print(f"  {i} catégories créées...")
    print(f"✓ {count} catégories créées")

def generate_items(cursor, category_count=2000, items_per_category=50):
    """Génère les items"""
    total_items = category_count * items_per_category
    print(f"Génération de {total_items} items...")
    
    item_count = 0
    for category_id in range(1, category_count + 1):
        # Nombre variable d'items par catégorie (entre 45 et 55)
        num_items = random.randint(45, 55)
        
        for j in range(num_items):
            item_count += 1
            sku = f"SKU_{category_id:04d}_{j:03d}"
            name = f"Item {item_count}"
            price = round(random.uniform(10.0, 1000.0), 2)
            stock = random.randint(0, 1000)
            
            cursor.execute(
                """INSERT INTO item (sku, name, price, stock, category_id, updated_at)
                   VALUES (%s, %s, %s, %s, %s, NOW())""",
                (sku, name, price, stock, category_id)
            )
        
        if category_id % 100 == 0:
            print(f"  {item_count} items créés...")
    
    print(f"✓ {item_count} items créés")

def main():
    conn = None
    cursor = None
    try:
        print("Connexion à la base de données...")
        conn = psycopg2.connect(**DB_CONFIG)
        cursor = conn.cursor()
        
        print("Nettoyage des tables existantes...")
        cursor.execute("TRUNCATE TABLE item CASCADE")
        cursor.execute("TRUNCATE TABLE category CASCADE")
        
        # Génération des données
        generate_categories(cursor, 2000)
        generate_items(cursor, 2000, 50)
        
        # Commit
        conn.commit()
        print("\n✓ Données générées avec succès!")
        
        # Statistiques
        cursor.execute("SELECT COUNT(*) FROM category")
        cat_count = cursor.fetchone()[0]
        cursor.execute("SELECT COUNT(*) FROM item")
        item_count = cursor.fetchone()[0]
        print(f"\nStatistiques:")
        print(f"  Catégories: {cat_count}")
        print(f"  Items: {item_count}")
        
    except Exception as e:
        print(f"Erreur: {e}")
        if conn:
            conn.rollback()
    finally:
        if cursor:
            cursor.close()
        if conn:
            conn.close()

if __name__ == "__main__":
    main()

