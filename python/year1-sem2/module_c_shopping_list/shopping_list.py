# Module C: Shopping List Generator
# Made by Lee Gin Shyag

import tkinter as tk
from tkinter import messagebox, simpledialog

from file_io import load_json_file
from module_a_recipe_manager.ui_helpers import open_window, make_scrollable_frame

# ============================= Module C: Shopping List Generator UI =============================
def open_shopping_list_window(parent):
    """Opens the window for selecting recipes and generating a shopping list."""
    recipes = load_json_file("recipes.json", [])
    win = open_window(parent, "Shopping List Generator", "700x650")
    
    tk.Label(win, text="Select Recipes to Include in Shopping List", font=("Arial",16,"bold")).pack(pady=10)
    
    # Listbox for recipe selection
    lb = tk.Listbox(win, font=("Arial",12), selectmode="multiple", height=20); lb.pack(padx=40, pady=12, fill="both", expand=True)
    for r in recipes: lb.insert("end", r.get("name", "Unnamed"))

    def generate():
        """Calculates total ingredients and displays the shopping list."""
        sel = lb.curselection()
        if not sel:
            messagebox.showwarning("No selection", "Please select at least one recipe"); return
            
        total = {}; total_calories = 0.0
        ingredient_calories = {}  # Track calories per ingredient
        
        # Aggregate ingredients
        for i in sel:
            r = recipes[i]
            for it in r.get("ingredients", []):
                name = it.get("name"); grams = it.get("grams", 0)
                total[name] = total.get(name, 0) + grams
                calories = it.get("calories", 0)
                total_calories += calories
                ingredient_calories[name] = ingredient_calories.get(name, 0) + calories
        
        # Shopping List Display Window (Fixed size 600x800)
        res = open_window(win, "Shopping List", "600x800")
        tk.Label(res, text="Shopping List - Select Ingredients to Include", font=("Arial",16,"bold")).pack(pady=10)
        
        txtf = tk.Frame(res); txtf.pack(fill="both", expand=True, padx=10, pady=6)
        inner, _ = make_scrollable_frame(txtf)
        
        # Dictionary to track checkbox states
        ingredient_vars = {}
        
        # ==========================================================
        # Grid Layout with Checkboxes
        # ==========================================================
        
        # Header Row 
        title_frame = tk.Frame(inner)
        title_frame.pack(fill="x", anchor="n", pady=(4, 2))
        
        tk.Label(title_frame, text="Include", font=("Arial", 12, "bold"), anchor="w", width=8).grid(row=0, column=0, sticky="w", padx=(20, 5))
        tk.Label(title_frame, text="Ingredient", font=("Arial", 12, "bold"), anchor="w", width=25).grid(row=0, column=1, sticky="w", padx=(5, 10))
        tk.Label(title_frame, text="Quantity", font=("Arial", 12, "bold"), anchor="e", width=10).grid(row=0, column=2, sticky="e", padx=10)
        
        title_frame.grid_columnconfigure(0, weight=0) 
        title_frame.grid_columnconfigure(1, weight=1) 
        title_frame.grid_columnconfigure(2, weight=0)

        # Separator
        tk.Label(inner, text="-"*60).pack(anchor="w", fill="x")

        # Shopping List Items with Checkboxes
        def update_total_calories():
            """Recalculates and updates the total calories based on selected ingredients."""
            selected_calories = sum(ingredient_calories[k] for k, v in ingredient_vars.items() if v.get())
            calories_label.config(text=f"Estimated total calories from selected ingredients: {round(selected_calories, 1)} kcal")
        
        row_idx = 0
        for k, v in sorted(total.items()):
            # Create a Frame for each row
            item_frame = tk.Frame(inner)
            item_frame.pack(fill="x", anchor="n")
            
            # Checkbox (selected by default)
            var = tk.BooleanVar(value=True)
            ingredient_vars[k] = var
            cb = tk.Checkbutton(item_frame, variable=var, command=update_total_calories)
            cb.grid(row=0, column=0, sticky="w", padx=(20, 5), pady=2)
            
            # Ingredient Name
            tk.Label(item_frame, text=k, font=("Arial",12), anchor="w").grid(row=0, column=1, sticky="w", padx=(5, 10), pady=2)
            
            # Quantity and Unit
            tk.Label(item_frame, text=f"{v} g", font=("Arial",12), anchor="e").grid(row=0, column=2, sticky="e", padx=10, pady=2)
            
            item_frame.grid_columnconfigure(0, weight=0)
            item_frame.grid_columnconfigure(1, weight=1) 
            item_frame.grid_columnconfigure(2, weight=0)
            row_idx += 1
            
        # Footer information
        tk.Label(inner, text="-"*60).pack(anchor="w", fill="x", pady=(8,2))
        calories_label = tk.Label(inner, text=f"Estimated total calories from selected ingredients: {round(total_calories,1)} kcal", font=("Arial",12,"bold"))
        calories_label.pack(anchor="w", padx=20, pady=6)
        
        # ==========================================================
        
        def export_txt():
            """Exports the list to a .txt file (only selected ingredients)."""
            selected_items = {k: v for k, v in total.items() if ingredient_vars[k].get()}
            if not selected_items:
                messagebox.showwarning("No Selection", "Please select at least one ingredient to export")
                return
                
            selected_calories = sum(ingredient_calories[k] for k in selected_items.keys())
            sw = ["Shopping List", "="*30] + [f"{k}: {v} g" for k,v in sorted(selected_items.items())] + ["", f"Estimated total calories: {round(selected_calories,1)} kcal"]
            fname = simpledialog.askstring("Save As", "Enter file name (without extension):", parent=res)
            if fname:
                try:
                    with open(fname + ".txt", "w", encoding="utf-8") as f: f.write("\n".join(sw))
                    messagebox.showinfo("Saved", f"Saved to {fname}.txt")
                except Exception as e:
                    messagebox.showerror("Error", f"Failed to save: {e}")
                    
        tk.Button(res, text="Export as TXT", bg="#27ae60", fg="white", font=("Arial",12,"bold"), command=export_txt).pack(pady=8)

    tk.Button(win, text="Generate List", bg="#e67e22", fg="white", font=("Arial",12,"bold"), width=30, command=generate).pack(pady=8)