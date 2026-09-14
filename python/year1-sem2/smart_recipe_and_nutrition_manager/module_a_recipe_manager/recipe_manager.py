# Module A: Recipe Organizer & Nutrition - Recipe Management
# Made by RAM

import tkinter as tk
from tkinter import messagebox, ttk
from datetime import datetime

from file_io import *
from .ui_helpers import open_window, make_scrollable_frame, parse_old_ingredients, bind_mousewheel

# ============================= Load Substitutes Data =============================
def load_substitutes():
    """Load and merge all substitute data from JSON and built-in sources."""
    # Built-in smart substitutes
    SUBSTITUTE_SMART = {
        "Rice": ["Brown Rice", "Quinoa", "Cauliflower Rice"],
        "Noodles": ["Rice Noodles", "Shirataki Noodles"],
        "Chicken": ["Turkey", "Tofu", "Fish"],
        "Beef": ["Turkey", "Chicken", "Mushroom Protein"],
        "Pork": ["Chicken", "Tofu"],
        "Egg": ["Tofu Scramble", "Chia Egg"],
        "Milk": ["Soy Milk", "Oat Milk", "Almond Milk"],
        "Butter": ["Olive Oil", "Avocado", "Ghee"],
        "Flour": ["Whole Wheat Flour", "Oat Flour"],
        "Sugar": ["Honey", "Maple Syrup", "Stevia"],
        "Tomato": ["Cherry Tomato", "Tomato Paste"],
        "Spinach": ["Kale", "Lettuce"],
        "Spring Onion": ["Onion", "Garlic"],
        "Onion": ["Shallot"],
        "Garlic": ["Ginger"],
        "Banana": ["Apple Sauce"],
        "Apple": ["Pear"],
        "Orange": ["Mandarin"],
        "Soy Sauce": ["Tamari", "Fish Sauce"],
        "Pepper": ["Chili", "Paprika"],
        "Chili": ["Red Pepper Flakes"],
        "Curry Powder": ["Garam Masala"]
    }
    
    # Load custom substitutes from JSON
    custom_substitutes_data = load_json_file(SUBSTITUTE_FILE, DEFAULT_SUBSTITUTES)
    
    # Merge custom data into SUBSTITUTE_SMART
    for original, subs_str in custom_substitutes_data.items():
        if original not in SUBSTITUTE_SMART:
            SUBSTITUTE_SMART[original] = []
        
        subs_list = [s.strip() for s in subs_str.split(',') if s.strip()]
        for sub in subs_list:
            if sub and sub not in SUBSTITUTE_SMART[original]:
                SUBSTITUTE_SMART[original].append(sub)
    
    return SUBSTITUTE_SMART



