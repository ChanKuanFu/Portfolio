# Recipe Manager - Year 1 Sem 2 (Python)

A Python-based recipe management system that allows users to record recipes, 
calculate nutritional information, and get ingredient substitution suggestions 
when items are unavailable.

## Features

- **Recipe Recording** — Store and manage personal recipes with ingredients 
  and instructions
- **Nutritional Calculation** — Calculate nutritional information based on 
  recipe ingredients
- **Ingredient Substitution** — Suggests alternative ingredients when a 
  required item is out of stock
- **Shopping List Generation** — Generates a shopping list based on selected 
  recipes

## Project Structure

year1-sem2/
├── main.py # Entry point of the application
├── file_io.py # Handles reading/writing data files
├── module_a_recipe_manager/ # Recipe recording & management logic
├── module_b_substitutes/ # Ingredient substitution logic
├── module_c_shopping_list/ # Shopping list generation logic
├── recipes.json # Stored recipe data
├── substitutes.json # Ingredient substitution data
├── weekly_log.json # Weekly usage/tracking log
└── shopping list.txt # Generated shopping list output


## How to Run

```bash
python main.py
```

## What I Learned

Working on this project helped me practice modular Python design — 
splitting functionality across separate files/modules — as well as 
working with JSON for persistent data storage and building simple 
file I/O logic from scratch.
