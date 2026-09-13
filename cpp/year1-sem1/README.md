# Train Ticket Booking System - Year 1 Sem 1 (C++)

A console-based train ticket booking system that lets customers select routes, seat classes, and payment methods to book train tickets, with a password-protected admin dashboard for income reporting.

## Features

- **Ticket Booking** — Select departure, destination, seat class (Economy/Luxury/Business), and number of tickets, with real-time seat availability checked against a shared inventory
- **Multiple Payment Methods** — Cash, E-Wallet (with simulated OTP verification), and Credit/Debit Card, each with its own input flow
- **Booking Confirmation** — Displays a full ticket summary including route, date, time, price breakdown, service fee, and change due
- **Admin Dashboard** — Password-protected access for staff to view a full income report across all 11 routes and 3 seat classes
- **Input Validation** — Retry loops guard against invalid dates, seat classes, payment amounts, OTP entries, and PIN length

## Project Structure

cpp/year1-sem1/
└── train_ticket_booking_system.cpp   (Entry point — menu, booking flow, admin dashboard)

## How to Run

g++ train_ticket_booking_system.cpp -o ticket_system
./ticket_system

## Known Limitations

- Admin credentials are hardcoded in the source for demo purposes; a production system would store hashed credentials securely rather than in plaintext.
- Seat inventory is randomly generated each run rather than persisted, so availability resets every session.

## What I Learned

This was one of my earliest projects, and it helped me build confidence with core C++ fundamentals — using 2D arrays to manage prices and seat inventory across multiple routes and classes, nested loops and switch-case logic for handling branching user flows, and structuring input validation loops to keep the program robust against invalid input.