# ============================= Module A: Add / Edit Recipe UI =============================
def open_add_recipe_window(parent, edit_mode=False, recipe_index=None):
    """Opens the interface for adding a new recipe or editing an existing one."""
    win = open_window(parent, "Edit Recipe" if edit_mode else "Add Recipe", "1100x900")
    
    #Prevent Root (main menu) from getting back focus
    win.transient(parent)
    win.grab_set()
    win.focus_force()

    win.minsize(1400, 800)
    recipes = load_json_file(RECIPE_FILE, [])

    tk.Label(win, text="Recipe Name:", font=("Arial", 12, "bold")).pack(anchor="w", padx=60, pady=(40,8))
    name_entry = tk.Entry(win, font=("Arial", 12), width=70)
    name_entry.pack(pady=8, padx=60)

    catf = tk.Frame(win); catf.pack(pady=20)
    tk.Label(catf, text="Category:", font=("Arial", 11)).pack(side="left", padx=(60,10))
    cat_var = tk.StringVar(value="Dinner")
    # Dropdown for meal category selection
    ttk.Combobox(catf, textvariable=cat_var, values=("Breakfast","Lunch","Dinner","Snack","Dessert"),
                 state="readonly", width=25).pack(side="left")

    mainf = tk.LabelFrame(win, text=" Ingredients (select + enter grams) ", font=("Arial", 12, "bold"), padx=20, pady=15)
    mainf.pack(pady=30, padx=60, fill="both", expand=True)

    # Setup for ingredient selection area
    left_container = tk.Frame(mainf); left_container.pack(side="left", fill="both", expand=True)
    
    # Create scrollable frame manually for better control
    leftf_canvas = tk.Canvas(left_container, width=480)
    leftf_scrollbar = tk.Scrollbar(left_container, orient="vertical", command=leftf_canvas.yview)
    leftf = tk.Frame(leftf_canvas)
    
    leftf_canvas.configure(yscrollcommand=leftf_scrollbar.set)
    leftf_scrollbar.pack(side="right", fill="y")
    leftf_canvas.pack(side="left", fill="both", expand=True)
    
    leftf_window = leftf_canvas.create_window((0, 0), window=leftf, anchor="nw")
    
    # Mousewheel binding for left panel
    def on_left_mousewheel(event):
        leftf_canvas.yview_scroll(int(-1*(event.delta/120)), "units")
    
    def bind_left_mousewheel(event):
        leftf_canvas.bind_all("<MouseWheel>", on_left_mousewheel)
    
    def unbind_left_mousewheel(event):
        leftf_canvas.unbind_all("<MouseWheel>")
    
    leftf_canvas.bind("<Enter>", bind_left_mousewheel)
    leftf_canvas.bind("<Leave>", unbind_left_mousewheel)
    
    # Configure scroll region
    def configure_left_scroll(event=None):
        leftf_canvas.configure(scrollregion=leftf_canvas.bbox("all"))
        leftf_canvas.itemconfig(leftf_window, width=leftf_canvas.winfo_width())
    
    leftf.bind("<Configure>", configure_left_scroll)
    leftf_canvas.bind("<Configure>", configure_left_scroll)
    
    rightf = tk.LabelFrame(mainf, text=" Selected Ingredients "); rightf.pack(side="right", fill="both", expand=True, padx=(30,0))
    
    check_vars = {}
    selected = {}
    row = 0
    
    # Load substitutes data
    substitutes = load_substitutes()

    # Generate categorized checkboxes for common ingredients
    for cat_name, items in COMMON_INGREDIENTS.items():
        v = tk.BooleanVar()
        # Category header checkbox
        cb = tk.Checkbutton(leftf, text=f" {cat_name} ", variable=v, font=("Arial", 12, "bold"))
        cb.grid(row=row, column=0, sticky="w", padx=10, pady=(20,5)); row += 1

        # Frame for sub-items (hidden by default)
        subf = tk.Frame(leftf); subf.grid(row=row, column=0, sticky="w", padx=40); subf.grid_remove(); row += 1
        sub_row = 0
        for item in items:
            iv = tk.BooleanVar(); check_vars[item] = iv
            # Individual ingredient checkbox
            tk.Checkbutton(subf, text=item, variable=iv, font=("Arial", 11), command=lambda n=item: update_item(n, check_vars, selected, rightf, substitutes)).grid(row=sub_row, column=0, sticky="w", pady=1)
            sub_row += 1
        # Toggle sub-items visibility based on category checkbox
        v.trace_add("write", lambda *a, ff=subf, vv=v: (ff.grid() if vv.get() else ff.grid_remove()))

    # Custom ingredient section
    tk.Label(leftf, text="\nAdd Custom Ingredient:", font=("Arial", 11, "bold")).grid(row=row, column=0, sticky="w", padx=10, pady=(30,10)); row += 1
    cf = tk.Frame(leftf); cf.grid(row=row, column=0, sticky="ew", padx=10, pady=10)
    centry = tk.Entry(cf, font=("Arial", 11), width=20); centry.pack(side="left", padx=(0,6))
    calentry = tk.Entry(cf, font=("Arial", 11), width=10); calentry.pack(side="left", padx=(0,6))
    tk.Button(cf, text="Add", width=10, bg="#27ae60", fg="white", command=lambda: add_custom(centry, calentry, leftf, check_vars, selected, rightf, substitutes)).pack(side="right")

    # If editing, load existing recipe data
    if edit_mode and recipe_index is not None and 0 <= recipe_index < len(recipes):
        r = recipes[recipe_index]
        name_entry.insert(0, r.get("name",""))
        cat_var.set(r.get("category","Dinner"))
        ing_field = r.get("ingredients", [])
        # Ensure ingredients are in the new dict format
        items = parse_old_ingredients(ing_field) if isinstance(ing_field, str) or (isinstance(ing_field, list) and ing_field and not isinstance(ing_field[0], dict)) else (ing_field if isinstance(ing_field, list) else [])
        
        for it in items:
            np = it.get("name"); g = it.get("grams", 100)
            # Add custom item checkbox if it's new
            if np not in check_vars:
                check_vars[np] = tk.BooleanVar(value=True)
                tk.Checkbutton(leftf, text=np, variable=check_vars[np], command=lambda n=np: update_item(n, check_vars, selected, rightf, substitutes)).grid(row=row, column=0, sticky="w", padx=40, pady=1); row += 1
            # Mark as selected and populate grams
            check_vars[np].set(True)
            update_item(np, check_vars, selected, rightf, substitutes)
            if np in selected:
                selected[np][1].delete(0, "end"); selected[np][1].insert(0, str(g))

    # Action buttons
    btnf = tk.Frame(win); btnf.pack(pady=30)
    tk.Button(btnf, text="Confirm & Save", bg="#27ae60", fg="white", font=("Arial",14,"bold"), width=28, height=2,
              command=lambda: save_recipe(name_entry, cat_var, selected, win, edit_mode, recipe_index)).pack(side="left", padx=20)
    tk.Button(btnf, text="Exit", bg="#c0392b", fg="white", font=("Arial",14,"bold"), width=28, height=2,
              command=lambda: (win.destroy() if messagebox.askyesno("Exit", "Discard changes?") else None)).pack(side="left", padx=20)

