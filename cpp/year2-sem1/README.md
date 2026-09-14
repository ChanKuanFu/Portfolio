# Smart Library Management System - Year 2 Sem 1 (C++)

A menu-driven library management system handling members, books, reservations, loans, and fines.

> Note: This project was completed primarily by me, covering system design, all four modules, and core logic implementation.

## Features

- **Member Management** — Add, view, search, update, and delete members across Student/Staff/Public categories, each with different borrowing limits and loan periods
- **Book Catalogue Management** — Add, view, search, update, and delete books, with per-copy tracking (not just total count)
- **Reservation Management** — FIFO reservation queue with borrow-priority enforcement, preventing members from skipping the queue
- **Borrowing & Returning** — Borrow/return books with automatic overdue fine calculation (capped per loan), duplicate-loan prevention, and simulated day advancement for testing
- **Fine Calculation & Reporting** — View/pay fines, plus reports for overdue books, book popularity, and total outstanding fines across all members

## Project Structure

```
cpp/year2-sem1/
└── library_system.cpp   (Entry point — all four modules: members, catalogue, reservations, borrowing/fines)
```

## How to Run

```
g++ library_system.cpp -o library_system
./library_system
```

## What I Learned

Building this system helped me practice structuring a larger, multi-module C++ program — managing shared state across members, books, and reservations, designing clean function boundaries between modules, and handling edge cases like enforcing reservation queue priority, preventing duplicate loans, and correctly capping fines per loan rather than per member.
