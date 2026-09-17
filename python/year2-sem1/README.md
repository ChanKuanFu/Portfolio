# Missionaries and Cannibals - Iterative Deepening Search - Year 2 Sem 1 (Python)

A Python implementation of the classic Missionaries and Cannibals problem,
solved using Iterative Deepening Search (IDS) — an uninformed search
strategy that combines the space efficiency of depth-first search with
the completeness of breadth-first search.

> Note: This was part of a group assignment where different members
> implemented different search strategies. This file contains only the
> IDS algorithm, which I was individually responsible for designing and
> writing.

## Problem

Three missionaries and three cannibals must cross a river using a boat
that can carry at most two people at a time. If cannibals ever outnumber
missionaries on either bank, the missionaries are in danger. The program
finds a valid sequence of moves to get everyone across safely.

## Features

- **State Validity Checking** — `is_valid()` ensures cannibals never
  outnumber missionaries on either bank for any candidate state
- **Successor Generation** — `get_successors()` generates all valid next
  states from the five possible boat moves (1 missionary, 1 cannibal, one
  of each, 2 missionaries, or 2 cannibals)
- **Iterative Deepening Search (IDS)** — `ids()` repeatedly runs a
  depth-limited search (`dls()`) with increasing depth limits until a
  solution is found, avoiding the memory cost of full breadth-first search
- **Cycle Detection** — Tracks explored states per path (with backtracking
  on the `explored` set) to avoid revisiting the same state within a
  search branch
- **Interactive Testing** — Lets the user manually enter a starting state
  (missionaries, cannibals, boat position) and see the step-by-step
  solution, with input validation and a loop to test multiple states
- **Node Expansion Tracking** — Reports how many nodes were expanded to
  find the solution, useful for comparing search efficiency

## Project Structure

```
python/year2-sem1/
└── missionaries_cannibals_ids.py   (State logic, IDS/DLS search, interactive test runner)
```

## How to Run

```bash
python missionaries_cannibals_ids.py
```

Follow the prompts to enter a starting state (missionaries on the left
bank, cannibals on the left bank, boat position), then choose to test
another state or exit.

## What I Learned

Implementing IDS deepened my understanding of search strategies in AI —
particularly the trade-off between memory usage and completeness compared
to breadth-first search. Working alongside a teammate who implemented A*
search on the same problem also gave me a clearer picture of how informed
search (using a heuristic) can be more efficient than uninformed search
when a good heuristic is available.