def update_item(name, check_vars, selected, rightf, substitutes):
    """Adds or removes an ingredient entry in the 'Selected Ingredients' panel."""
    if check_vars.get(name) and check_vars[name].get():
        if name in selected: return # Already selected
        
        # Create UI elements for the selected ingredient
        f = tk.Frame(rightf); f.pack(fill="x", pady=4, padx=10)
        tk.Label(f, text=name, width=25, anchor="w", font=("Arial", 11)).pack(side="left")
        e = tk.Entry(f, width=10, font=("Arial", 11)); e.insert(0, "100"); e.pack(side="left", padx=8)
        tk.Label(f, text="g").pack(side="left")
        
        cal100 = INGREDIENT_NUTRITION.get(name, 0)
        tk.Label(f, text=f"{cal100} kcal/100g", width=15, anchor="w").pack(side="left", padx=8)
        
        tk.Button(f, text="Remove", bg="#e74c3c", fg="white", width=10,
                  command=lambda ff=f, n=name: remove_selected(ff, n, selected, check_vars)).pack(side="right")
        
        # Check if this ingredient has substitutes
        subs = substitutes.get(name, [])
        if subs:
            # Create a frame to show substitute options
            subs_frame = tk.Frame(rightf, bg="#f0f0f0")
            subs_frame.pack(fill="x", pady=(0, 4), padx=20)
            
            tk.Label(subs_frame, text="  ↳ Available substitutes:", font=("Arial", 9, "italic"), 
                    fg="#555", bg="#f0f0f0", anchor="w").pack(side="left")
            
            for sub in subs:
                sub_cal = INGREDIENT_NUTRITION.get(sub, 0)
                tk.Button(subs_frame, text=f"{sub} ({sub_cal} kcal)", font=("Arial", 9), 
                         bg="#3498db", fg="white", relief="raised", padx=8, pady=2,
                         command=lambda s=sub, orig=name: add_substitute_ingredient(s, check_vars, selected, rightf, substitutes, orig)).pack(side="left", padx=3)
            
            selected[name] = (f, e, subs_frame)
        else:
            selected[name] = (f, e, None)
    else:
        # If unchecked, remove from the list
        if name in selected:
            selected[name][0].destroy()
            if selected[name][2] is not None:  # Remove substitute frame if exists
                selected[name][2].destroy()
            del selected[name]

def add_substitute_ingredient(sub_name, check_vars, selected, rightf, substitutes, original_name=None):
    """Adds a substitute ingredient and removes the original ingredient."""
    # Find the original ingredient that this substitute belongs to
    if original_name is None:
        for orig, subs_list in substitutes.items():
            if sub_name in subs_list and orig in selected:
                original_name = orig
                break
    
    # Remove the original ingredient if found
    if original_name and original_name in selected:
        # Store the grams value from the original ingredient
        original_grams = selected[original_name][1].get()
        
        # Remove the original ingredient
        remove_selected(selected[original_name][0], original_name, selected, check_vars)
    else:
        original_grams = "100"  # Default value
    
    # Add the substitute ingredient
    if sub_name not in check_vars:
        # Create checkbox for the substitute if it doesn't exist
        check_vars[sub_name] = tk.BooleanVar()
    
    # Set to selected
    check_vars[sub_name].set(True)
    
    # Add to the selected list
    update_item(sub_name, check_vars, selected, rightf, substitutes)
    
    # Copy the grams value from original ingredient
    if sub_name in selected:
        selected[sub_name][1].delete(0, "end")
        selected[sub_name][1].insert(0, original_grams)

def remove_selected(frame, name, selected, check_vars):
    """Removes an item from the selected list and unchecks its box."""
    try:
        frame.destroy()
        if name in selected and selected[name][2] is not None:  # Remove substitute frame if exists
            selected[name][2].destroy()
        del selected[name]
        check_vars[name].set(False)
    except Exception:
        pass

