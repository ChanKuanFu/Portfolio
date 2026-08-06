# Main Application UI
# Made by RAM (coordinated all modules)

import tkinter as tk
from module_a_recipe_manager.recipe_manager import (
    open_add_recipe_window, 
    open_view_recipes_window
)
from module_a_recipe_manager.meal_tracker import open_weekly_meal_log_window
from module_b_substitutes.substitutes import open_substitute_window
from module_c_shopping_list.shopping_list import open_shopping_list_window
from file_io import ensure_files

def main():
    """Initializes the main application window and buttons."""
    ensure_files() # Ensure all necessary JSON files exist
    
    root = tk.Tk(); 
    root.title("Recipe & Nutrition Assistant"); 
    
    # Manually setting geometry for the root window and centering it
    root_width = 780
    root_height = 860
    
    # Force update_idletasks for correct screen dimensions
    root.update_idletasks() 
    
    screen_w = root.winfo_screenwidth()
    screen_h = root.winfo_screenheight()
    
    x = (screen_w // 2) - (root_width // 2)
    y = (screen_h // 2) - (root_height // 2)
    
    root.geometry(f'{root_width}x{root_height}+{x}+{y}') 
    root.configure(bg="#f0f0f0")
    
    tk.Label(root, text="Recipe & Nutrition Assistant", font=("Arial", 30, "bold"), bg="#2C3E50", fg="white", width=root_width, anchor="center").pack(pady=(0,10), fill="x")
    
    # Recipe Management Buttons
    tk.Label(root, text="Module A: Recipe Organizer & Nutrition", font=("Arial", 14, "bold"), bg="#f0f0f0").pack(pady=(30,10))
    btn_frame1 = tk.Frame(root, bg="#f0f0f0"); btn_frame1.pack(pady=10)
    tk.Button(btn_frame1, text="   Add Recipe   ", font=("Arial", 14, "bold"), bg="#27ae60", fg="white", width=20, height=2,
              command=lambda: open_add_recipe_window(root)).pack(side="left", padx=20)
    tk.Button(btn_frame1, text="   View Recipes   ", font=("Arial", 14, "bold"), bg="#2980b9", fg="white", width=20, height=2,
              command=lambda: open_view_recipes_window(root)).pack(side="left", padx=20)
                  
    # Meal Tracker Button (formerly Weekly Log)
    tk.Button(root, text="Meal Tracker", font=("Arial", 14, "bold"), bg="#16a085", fg="white", width=52, height=2,
              command=lambda: open_weekly_meal_log_window(root)).pack(pady=20)
              
    # Substitute Module Button
    tk.Label(root, text="Module B: Ingredient Substitutes", font=("Arial", 14, "bold"), bg="#f0f0f0").pack(pady=(30,10))
    tk.Button(root, text="Find Substitutes", font=("Arial", 14, "bold"), bg="#9b59b6", fg="white", width=52, height=2,
              command=lambda: open_substitute_window(root)).pack(pady=8)
              
    # Shopping List Button
    tk.Label(root, text="Module C: Shopping List", font=("Arial", 14, "bold"), bg="#f0f0f0").pack(pady=(30,10))
    tk.Button(root, text="Generate Shopping List", font=("Arial", 14, "bold"), bg="#e67e22", fg="white", width=52, height=2,
              command=lambda: open_shopping_list_window(root)).pack(pady=8)
              
    root.mainloop()

if __name__ == "__main__":
    main()