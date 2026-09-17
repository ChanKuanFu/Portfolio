# Missionaries and Cannibals - Iterative Deepening Search (Python)

A Python implementation of the classic Missionaries and Cannibals problem, solved using Iterative Deepening Search (IDS) — an uninformed search strategy that combines the space efficiency of depth-first search with the completeness of breadth-first search.

> Note: This was part of a group assignment where different members implemented different search strategies. This file contains only the IDS algorithm, which I was individually responsible for designing and writing.

## Problem

Three missionaries and three cannibals must cross a river using a boat that can carry at most two people at a time. If cannibals ever outnumber missionaries on either bank, the missionaries are in danger. The program finds a valid sequence of moves to get everyone across safely.

## Features

- **State Validity Checking** — Ensures cannibals never outnumber missionaries on either bank
- **Iterative Deepening Search (IDS)** — Repeatedly runs depth-limited search with increasing depth limits until a solution is found, avoiding the memory cost of full breadth-first search
- **Cycle Detection** — Tracks explored states per path to avoid revisiting the same state within a search branch
- **Interactive Testing** — Lets the user manually enter a starting state (missionaries, cannibals, boat position) and see the step-by-step solution
- **Node Expansion Tracking** — Reports how many nodes were expanded to find the solution, useful for comparing search efficiency

## How to Run

```
python missionaries_cannibals_ids.py
```

Follow the prompts to enter a starting state, then choose to test another state or exit.

## What I Learned

Implementing IDS deepened my understanding of search strategies in AI — particularly the trade-off between memory usage and completeness. Working alongside a teammate who implemented A* search on the same problem also gave me a clearer picture of how informed search (using a heuristic) can be more efficient than uninformed search when a good heuristic is available.