def add_custom(centry, calentry, leftf, check_vars, selected, rightf, substitutes):
    """Adds a user-defined ingredient to the available list and nutrition database."""
    name = centry.get().strip()
    if not name:
        messagebox.showwarning("Empty", "Enter name"); return
    if name in check_vars:
        messagebox.showwarning("Exist", "Already exists"); return
        
    try:
        cal_val = float(calentry.get()) if calentry.get().strip() else 0.0
    except Exception:
        messagebox.showwarning("Invalid", "Calories must be numeric"); return
        
    # Add to global lists/dictionaries
    COMMON_INGREDIENTS.setdefault("Others", []).append(name)
    INGREDIENT_NUTRITION[name] = cal_val
    
    # Create the new checkbox dynamically
    v = tk.BooleanVar(); check_vars[name] = v
    tk.Checkbutton(leftf, text=name, variable=v, font=("Arial", 11), command=lambda n=name: update_item(n, check_vars, selected, rightf, substitutes)).grid(sticky="w", padx=40, pady=1)
    
    messagebox.showinfo("Success", f"'{name}' added with {cal_val} kcal/100g")
    centry.delete(0, "end"); calentry.delete(0, "end")

def save_recipe(name_entry, cat_var, selected, win, edit_mode, recipe_index):
    """Collects data from the UI and saves the recipe to the JSON file."""
    if not name_entry.get().strip():
        messagebox.showerror("Error", "Recipe name required"); return
        
    recipes_local = load_json_file(RECIPE_FILE, [])
    items = []
    
    # Process selected ingredients
    for n, item_data in list(selected.items()):
        # item_data can be (frame, entry) or (frame, entry, subs_frame)
        entry = item_data[1]
        try:
            g = max(1, int(float(entry.get() or 100)))
        except:
            g = 100
        cal100 = INGREDIENT_NUTRITION.get(n, 0)
        total_cal = round(cal100 * g / 100, 1)
        items.append({"name": n, "grams": g, "calories": total_cal})
        
    newr = {"name": name_entry.get().strip(), "category": cat_var.get(), "ingredients": items, "created_at": datetime.now().isoformat()}
    
    # Update or append the recipe
    if edit_mode and recipe_index is not None:
        if 0 <= recipe_index < len(recipes_local):
            recipes_local[recipe_index] = newr
        else:
            recipes_local.append(newr)
    else:
        recipes_local.append(newr)
        
    if save_json_file(RECIPE_FILE, recipes_local):
        messagebox.showinfo("Success", "Saved!"); win.destroy()
    else:
        messagebox.showerror("Error", "Failed to save file")

