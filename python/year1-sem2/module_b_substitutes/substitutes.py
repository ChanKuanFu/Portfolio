# Module B: Ingredient Substitutes
# Made by Chan Kuan Fu

import tkinter as tk
from tkinter import messagebox, ttk
import re

from file_io import *
from module_a_recipe_manager.ui_helpers import open_window, make_scrollable_frame, bind_mousewheel

def open_substitute_window(parent):
    # --- Smart Substitute Recommendations (Built-in) ---
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

    # Initialize data structures for tracking custom substitutes
    custom_sub_map = {} 
    all_custom_subs = set()
    
    '''
    # Add a generic placeholder for ingredients without specific smart subs
    for k in INGREDIENT_NUTRITION.keys():
        if k not in SUBSTITUTE_SMART:
            SUBSTITUTE_SMART[k] = ["Similar Ingredient"]
    '''
    # Load user-defined substitutes
    custom_substitutes_data = load_json_file(SUBSTITUTE_FILE, DEFAULT_SUBSTITUTES)
    
    # Identify system default substitutes (cannot be deleted by user)
    default_subs_set = set()
    for subs_str in DEFAULT_SUBSTITUTES.values():
        default_subs_set.update([s.strip() for s in subs_str.split(',') if s.strip()])


    # Merge custom data into the main substitute list and nutrition DB
    for original, subs_str in custom_substitutes_data.items():
        if original not in SUBSTITUTE_SMART:
            SUBSTITUTE_SMART[original] = []

        subs_list = [s.strip() for s in subs_str.split(',') if s.strip()]
        for sub in subs_list:
            if sub and sub not in SUBSTITUTE_SMART[original]:
                SUBSTITUTE_SMART[original].append(sub)
            
            # Track custom subs that are also in the nutrition DB (excluding defaults)
            if sub and sub in INGREDIENT_NUTRITION and sub not in default_subs_set:
                custom_sub_map[sub] = original
                all_custom_subs.add(sub)


    # --- Setup Main Window ---
    win = open_window(parent, "Ingredient Substitutes", "1200x900")
    win.minsize(900, 750)

    tk.Label(
        win, text="Ingredient Substitute System",
        font=("Arial", 18, "bold")
    ).pack(pady=10)

    # Main frame: Left = Selection/Custom Input, Right = Results
    frame = tk.Frame(win) 
    frame.pack(fill="both", expand=True, padx=20, pady=5)

    # ===========================
    #      Left Panel: Selection & Custom Input
    # ===========================
    left_frame = tk.Frame(frame); left_frame.pack(side="left", fill="y", padx=5)

    tk.Label(left_frame, text="Select ingredients to substitute:",
             font=("Arial", 14, "bold")).pack(pady=5, anchor="w")

    # Create scrollable frame for checkboxes
    checkbox_container = tk.Frame(left_frame)
    checkbox_container.pack(side="left", fill="both", expand=True)
    
    checkbox_canvas = tk.Canvas(checkbox_container, width=350)
    checkbox_scrollbar = tk.Scrollbar(checkbox_container, orient="vertical", command=checkbox_canvas.yview)
    checkbox_frame = tk.Frame(checkbox_canvas)
    
    checkbox_canvas.configure(yscrollcommand=checkbox_scrollbar.set)
    checkbox_scrollbar.pack(side="right", fill="y")
    checkbox_canvas.pack(side="left", fill="both", expand=True)
    
    checkbox_window = checkbox_canvas.create_window((0, 0), window=checkbox_frame, anchor="nw")
    
    # Bind mousewheel to canvas
    def on_checkbox_mousewheel(event):
        checkbox_canvas.yview_scroll(int(-1*(event.delta/120)), "units")
    
    def bind_checkbox_mousewheel(event):
        checkbox_canvas.bind_all("<MouseWheel>", on_checkbox_mousewheel)
    
    def unbind_checkbox_mousewheel(event):
        checkbox_canvas.unbind_all("<MouseWheel>")
    
    checkbox_canvas.bind("<Enter>", bind_checkbox_mousewheel)
    checkbox_canvas.bind("<Leave>", unbind_checkbox_mousewheel)
    
    # Configure scroll region
    def configure_checkbox_scroll(event=None):
        checkbox_canvas.configure(scrollregion=checkbox_canvas.bbox("all"))
        checkbox_canvas.itemconfig(checkbox_window, width=checkbox_canvas.winfo_width())
    
    checkbox_frame.bind("<Configure>", configure_checkbox_scroll)
    checkbox_canvas.bind("<Configure>", configure_checkbox_scroll)

    # Build expanded categories by adding smart substitutes into existing categories
    categories_expanded = {cat: list(items) for cat, items in COMMON_INGREDIENTS.items()}
    for _orig, _subs in SUBSTITUTE_SMART.items():
        _cat = get_category_for(_orig)
        if not _cat:
            continue
        for _sub in _subs:
            if _sub not in categories_expanded.get(_cat, []):
                categories_expanded[_cat].append(_sub)

    # --- Generate categorized checkboxes ---
    var_dict = {}
    displayed_ingredients = set()
    
    # Collect all substitute ingredients to exclude from selection
    all_substitute_ingredients = set()
    for subs_list in SUBSTITUTE_SMART.values():
        all_substitute_ingredients.update(subs_list)
    
    for cat_name, items in categories_expanded.items():
        # Category label
        tk.Label(checkbox_frame, text=f"--- {cat_name} ---",
                 font=("Arial", 12, "italic"), fg="#2980b9").pack(anchor="w", pady=(8,2))

        for ing in items:
            # Skip if this ingredient is a substitute for another ingredient
            if ing in all_substitute_ingredients:
                continue
                
            v = tk.BooleanVar()
            cb = tk.Checkbutton(
                checkbox_frame,
                text=ing,
                variable=v,
                font=("Arial", 11)
            )
            cb.pack(anchor="w", padx=10)
            var_dict[ing] = v
            displayed_ingredients.add(ing)
            
    # Add 'Other' ingredients (non-categorized, non-custom)
    other_ings = sorted([ing for ing in INGREDIENT_NUTRITION if ing not in displayed_ingredients and ing not in all_custom_subs and ing not in default_subs_set and ing not in all_substitute_ingredients])

    if other_ings:
        tk.Label(checkbox_frame, text=f"--- Others ---",
                 font=("Arial", 12, "italic"), fg="#2980b9").pack(anchor="w", pady=(8,2))
        for ing in other_ings:
            v = tk.BooleanVar()
            cb = tk.Checkbutton(
                checkbox_frame,
                text=ing,
                variable=v,
                font=("Arial", 11)
            )
            cb.pack(anchor="w", padx=10)
            var_dict[ing] = v

    # ===========================
    #      Custom Substitute Input Section
    # ===========================
    custom_frame = tk.LabelFrame(left_frame, text=" 1. Add Custom Substitute ", font=("Arial", 12, "bold"))
    custom_frame.pack(pady=(20, 10), padx=10, fill="x")

    tk.Label(custom_frame, text="Substitute Name:", font=("Arial", 11)).grid(row=0, column=0, padx=5, pady=5, sticky="w")
    custom_name_entry = tk.Entry(custom_frame, font=("Arial", 11), width=20)
    custom_name_entry.grid(row=0, column=1, padx=5, pady=5)

    tk.Label(custom_frame, text="Calories (kcal/100g):", font=("Arial", 11)).grid(row=1, column=0, padx=5, pady=5, sticky="w")
    custom_cal_entry = tk.Entry(custom_frame, font=("Arial", 11), width=20)
    custom_cal_entry.grid(row=1, column=1, padx=5, pady=5)

    tk.Label(custom_frame, text="Ingredient to replace:", font=("Arial", 11)).grid(row=2, column=0, padx=5, pady=5, sticky="w")
    ing_to_replace_var = tk.StringVar(value="")
    
    # ComboBox with all available ingredients to replace (excluding only substitute ingredients, not originals)
    all_replaceable_keys = sorted([ing for ing in INGREDIENT_NUTRITION.keys() 
                                   if ing not in all_substitute_ingredients])
    ing_to_replace_combo = ttk.Combobox(custom_frame, textvariable=ing_to_replace_var,
                                        values=all_replaceable_keys, state="readonly", width=18)
    ing_to_replace_combo.grid(row=2, column=1, padx=5, pady=5)
    
    tk.Button(custom_frame, text="Add Custom", bg="#27ae60", fg="white", font=("Arial", 10, "bold"),
              command=lambda: add_custom_substitute()).grid(row=3, column=0, columnspan=2, pady=10)
              
    # Status label for user feedback
    status_var = tk.StringVar(value="")
    status_label = tk.Label(left_frame, textvariable=status_var, font=("Arial", 11, "bold"))
    status_label.pack(pady=5)
    
    def update_status(message, color="blue", duration=3000):
        """Displays temporary feedback message."""
        status_label.config(fg=color)
        status_var.set(message)
        # Clear the message after a delay
        win.after(duration, lambda: status_var.set(""))

    # Listbox for custom substitute deletion/editing
    delete_frame = tk.LabelFrame(left_frame, text=" 2. Manage Custom Substitutes ", font=("Arial", 12, "bold"))
    delete_frame.pack(pady=(10, 20), padx=10, fill="x")
    
    custom_lb = tk.Listbox(delete_frame, selectmode="single", height=6, font=("Arial", 11))
    custom_lb.pack(fill="x", padx=5, pady=5)
    
    def populate_custom_listbox():
        """Fills the listbox with currently tracked custom substitutes."""
        custom_lb.delete(0, "end")
        for sub in sorted(list(all_custom_subs)):
            original = custom_sub_map.get(sub, "N/A") 
            kcal = INGREDIENT_NUTRITION.get(sub, "?")
            custom_lb.insert("end", f"{sub} [{kcal} kcal] (for {original})")
            
    populate_custom_listbox()
    
    
    # ===========================
    #      Delete Custom Substitute Logic
    # ===========================
    def delete_custom_substitute():
        sel = custom_lb.curselection()
        if not sel:
            update_status("Warning: Please select a custom substitute to delete.", color="red")
            return

        item_text = custom_lb.get(sel[0])
        # Extract the substitute name from the listbox text (format: "name [kcal] (for original)")
        sub_name_match = re.match(r"(.+?)\s+\[", item_text)
        sub_name = sub_name_match.group(1).strip() if sub_name_match else item_text.split(" [")[0].strip()

        original_ing = custom_sub_map.get(sub_name)

        if not original_ing:
            update_status(f"Error: Could not find original ingredient for {sub_name}. Check the internal data.", color="red")
            return

        if sub_name in default_subs_set:
             update_status(f"Error: '{sub_name}' is a system default substitute and cannot be deleted.", color="red")
             return

        # Use temporary window focus manipulation for confirmation boxes
        parent.withdraw() 
        confirm = messagebox.askyesno("Confirm Deletion", f"Are you sure you want to permanently delete '{sub_name}' as a substitute for '{original_ing}'?")
        parent.deiconify() 
        win.lift()         
        win.focus_force()  
        
        if confirm:
            
            # 1. Remove from SUBSTITUTE_SMART list
            if original_ing in SUBSTITUTE_SMART and sub_name in SUBSTITUTE_SMART[original_ing]:
                SUBSTITUTE_SMART[original_ing].remove(sub_name)
            
            # 2. Remove from INGREDIENT_NUTRITION
            if sub_name in INGREDIENT_NUTRITION:
                del INGREDIENT_NUTRITION[sub_name]
                
            # 3. Update tracking maps
            if sub_name in custom_sub_map:
                del custom_sub_map[sub_name]
            if sub_name in all_custom_subs:
                all_custom_subs.remove(sub_name)

            # 4. Update substitutes.json file
            current_subs_str = custom_substitutes_data.get(original_ing, "")
            existing_subs = [s.strip() for s in current_subs_str.split(',') if s.strip() and s.strip() != sub_name]
            
            if existing_subs:
                custom_substitutes_data[original_ing] = ", ".join(sorted(existing_subs))
            else:
                 # Remove key if no custom subs left for this ingredient
                 if original_ing in custom_substitutes_data:
                     del custom_substitutes_data[original_ing]
                     
            save_json_file(SUBSTITUTE_FILE, custom_substitutes_data) 
            
            update_status(f"'{sub_name}' successfully deleted and removed from database.", color="green")
            
            # Refresh UI components
            populate_custom_listbox()
            # Update dropdown to exclude only substitute ingredients, not originals
            all_substitute_ingredients_updated = set()
            for subs_list in SUBSTITUTE_SMART.values():
                all_substitute_ingredients_updated.update(subs_list)
            all_ing_keys = sorted([ing for ing in INGREDIENT_NUTRITION.keys() 
                                   if ing not in all_substitute_ingredients_updated])
            ing_to_replace_combo.config(values=all_ing_keys)
            refresh_results()

    # Button frame for Delete and Edit buttons
    button_frame = tk.Frame(delete_frame)
    button_frame.pack(pady=5)
    
    delete_btn = tk.Button(button_frame, text="Delete Selected", bg="#e74c3c", fg="white", font=("Arial", 10, "bold"), 
                           command=delete_custom_substitute)
    delete_btn.pack(side="left", padx=5)
    
    edit_btn = tk.Button(button_frame, text="Edit Selected", bg="#3498db", fg="white", font=("Arial", 10, "bold"), 
                         command=lambda: edit_custom_substitute())
    edit_btn.pack(side="left", padx=5)

    
    # ===========================
    #      Edit Custom Substitute Logic
    # ===========================
    def edit_custom_substitute():
        sel = custom_lb.curselection()
        if not sel:
            update_status("Warning: Please select a custom substitute to edit.", color="red")
            return

        item_text = custom_lb.get(sel[0])
        # Extract the substitute name from the listbox text
        sub_name_match = re.match(r"(.+?)\s+\[", item_text)
        sub_name = sub_name_match.group(1).strip() if sub_name_match else item_text.split(" [")[0].strip()

        original_ing = custom_sub_map.get(sub_name)

        if not original_ing:
            update_status(f"Error: Could not find original ingredient for {sub_name}.", color="red")
            return

        if sub_name in default_subs_set:
            update_status(f"Error: '{sub_name}' is a system default substitute and cannot be edited.", color="red")
            return

        # Get current calorie value
        current_kcal = INGREDIENT_NUTRITION.get(sub_name, 0)

        # Create edit window
        edit_win = tk.Toplevel(win)
        edit_win.title(f"Edit Substitute: {sub_name}")
        edit_win.geometry("400x250")
        edit_win.transient(win)
        edit_win.grab_set()

        tk.Label(edit_win, text=f"Editing: {sub_name}", font=("Arial", 14, "bold")).pack(pady=10)
        tk.Label(edit_win, text=f"Original Ingredient: {original_ing}", font=("Arial", 11)).pack(pady=5)

        # New name entry
        tk.Label(edit_win, text="New Name:", font=("Arial", 11)).pack(pady=5)
        new_name_entry = tk.Entry(edit_win, font=("Arial", 11), width=30)
        new_name_entry.insert(0, sub_name)
        new_name_entry.pack(pady=5)

        # New calorie entry
        tk.Label(edit_win, text="Calories (kcal/100g):", font=("Arial", 11)).pack(pady=5)
        new_kcal_entry = tk.Entry(edit_win, font=("Arial", 11), width=30)
        new_kcal_entry.insert(0, str(current_kcal))
        new_kcal_entry.pack(pady=5)

        def save_edit():
            new_name = new_name_entry.get().strip()
            new_kcal_str = new_kcal_entry.get().strip()

            if not new_name:
                messagebox.showerror("Error", "Name cannot be empty.")
                return

            try:
                new_kcal = float(new_kcal_str) if new_kcal_str else 0.0
            except:
                messagebox.showerror("Error", "Calories must be numeric.")
                return

            # Check if name changed and if new name already exists as a substitute
            if new_name != sub_name:
                for orig_ing, subs_list in SUBSTITUTE_SMART.items():
                    if new_name in subs_list:
                        messagebox.showerror("Error", f"Duplicate Substitutes not allowed: '{new_name}' is already a substitute for '{orig_ing}'.")
                        return

                # Check for conflict with existing base ingredients
                if new_name in INGREDIENT_NUTRITION and new_name not in all_custom_subs and new_name not in default_subs_set:
                    messagebox.showerror("Error", f"'{new_name}' already exists as a base ingredient.")
                    return

            # Update the substitute
            # 1. Remove old entry from SUBSTITUTE_SMART
            if original_ing in SUBSTITUTE_SMART and sub_name in SUBSTITUTE_SMART[original_ing]:
                SUBSTITUTE_SMART[original_ing].remove(sub_name)

            # 2. Add new entry to SUBSTITUTE_SMART
            if new_name not in SUBSTITUTE_SMART.setdefault(original_ing, []):
                SUBSTITUTE_SMART[original_ing].append(new_name)

            # 3. Update INGREDIENT_NUTRITION
            if sub_name in INGREDIENT_NUTRITION and sub_name != new_name:
                del INGREDIENT_NUTRITION[sub_name]
            INGREDIENT_NUTRITION[new_name] = new_kcal

            # 4. Update tracking maps
            if sub_name in custom_sub_map:
                del custom_sub_map[sub_name]
            if sub_name in all_custom_subs:
                all_custom_subs.remove(sub_name)
            custom_sub_map[new_name] = original_ing
            all_custom_subs.add(new_name)

            # 5. Update substitutes.json file
            current_subs_str = custom_substitutes_data.get(original_ing, "")
            existing_subs = [s.strip() for s in current_subs_str.split(',') if s.strip() and s.strip() != sub_name]
            existing_subs.append(new_name)
            custom_substitutes_data[original_ing] = ", ".join(sorted(existing_subs))
            save_json_file(SUBSTITUTE_FILE, custom_substitutes_data)

            update_status(f"'{sub_name}' updated to '{new_name}' with {new_kcal} kcal/100g.", color="green")

            # Refresh UI
            populate_custom_listbox()
            # Update dropdown to exclude only substitute ingredients, not originals
            all_substitute_ingredients_updated = set()
            for subs_list in SUBSTITUTE_SMART.values():
                all_substitute_ingredients_updated.update(subs_list)
            all_ing_keys = sorted([ing for ing in INGREDIENT_NUTRITION.keys() 
                                   if ing not in all_substitute_ingredients_updated])
            ing_to_replace_combo.config(values=all_ing_keys)
            refresh_results()

            edit_win.destroy()

        # Buttons
        btn_frame = tk.Frame(edit_win)
        btn_frame.pack(pady=15)
        tk.Button(btn_frame, text="Save", bg="#27ae60", fg="white", font=("Arial", 10, "bold"),
                  command=save_edit).pack(side="left", padx=5)
        tk.Button(btn_frame, text="Cancel", bg="#95a5a6", fg="white", font=("Arial", 10, "bold"),
                  command=edit_win.destroy).pack(side="left", padx=5)


    # ===========================
    #      Add Custom Substitute Logic
    # ===========================
    def add_custom_substitute():
        name = custom_name_entry.get().strip()
        ing_to_replace = ing_to_replace_var.get()
        if not name or not ing_to_replace:
            update_status("Incomplete: Name and Ingredient to replace are required.", color="red")
            return
        
        # Check if this substitute name is already used for ANY ingredient
        for original_ing, subs_list in SUBSTITUTE_SMART.items():
            if name in subs_list:
                if original_ing == ing_to_replace:
                    update_status(f"Duplicate Substitutes not allowed: '{name}' already exists as a substitute for '{ing_to_replace}'.", color="red")
                else:
                    update_status(f"Duplicate Substitutes not allowed: '{name}' is already a substitute for '{original_ing}'.", color="red")
                return
        
        # Check for conflict with existing base ingredients
        if name in INGREDIENT_NUTRITION and name not in all_custom_subs and name not in default_subs_set:
             update_status(f"Exists: '{name}' already exists as a base ingredient.", color="red")
             return
        
        try:
            cal_val = float(custom_cal_entry.get()) if custom_cal_entry.get().strip() else 0.0
        except:
            update_status("Invalid: Calories must be numeric.", color="red")
            return

        # 1. Update Nutrition DB
        INGREDIENT_NUTRITION[name] = cal_val
        
        # 2. Update Smart Substitutes list
        if name not in SUBSTITUTE_SMART.setdefault(ing_to_replace, []):
            SUBSTITUTE_SMART[ing_to_replace].append(name)
        
        # 3. Persist to substitutes.json 
        current_subs_str = custom_substitutes_data.get(ing_to_replace, "")
        existing_subs = set([s.strip() for s in current_subs_str.split(',') if s.strip()])
        existing_subs.add(name)
        custom_substitutes_data[ing_to_replace] = ", ".join(sorted(list(existing_subs)))
        save_json_file(SUBSTITUTE_FILE, custom_substitutes_data) 
        
        # 4. Update tracking maps (mark as custom)
        custom_sub_map[name] = ing_to_replace
        all_custom_subs.add(name)

        update_status(f"'{name}' added as substitute for '{ing_to_replace}' with {cal_val} kcal/100g.", color="green")
        
        custom_name_entry.delete(0, "end"); custom_cal_entry.delete(0, "end")
        ing_to_replace_var.set("")
        
        # Refresh UI components
        populate_custom_listbox()
        # Update dropdown to exclude only substitute ingredients, not originals
        all_substitute_ingredients_updated = set()
        for subs_list in SUBSTITUTE_SMART.values():
            all_substitute_ingredients_updated.update(subs_list)
        all_ing_keys = sorted([ing for ing in INGREDIENT_NUTRITION.keys() 
                               if ing not in all_substitute_ingredients_updated])
        ing_to_replace_combo.config(values=all_ing_keys)
        refresh_results()


    # ===========================
    #       Right Panel: Results Display
    # ===========================
    right_frame = tk.Frame(frame); right_frame.pack(side="right", fill="both", expand=True)

    tk.Label(right_frame, text="Substitution Results",
             font=("Arial", 14, "bold")).pack(pady=5)

    # Create scrollable frame for results
    result_container = tk.Frame(right_frame)
    result_container.pack(side="left", fill="both", expand=True)
    
    result_canvas = tk.Canvas(result_container)
    result_scrollbar = tk.Scrollbar(result_container, orient="vertical", command=result_canvas.yview)
    result_frame = tk.Frame(result_canvas)
    
    result_canvas.configure(yscrollcommand=result_scrollbar.set)
    result_scrollbar.pack(side="right", fill="y")
    result_canvas.pack(side="left", fill="both", expand=True)
    
    result_window = result_canvas.create_window((0, 0), window=result_frame, anchor="nw")
    
    # Bind mousewheel to canvas
    def on_result_mousewheel(event):
        result_canvas.yview_scroll(int(-1*(event.delta/120)), "units")
    
    def bind_result_mousewheel(event):
        result_canvas.bind_all("<MouseWheel>", on_result_mousewheel)
    
    def unbind_result_mousewheel(event):
        result_canvas.unbind_all("<MouseWheel>")
    
    result_canvas.bind("<Enter>", bind_result_mousewheel)
    result_canvas.bind("<Leave>", unbind_result_mousewheel)
    
    # Configure scroll region
    def configure_result_scroll(event=None):
        result_canvas.configure(scrollregion=result_canvas.bbox("all"))
        result_canvas.itemconfig(result_window, width=result_canvas.winfo_width())
    
    result_frame.bind("<Configure>", configure_result_scroll)
    result_canvas.bind("<Configure>", configure_result_scroll)

    # ===========================
    #      Display / Removal Logic
    # ===========================
    
    def remove_ingredient_group(original_ing):
        """Unchecks the ingredient checkbox, triggering refresh."""
        if original_ing in var_dict:
            var_dict[original_ing].set(False)
            
    def get_calorie_color(kcal):
        """Determines color coding based on calorie density."""
        if kcal == "Unknown" or kcal == "?":
            return "#34495e"
        try:
            kcal_float = float(kcal)
        except ValueError:
            return "#34495e"

        # Color grading logic
        if kcal_float < 100:
            return "#27ae60" # Green (Healthy/Low Cal)
        elif kcal_float < 300:
            return "#f39c12" # Yellow (Medium Cal)
        else:
            return "#e74c4c" # Red (High Cal)
        
    def refresh_results():
        """Updates the results panel based on selected ingredients."""
        # Clear current results
        for widget in result_frame.winfo_children():
            widget.destroy()

        selected = [k for k, v in var_dict.items() if v.get()]

        if not selected:
            tk.Label(result_frame, text="No selected items.",
                     font=("Arial", 12)).pack(anchor="w", pady=5)
            result_canvas.configure(scrollregion=result_canvas.bbox("all"))
            return

        for ing in selected:
            kcal = INGREDIENT_NUTRITION.get(ing, "?")
            
            # Frame for the original ingredient group
            ing_f = tk.Frame(result_frame, padx=5, pady=5, relief="groove", borderwidth=1, bg="#ecf0f1"); 
            ing_f.pack(anchor="w", fill="x", pady=5)
            
            # Row 1: Original ingredient title + Calorie + Remove button
            title_f = tk.Frame(ing_f, bg="#ecf0f1"); title_f.pack(fill="x")
            
            # Original ingredient name
            tk.Label(title_f, text=f"► {ing}",
                     font=("Arial", 13, "bold"), fg="#2c3e50", bg="#ecf0f1").pack(side="left")
                     
            # Calorie info (color-coded)
            tk.Label(title_f, text=f" ({kcal} kcal/100g)",
                     font=("Arial", 13, "bold"), fg=get_calorie_color(kcal), bg="#ecf0f1").pack(side="left")
            
            # Remove button
            tk.Button(title_f, text=" [ ❌ ] ", fg="red", font=("Arial", 10, "bold"), relief="flat", padx=5,
                     command=lambda org=ing: remove_ingredient_group(org)).pack(side="right", padx=5)

            
            # Subsequent rows: Substitutes list
            subs = SUBSTITUTE_SMART.get(ing, [])

            for sub in subs:
                sub_kcal = INGREDIENT_NUTRITION.get(sub, "Unknown")
                sub_f = tk.Frame(ing_f, bg="#ecf0f1"); sub_f.pack(anchor="w", padx=20)
                
                # Substitute name
                tk.Label(sub_f, text=f"• {sub}",
                         font=("Arial", 12), bg="#ecf0f1").pack(side="left")
                # Substitute calorie info (color-coded)
                tk.Label(sub_f, text=f" ({sub_kcal} kcal/100g)",
                         font=("Arial", 12), fg=get_calorie_color(sub_kcal), bg="#ecf0f1").pack(side="left")
                         
            tk.Label(ing_f, text="", bg="#ecf0f1").pack()  # Spacer line
        
        # Update scroll region after adding all widgets
        result_canvas.configure(scrollregion=result_canvas.bbox("all"))

    # Bind the refresh function to every checkbox state change
    for v in var_dict.values():
        v.trace_add("write", lambda *args: refresh_results())

    # Initial run
    refresh_results()
    
    # Exit button at the bottom
    tk.Button(win, text="Exit", bg="#c0392b", fg="white", font=("Arial",14,"bold"), width=50, height=1,
              command=lambda: win.destroy()).pack(pady=(10, 20))