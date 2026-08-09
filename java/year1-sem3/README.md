# Smart Library System - Year 1 Sem 3 (Java)

A Java-based library management system that lets students, faculty, public
members, and librarians search the catalog, borrow and return resources,
manage fines, and generate reports — all through a console menu, with data
persisted to disk between sessions.

## Features

- **User Roles & Authentication** — Separate accounts for Student, Faculty,
  Public Member, and Librarian, each with login/registration and
  role-specific permissions
- **Resource Catalog** — Manage Books, Journals, and Digital Resources
  through a shared `Resource` model, with add/update/remove/view support
  for librarians
- **Borrowing & Returns** — Borrow, return, and reserve resources, with
  full transaction history tracking
- **Fine Management** — Automatic overdue fine calculation, with members
  able to view and pay outstanding fines
- **Reporting** — Librarian reports for most-borrowed resources, fine
  revenue, overdue history, and active users
- **Data Persistence** — Library state (users, resources, transactions,
  fines) is saved and reloaded using Java object serialization, so data
  survives between runs

## Project Structure

```
smartlibrarysystem/
├── SmartLibrarySystem.java   # Entry point — console menus and program flow
├── Library.java              # Core logic: catalog, borrowing, fines, reports, persistence
├── User.java                 # Base user account (auth, contact info)
├── Librarian.java            # Librarian role — extends User
├── Resource.java             # Abstract base for all library resources
├── Book.java                 # Book resource — extends Resource
├── Journal.java               # Journal resource — extends Resource
├── DigitalResource.java       # Digital resource — extends Resource
├── Transaction.java          # Borrow/return transaction record
└── Fine.java                  # Overdue fine record
```

## How to Run

```bash
# Compile
javac smartlibrarysystem/*.java -d build

# Run
java -cp build smartlibrarysystem.SmartLibrarySystem
```

On first run, the system seeds a few default accounts so you can log
straight in and explore the menus (see the console output for their
IDs/passwords). Data is written to disk automatically, so subsequent runs
pick up where the previous session left off.

## What I Learned

Building this project helped me practice object-oriented design in Java —
using inheritance and an abstract base class (`Resource`) to model related
resource types, and applying `Serializable` for straightforward object
persistence without a database. It also gave me practice structuring a
larger, multi-class console application around role-based menus and
business logic like fine calculation and reporting.
