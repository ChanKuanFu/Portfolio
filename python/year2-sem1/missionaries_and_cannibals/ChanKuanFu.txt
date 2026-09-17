def is_valid(state):
    m_left, c_left, b_pos = state
    m_right, c_right = 3 - m_left, 3 - c_left

    if m_left < 0 or c_left < 0 or m_right < 0 or c_right < 0: return False
    if m_left > 0 and c_left > m_left: return False
    if m_right > 0 and c_right > m_right: return False
    return True

def get_successors(state):
    m_left, c_left, b_pos = state
    successors = []
    moves = [(1, 0), (0, 1), (1, 1), (2, 0), (0, 2)]

    for dm, dc in moves:
        if b_pos == 1:
            new_state = (m_left - dm, c_left - dc, 0)
        else:
            new_state = (m_left + dm, c_left + dc, 1)

        if is_valid(new_state):
            successors.append(new_state)
    return successors

def is_goal(state):
    return state == (0, 0, 0)

def dls(current_state, path, limit, explored, nodes_expanded):
    if is_goal(current_state): return path, nodes_expanded
    if limit == 0: return "CUTOFF", nodes_expanded
    
    cutoff_occurred = False
    for successor in get_successors(current_state):
        if successor not in explored:
            explored.add(successor)
            nodes_expanded[0] += 1
            result, _ = dls(successor, path + [successor], limit - 1, explored, nodes_expanded)
            explored.remove(successor) 
            if result == "CUTOFF": cutoff_occurred = True
            elif result is not None: return result, nodes_expanded
                
    return "CUTOFF" if cutoff_occurred else None, nodes_expanded

def ids(start_state):
    depth_limit, nodes_expanded = 0, [0]
    while True:
        explored = set([start_state])
        result, _ = dls(start_state, [start_state], depth_limit, explored, nodes_expanded)
        if result != "CUTOFF" and result is not None: return result, nodes_expanded[0]
        depth_limit += 1
        if depth_limit > 30: return None, nodes_expanded[0] # Prevent infinite loop

def print_step_by_step(path):
    print(f"    [Step 0] Initial State: {path[0]}")
    for i in range(1, len(path)):
        prev, curr = path[i-1], path[i]
        m_moved, c_moved = abs(prev[0] - curr[0]), abs(prev[1] - curr[1])
        direction = "Right Bank" if prev[2] == 1 else "Left Bank "
        print(f"    [Step {i}] Moved {m_moved} Missionary & {c_moved} Cannibal to {direction} -> State: {curr}")

def run_experiment():
    print("="*70 + "\n  ITERATIVE DEEPENING SEARCH (IDS)\n" + "="*70)
    
    while True:
        try:
            print("\n--- Enter Manual Test Data ---")
            m = int(input("Enter number of Missionaries on Left Bank (0-3): "))
            c = int(input("Enter number of Cannibals on Left Bank (0-3): "))
            b = int(input("Enter Boat position (1 for Left, 0 for Right): "))
            
            if m < 0 or m > 3 or c < 0 or c > 3 or b not in [0, 1]:
                print("[!] Invalid input. Please follow the range rules.")
                continue
                
            start_state = (m, c, b)
            
            if not is_valid(start_state):
                print("[!] Warning: This start state is unsafe/invalid.")
                
            print(f"\nEvaluating Start: {start_state} -> Goal: (0, 0, 0)")
            path, nodes = ids(start_state)
            
            if path:
                print(f"  -> Success! Nodes Expanded: {nodes} | Total Moves: {len(path)-1}")
                print_step_by_step(path)
            else:
                print(f"  -> NO SOLUTION FOUND. Nodes Expanded: {nodes}")
                
            print("\nOptions:")
            print("1 - Continue to test another data")
            print("2 - Exit the program")
            choice = input("Enter your choice (1 or 2): ")
            
            if choice == '2':
                print("Exiting program...")
                break
            elif choice != '1':
                print("Invalid choice detected. Exiting program by default...")
                break
                
        except ValueError:
            print("[!] Invalid input format. Please enter integers only.")

if __name__ == "__main__":
    run_experiment()








