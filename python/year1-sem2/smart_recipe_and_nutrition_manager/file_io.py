# Shared File I/O Functions and Constants
# Made by RAM (shared between all modules)

import json, os
from datetime import datetime

# ============================= File Paths =============================
# Define where we store our data
RECIPE_FILE = "recipes.json"
SUBSTITUTE_FILE = "substitutes.json"
WEEKLY_LOG_FILE = "weekly_log.json"  # Note: This is referenced in UI as "Meal Tracker"
    
# ============================= Default Data =============================
# A few common substitutes to get started
DEFAULT_SUBSTITUTES = {
    "butter": "margarine, olive oil, applesauce",
    "egg": "1/4 cup applesauce, 1/2 mashed banana",
    "milk": "almond milk, soy milk, water",
    "sugar": "honey, maple syrup, stevia",
    "flour": "almond flour, oat flour, coconut flour"
}

# ============================= File I/O Helpers (Abstraction) =============================
def load_json_file(filename, default):
    """Loads JSON data from a file, returning default if file is missing or corrupted."""
    if os.path.exists(filename):
        try:
            with open(filename, "r", encoding="utf-8") as f:
                return json.load(f)
        except Exception as e:
            print(f"[load_json_file] Failed to load {filename}: {e}")
    # Return deep copy-like default to avoid accidental mutation of module constants
    if isinstance(default, dict):
        return dict(default)
    if isinstance(default, list):
        return list(default)
    return default

def save_json_file(filename, data):
    """Saves data to a JSON file with nice formatting."""
    try:
        with open(filename, "w", encoding="utf-8") as f:
            json.dump(data, f, indent=4, ensure_ascii=False)
        return True
    except Exception as e:
        print(f"[save_json_file] Failed to save {filename}: {e}")
        return False

def ensure_files():
    """Checks if data files exist and creates them with defaults if they don't."""
    if not os.path.exists(RECIPE_FILE):
        save_json_file(RECIPE_FILE, [])
    if not os.path.exists(SUBSTITUTE_FILE):
        save_json_file(SUBSTITUTE_FILE, DEFAULT_SUBSTITUTES)
    if not os.path.exists(WEEKLY_LOG_FILE):
        save_json_file(WEEKLY_LOG_FILE, {})
        
# ============================= Nutrition Database (kcal per 100g) ============================
# Basic calorie information for common ingredients
INGREDIENT_NUTRITION = {
    "Rice": 130, "Noodles": 138, "Chicken": 165, "Beef": 217, "Pork": 242,
    "Egg": 155, "Milk": 42, "Butter": 717, "Flour": 364, "Sugar": 387,
    "Salt": 0, "Cooking Oil": 884, "Tomato": 18, "Spinach": 23, "Spring Onion": 32,
    "Onion": 40, "Garlic": 149, "Banana": 89, "Apple": 52, "Orange": 47, "Avocado": 160,
    "Soy Sauce": 53, "Pepper": 251, "Chili": 40, "Curry Powder": 325, "Sour Cream": 171,
    "Almond Milk": 15, "Apple Sauce": 68, "Brown Rice": 123, "Cauliflower Rice": 25,
    "Cherry Tomato": 18, "Chia Egg": 486, "Fish": 206, "Fish Sauce": 60, "Garam Masala": 350,
    "Ghee": 900, "Ginger": 80, "Honey": 304, "Kale": 49, "Lettuce": 15, "Mandarin": 53,
    "Maple Syrup": 260, "Mushroom Protein": 90, "Oat Flour": 404, "Oat Milk": 44, "Olive Oil": 884,
    "Paprika": 282, "Pear": 57, "Quinoa": 120, "Red Pepper Flakes": 318, "Rice Noodles": 109,
    "Shallot": 72, "Shirataki Noodles": 10, "Soy Milk": 54, "Stevia": 0, "Tamari": 60, "Tofu": 76,
    "Tofu Scramble": 95, "Tomato Paste": 82, "Turkey": 189, "Whole Wheat Flour": 332
}

# Categorization for the recipe selection UI
COMMON_INGREDIENTS = {
    "Grains & Staples": ["Rice", "Noodles"],
    "Protein Sources": ["Chicken", "Beef", "Pork", "Egg"],
    "Dairy Products": ["Milk", "Butter"],
    "Baking Essentials": ["Flour", "Sugar"],
    "Basic Pantry Ingredients": ["Salt", "Cooking Oil"],
    "Vegetables": ["Tomato", "Spinach", "Spring Onion", "Onion", "Garlic"],
    "Fruits": ["Banana", "Apple", "Orange"],
    "Condiments & Spices": ["Soy Sauce", "Pepper", "Chili", "Curry Powder", "Sour Cream"]
}

# Reverse mapping: ingredient -> category
CATEGORY_BY_INGREDIENT = {}
for _cat, _items in COMMON_INGREDIENTS.items():
    for _ing in _items:
        CATEGORY_BY_INGREDIENT[_ing] = _cat

def get_category_for(ingredient):
    """Return the category for an ingredient, or None if not categorized."""
    return CATEGORY_BY_INGREDIENT.get(ingredient)
