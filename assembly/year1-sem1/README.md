# Console Based Food Delivery Ordering System - Year 1 Sem 1 (Assembly)

A console-based food delivery ordering system written in x86 Assembly
(8086, DOS interrupts), letting users register/log in, browse a food and
drinks menu, manage a cart, choose a delivery option, pay, and receive a
printed receipt — all through DOS-level screen positioning and interrupt
calls.

## Features

- **User Authentication** — Sign up with a username and password, then log
  in with character-by-character comparison against the stored credentials
  before accessing the ordering menu
- **Food & Drinks Menu** — Browse a 9-item menu (5 food items, 4 drinks)
  laid out in a fixed-position DOS text-mode screen
- **Cart Management** — Add multiple items with quantities (1–5 per item),
  delete the last added item, or continue adding until done via a
  Yes/No/Delete prompt loop
- **Delivery Options** — Choose between Standard (RM5.00) and Priority
  (RM7.00) delivery, added to the running total
- **Payment Processing** — Enter a payment amount, validated digit-by-digit
  and checked against the total due, with change calculated and displayed
- **Receipt Generation** — Displays a formatted order confirmation screen
  with the delivery address, username, and an ASCII-art receipt design,
  built entirely from DOS interrupt calls and manual cursor positioning

## Project Structure

```
assembly/year1-sem1/
└── main.asm   (Entry point — sign up/log in, menu, cart, delivery, payment, receipt)
```

## How to Run

This program targets 16-bit DOS (8086 real mode) and requires an
assembler such as TASM or MASM, run inside a DOS emulator like DOSBox.

```
TASM main.asm
TLINK main.obj
main.exe
```

## Known Issues / Lessons Learned

While reviewing this project after completion, I identified several bugs
that reflect common pitfalls in low-level assembly programming:

1. **Missing hex suffix** — `INT 21` was written instead of `INT 21H` in
   several branches, causing an unintended interrupt call instead of the
   intended DOS string output function.

2. **Incomplete length validation** — Password/confirmation comparisons
   only checked up to the length of the first input, allowing mismatched
   lengths with matching prefixes to pass as valid.

3. **Incorrect index register in a loop** — A digit-validation loop
   incremented `DI` while reading from `SI`, causing it to repeatedly
   check the same character instead of advancing through the input.

4. **Dead code path** — A payment recalculation block (`PAID:`) was
   unreachable, as both preceding branches already jumped elsewhere.

5. **Byte/word type mismatch** — Used a byte-sized multiplier (`MUL TEN`)
   on a word-sized value, silently truncating results above 255 instead
   of using the word-sized equivalent already defined in the data section.

6. **Insufficient overflow handling** — Total price was accumulated in a
   single byte, risking silent overflow when totals exceeded 255.

7. **No array bounds checking** — Cart-related arrays had a fixed size of
   30 entries with no limit enforced on user input loops, risking memory
   corruption if exceeded.

**Takeaway:** This project deepened my understanding of low-level memory
management, register usage, and the importance of rigorous input
validation — issues that are easy to overlook without the type safety
and bounds checking found in higher-level languages.

## What I Learned

Building a full ordering system in raw Assembly gave me a much deeper
appreciation for what higher-level languages handle automatically —
memory allocation, type safety, and bounds checking. Working directly
with DOS interrupts, registers, and manual string comparison loops taught
me to think carefully about program state at the byte level, and to plan
screen layout and control flow explicitly rather than relying on any
framework to manage it for me.
