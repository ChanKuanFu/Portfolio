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

7. **No array bounds checking** — Cart-related arrays had a fixed size 
   of 30 entries with no limit enforced on user input loops, risking 
   memory corruption if exceeded.

**Takeaway:** This project deepened my understanding of low-level memory 
management, register usage, and the importance of rigorous input 
validation — issues that are easy to overlook without the type safety 
and bounds checking found in higher-level languages.
