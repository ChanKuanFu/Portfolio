# Module A: Recipe Organizer & Nutrition - Meal Tracker (formerly Weekly Log)
# Made by RAM

import tkinter as tk
from tkinter import messagebox, ttk, simpledialog
from datetime import datetime

from file_io import *
from .ui_helpers import open_window

# ============================= Meal Tracker UI =============================
def open_weekly_meal_log_window(parent):
    """Opens the interface for planning and tracking weekly meals."""
    recipes = load_json_file(RECIPE_FILE, [])
    recipes_names = [r.get("name", "Unnamed") for r in recipes]
    weekly = load_json_file(WEEKLY_LOG_FILE, {})
    
    # Determine the current week key
    today = datetime.now()
    week_key = today.strftime("%Y-W%W")
    days = ["Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"]
    
    # Initialize the current week's data if needed
    if week_key not in weekly:
        weekly[week_key] = {d: [] for d in days}
        
    win = open_window(parent, "Meal Tracker", "900x700")
    
    # Header and Week Selector
    topf = tk.Frame(win); topf.pack(pady=10, fill="x")
    tk.Label(topf, text=f"Meal Tracker - Week {week_key}", font=("Arial",16,"bold")).pack(side="left", padx=12)
    
    wk_list = list(weekly.keys())
    week_var = tk.StringVar(value=week_key)
    # Dropdown to switch between logged weeks
    ttk.Combobox(topf, textvariable=week_var, values=wk_list, state="readonly", width=18).pack(side="left", padx=8)
    
    def switch_week():
        """Switches the displayed log data to the selected week."""
        nonlocal week_key
        wk = week_var.get()
        if wk and wk in weekly:
            week_key = wk; refresh_day_listboxes()
            
    tk.Button(topf, text="Switch Week", command=switch_week).pack(side="left", padx=6)
    
    # Meal Columns
    midf = tk.Frame(win); midf.pack(fill="both", expand=True, padx=10, pady=6)
    listboxes = {}
    
    for i, day in enumerate(days):
        col = tk.Frame(midf, bd=1, relief="groove"); col.grid(row=0, column=i, padx=6, sticky="nsew"); midf.grid_columnconfigure(i, weight=1)
        tk.Label(col, text=day, font=("Arial",12,"bold")).pack(pady=6)
        
        lb = tk.Listbox(col, width=20, height=15); lb.pack(padx=6, pady=6, fill="both", expand=True); listboxes[day] = lb
        
        btnf = tk.Frame(col); btnf.pack(pady=4)
        
        def add_for_day(d=day):
            """Prompts user to add a recipe to a specific day."""
            if not recipes_names:
                messagebox.showwarning("No recipes", "No recipes available. Add recipes first."); return
            # Create a temporary selection window
            sel_win = tk.Toplevel(win)
            sel_win.title(f"Select recipe for {d}")
            sel_win.geometry("300x120")

            tk.Label(sel_win, text=f"Select recipe for {d}:").pack(pady=6)

            selected_var = tk.StringVar()
            combo = ttk.Combobox(sel_win, textvariable=selected_var, values=recipes_names, state="readonly")
            combo.pack(pady=4)

            def confirm():
                choice = selected_var.get()
                if not choice:
                    messagebox.showwarning("No selection", "Please select a recipe.")
                    return
                # Add to weekly log (inside confirm)
                weekly.setdefault(week_key, {dd: [] for dd in days})
                weekly[week_key].setdefault(d, []).append(choice)
                save_json_file(WEEKLY_LOG_FILE, weekly)
                refresh_day_listboxes()
                sel_win.destroy()

            tk.Button(sel_win, text="Add", command=confirm).pack(pady=6)
            
        def remove_for_day(d=day):
            """Removes the selected meal from a specific day."""
            sel = listboxes[d].curselection()
            if not sel: return
            idx = sel[0]
            if messagebox.askyesno("Remove", f"Remove selected item from {d}?"):
                weekly[week_key][d].pop(idx); save_json_file(WEEKLY_LOG_FILE, weekly); refresh_day_listboxes()
                
        tk.Button(btnf, text="+", width=3, command=lambda d=day: add_for_day(d)).pack(side="left", padx=3)
        tk.Button(btnf, text="-", width=3, command=lambda d=day: remove_for_day(d)).pack(side="left", padx=3)
        
    # Weekly Summary Area
    right_panel = tk.Frame(win); right_panel.pack(fill="x", pady=8)
    summary_label = tk.Label(right_panel, text="", font=("Courier New",12), justify="left"); summary_label.pack(padx=12, pady=6, anchor="w")
    
    def refresh_day_listboxes():
        """Updates all listboxes and recalculates the weekly calorie summary."""
        wk_list2 = sorted(list(weekly.keys()))
        try:
            # Update the Combobox options
            week_combo = topf.winfo_children()[1]
            week_combo.config(values=wk_list2)
        except Exception:
            pass
            
        # Populate daily listboxes
        for d in days:
            lb = listboxes[d]; lb.delete(0, "end")
            for item in weekly.get(week_key, {}).get(d, []):
                lb.insert("end", item)
                
        # Calculate calorie summary
        recipes_map = {r.get("name"): r for r in load_json_file(RECIPE_FILE, [])}
        weekly_total = 0.0; txt = ""
        for d in days:
            day_sum = 0.0
            for nm in weekly.get(week_key, {}).get(d, []):
                r = recipes_map.get(nm)
                if r:
                    day_sum += sum(it.get("calories", 0) for it in r.get("ingredients", []))
            weekly_total += day_sum; txt += f"{d:12}: {int(day_sum)} kcal\n"
            
        txt += "\nWeekly Total: {} kcal".format(int(weekly_total)); 
        summary_label.config(text=txt)
        
    refresh_day_listboxes()