# Module A: UI Helper Functions
# Made by RAM

import tkinter as tk
from tkinter import ttk
import re
from file_io import INGREDIENT_NUTRITION

def open_window(parent, title, size):
    """
    Creates and returns a new top-level window (Toplevel), attempting to center it on the screen.
    
    Args:
        parent (tk.Tk or tk.Toplevel): The parent widget.
        title (str): The window title.
        size (str): The initial window size in "WIDTHxHEIGHT" format (e.g., "700x650").
    """
    win = tk.Toplevel(parent)
    win.title(title)
    
    # 1. Parse the size string (e.g., "600x800")
    try:
        width_str, height_str = size.split('x')
        win_width = int(width_str)
        win_height = int(height_str)
    except ValueError:
        # If size format is bad, just apply it and return
        win.geometry(size)
        return win

    # 2. Force Tkinter to calculate screen dimensions immediately
    # This is crucial for getting accurate screen width/height before positioning
    win.update_idletasks() 

    # 3. Get screen width and height
    screen_width = win.winfo_screenwidth()
    screen_height = win.winfo_screenheight()

    # 4. Calculate the centered position
    x = (screen_width // 2) - (win_width // 2)
    y = (screen_height // 2) - (win_height // 2)

    # 5. Set the geometry: WIDTHxHEIGHT+X+Y
    win.geometry(f'{win_width}x{win_height}+{x}+{y}')
    
    return win

def make_scrollable_frame(parent, canvas_width=None):
    """Creates a Canvas and Scrollbar setup containing a Frame."""
    canvas = tk.Canvas(parent, width=canvas_width) if canvas_width else tk.Canvas(parent)
    scr = ttk.Scrollbar(parent, orient="vertical", command=canvas.yview)
    frame = tk.Frame(canvas)
    
    # Configure the inner frame's size to update the scroll region
    frame.bind("<Configure>", lambda e: canvas.configure(scrollregion=canvas.bbox("all")))
    canvas.create_window((0,0), window=frame, anchor="nw")
    canvas.configure(yscrollcommand=scr.set)
    
    canvas.pack(side="left", fill="both", expand=True)
    scr.pack(side="left", fill="y")
    return frame, canvas

# ============================= Handle Legacy Ingredient Format =============================
def parse_old_ingredients(ing_field):
    """Converts old string-based or list-of-string ingredient formats to the new dict format."""
    items = []
    if not ing_field:
        return items
    # Handle nested list/string inputs recursively
    if isinstance(ing_field, list):
        for it in ing_field:
            if isinstance(it, dict):
                items.append(it)
            else:
                items.extend(parse_old_ingredients(it))
        return items
        
    lines = str(ing_field).splitlines()
    for line in lines:
        line = line.strip()
        if not line:
            continue
            
        # Try to parse 'XXX g'
        m = re.search(r"(\d+)\s*g", line, re.I)
        if m:
            grams = int(m.group(1))
            name = re.sub(r"\d+\s*g", "", line, flags=re.I).strip(", -")
        else:
            # Fallback to just the first number found
            m2 = re.search(r"(\d+)", line)
            if m2:
                grams = int(m2.group(1))
                name = re.sub(r"(\d+)", "", line).strip(", -")
            else:
                # Default to 100g if no quantity found
                grams = 100
                name = line
                
        cal100 = INGREDIENT_NUTRITION.get(name, 0)
        total_cal = round(cal100 * grams / 100, 1)
        items.append({"name": name, "grams": grams, "calories": total_cal})
    return items

    #Attaches the Physical Scrolling wheel to the canvas/area so it scrolls along with the scrollbar
def bind_mousewheel(widget, canvas):
    toplevel = widget.winfo_toplevel()
    
    def on_enter(event):
        toplevel.bind("<MouseWheel>", lambda e: canvas.yview_scroll(int(-1*(e.delta/120)), "units"))
        toplevel.bind("<Button-4>", lambda e: canvas.yview_scroll(-1, "units"))  
        toplevel.bind("<Button-5>", lambda e: canvas.yview_scroll(1, "units"))   

    def on_leave(event):
        toplevel.unbind("<MouseWheel>")
        toplevel.unbind("<Button-4>")
        toplevel.unbind("<Button-5>")

    widget.bind("<Enter>", on_enter)
    widget.bind("<Leave>", on_leave)