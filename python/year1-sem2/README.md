# Smart Recipe and Nutrition Manager - Year 1 Sem 2 (Python)

A Python/Tkinter desktop application that helps users organize recipes,
track nutrition, plan weekly meals, find healthier ingredient substitutes,
and generate shopping lists — all through a GUI, with data persisted to
JSON files between sessions.

## Features

- **Recipe Organizer & Nutrition** — Add, edit, view, and delete recipes
  with categorized ingredient checklists, custom ingredients, and
  auto-calculated calories per ingredient (grams × kcal/100g)
- **Meal Tracker** — Plan meals across a 7-day week, switch between
  logged weeks, and view a live calorie summary per day and per week
- **Ingredient Substitutes** — Browse built-in "smart" substitute
  suggestions grouped by category (e.g. Butter → Olive Oil, Avocado, Ghee),
  add/edit/delete custom substitutes, and see substitutes colour-coded by
  calorie density
- **Shopping List Generator** — Select one or more recipes, aggregate
  their ingredients and quantities, toggle items in/out with checkboxes,
  see the estimated total calories, and export the list as a `.txt` file
- **Shared Utilities** — Centralized file I/O (`file_io.py`) for loading
  and saving JSON, a shared nutrition database, and reusable UI helpers
  (scrollable frames, centered windows, mousewheel binding, legacy
  ingredient-format parsing)
- **Data Persistence** — Recipes, substitutes, and the weekly meal log are
  saved and reloaded from JSON files, so data survives between runs

## Project Structure

```
recipe_nutrition_assistant/
├── main.py                          # Entry point — builds the main window and wires up all three modules
├── file_io.py                       # Shared JSON load/save helpers, nutrition database, default data
├── module_a_recipe_manager/
│   ├── __init__.py
│   ├── recipe_manager.py            # Add/edit/view/delete recipes, substitute-aware ingredient picker
│   ├── meal_tracker.py              # Weekly meal planner with per-day/per-week calorie summaries
│   └── ui_helpers.py                # Shared Tkinter helpers (windows, scrollable frames, legacy parsing)
├── module_b_substitutes/
│   ├── __init__.py
│   └── substitutes.py               # Browse/add/edit/delete ingredient substitutes
├── module_c_shopping_list/
│   ├── __init__.py
│   └── shopping_list.py             # Aggregate recipe ingredients into a selectable, exportable shopping list
├── recipes.json                     # Saved recipes (created automatically on first run)
├── substitutes.json                 # Custom substitutes (created automatically on first run)
└── weekly_log.json                  # Weekly meal log (created automatically on first run)
```

## How to Run

```bash
# Requires Python 3 (Tkinter ships with the standard library)
python main.py
```

> Note: `main.py` and `file_io.py` must stay at the project root, alongside
> the `module_a_recipe_manager`, `module_b_substitutes`, and
> `module_c_shopping_list` package folders, since the modules import from
> each other using these relative/package paths.

On first run, the system automatically creates `recipes.json`,
`substitutes.json`, and `weekly_log.json` with sensible defaults (including
a starter set of ingredient substitutes), so you can start adding recipes
and planning meals right away.

## What I Learned

Building this project helped me practice structuring a multi-module Python
application with `tkinter` — splitting recipe management, meal tracking,
substitutes, and shopping lists into separate packages that share common
utilities (`file_io.py`, `ui_helpers.py`) instead of duplicating logic. It
also gave me practice with JSON-based persistence without a database,
building scrollable/dynamic GUI panels that update in response to user
selections, and collaborating across modules that were split and owned by
different teammates.