# ============================= Module A: View Recipes & Detail UI =============================
def open_recipe_detail_window(parent, recipe):
    """Opens a separate window to show detailed ingredient and calorie breakdown."""
    w = open_window(parent, recipe.get("name", "Detail"), "600x550")
    w.minsize(500, 400)
    tk.Label(w, text=recipe.get("name",""), font=("Arial", 16, "bold")).pack(pady=10)
    tk.Label(w, text=f"Category: {recipe.get('category','')}", font=("Arial", 11)).pack(pady=(0,10))
    
    # Scrollable area for ingredient list
    frame = tk.Frame(w); frame.pack(padx=20, pady=10, fill="both", expand=True)
    
    # Create scrollable frame manually
    detail_canvas = tk.Canvas(frame)
    detail_scrollbar = tk.Scrollbar(frame, orient="vertical", command=detail_canvas.yview)
    inner = tk.Frame(detail_canvas)
    
    detail_canvas.configure(yscrollcommand=detail_scrollbar.set)
    detail_scrollbar.pack(side="right", fill="y")
    detail_canvas.pack(side="left", fill="both", expand=True)
    
    detail_window = detail_canvas.create_window((0, 0), window=inner, anchor="nw")
    
    # Mousewheel binding for detail panel
    def on_detail_mousewheel(event):
        detail_canvas.yview_scroll(int(-1*(event.delta/120)), "units")
    
    def bind_detail_mousewheel(event):
        detail_canvas.bind_all("<MouseWheel>", on_detail_mousewheel)
    
    def unbind_detail_mousewheel(event):
        detail_canvas.unbind_all("<MouseWheel>")
    
    detail_canvas.bind("<Enter>", bind_detail_mousewheel)
    detail_canvas.bind("<Leave>", unbind_detail_mousewheel)
    
    # Configure scroll region
    def configure_detail_scroll(event=None):
        detail_canvas.configure(scrollregion=detail_canvas.bbox("all"))
        detail_canvas.itemconfig(detail_window, width=detail_canvas.winfo_width())
    
    inner.bind("<Configure>", configure_detail_scroll)
    detail_canvas.bind("<Configure>", configure_detail_scroll)
    
    # Configure grid layout
    inner.columnconfigure(0, weight=3) # Ingredient name
    inner.columnconfigure(1, weight=1) # Grams
    inner.columnconfigure(2, weight=1) # Calories
    
    total = 0
    # Header for the table
    tk.Label(inner, text="Ingredient", font=("Arial", 11, "bold")).grid(row=0, column=0, sticky="w", pady=(4,6))
    tk.Label(inner, text="Grams", font=("Arial", 11, "bold")).grid(row=0, column=1, sticky="w", pady=(4,6))
    tk.Label(inner, text="Calories", font=("Arial", 11, "bold")).grid(row=0, column=2, sticky="w", pady=(4,6))
    
    # Separator
    separator = ttk.Separator(inner, orient='horizontal')
    separator.grid(row=1, column=0, columnspan=3, sticky='ew')
    
    # Ingredient rows
    row_num = 2
    for it in recipe.get("ingredients", []):
        name = it.get("name", "")
        grams = it.get("grams", 0)
        calories = it.get("calories", 0)
        total += calories
        
        tk.Label(inner, text=name, font=("Courier New", 13), wraplength=250, justify="left").grid(row=row_num, column=0, sticky="w", pady=2)
        tk.Label(inner, text=f"{grams}g", font=("Courier New", 13)).grid(row=row_num, column=1, sticky="w", pady=2)
        tk.Label(inner, text=f"{calories} kcal", font=("Courier New", 13)).grid(row=row_num, column=2, sticky="w", pady=2)
        row_num += 1
        
    # Separator
    separator2 = ttk.Separator(inner, orient='horizontal')
    separator2.grid(row=row_num, column=0, columnspan=3, sticky='ew', pady=(8,2))
    row_num += 1

    tk.Label(inner, text=f"Total Calories: {round(total,1)} kcal", font=("Arial", 12, "bold")).grid(row=row_num, column=0, columnspan=3, sticky="w", pady=6)

def open_view_recipes_window(parent):
    """Opens the main list of all saved recipes."""
    recipes = load_json_file(RECIPE_FILE, [])
    
    # Pre-check and convert old recipe formats if necessary
    converted = False
    for idx, r in enumerate(recipes):
        ing = r.get("ingredients", [])
        if isinstance(ing, str) or (isinstance(ing, list) and ing and not isinstance(ing[0], dict)):
            recipes[idx]["ingredients"] = parse_old_ingredients(ing); converted = True
    if converted:
        save_json_file(RECIPE_FILE, recipes)
        
    w = open_window(parent, "View Recipes", "900x660")
    tk.Label(w, text="All Saved Recipes", font=("Arial", 18, "bold")).pack(pady=20)
    
    lb = tk.Listbox(w, font=("Arial", 11), height=25); lb.pack(padx=60, pady=10, fill="both", expand=True)
    
    # Populate the listbox
    for r in recipes:
        total = sum(x.get("calories",0) for x in r.get("ingredients", []))
        lb.insert("end", f"{r.get('name','Unnamed')}  [{r.get('category','')}]  - {round(total,1)} kcal")
        
    def edit():
        """Edits the selected recipe."""
        s = lb.curselection()
        if s:
            w.destroy(); open_add_recipe_window(parent, True, s[0])
            
    def view():
        """Opens the detail window for the selected recipe."""
        s = lb.curselection()
        if s:
            open_recipe_detail_window(w, recipes[s[0]])
            
    def delete():
        """Deletes the selected recipe."""
        s = lb.curselection()
        if s and messagebox.askyesno("Delete", "Delete selected recipe?"):
            del recipes[s[0]]; save_json_file(RECIPE_FILE, recipes); w.destroy(); open_view_recipes_window(parent)
            
    # Buttons for list management
    btnf = tk.Frame(w); btnf.pack(pady=10)
    tk.Button(btnf, text="View Selected", bg="#2980b9", fg="white", font=("Arial",12,"bold"), width=20, command=view).pack(side="left", padx=10)
    tk.Button(btnf, text="Edit Selected", bg="#e67e22", fg="white", font=("Arial",12,"bold"), width=20, command=edit).pack(side="left", padx=10)
    tk.Button(btnf, text="Delete Selected", bg="#c0392b", fg="white", font=("Arial",12,"bold"), width=20, command=delete).pack(side="left", padx=10)