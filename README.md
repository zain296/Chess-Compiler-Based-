# Chess-Compiler-Based
A console-based two-player chess game built in Java.(Excluding some special moves)

# Console Chess Game (Java)

A two-player chess game that runs entirely in the console/terminal, built in Java as a personal learning project. No GUI, no external libraries — just core Java fundamentals.

## Features

- Full 8x8 chess board rendered in the console using simple text codes (`wK`, `wQ`, `bP`, etc.) instead of special characters, so it displays correctly in any terminal.
- Legal move validation for every piece type:
  - Pawn (including double-step first move and diagonal captures)
  - Rook
  - Knight
  - Bishop
  - Queen
  - King
- Turn-based play (White moves first, then Black).
- Check detection — warns a player if their move would leave their own king in check.
- Checkmate detection — automatically ends the game and announces the winner.
- Optional move history — moves can be saved to a text file (`GameRecord.txt`) and replayed/viewed at the end of the game.
- Built-in instructions menu for players who are new to chess.
- "Play again" loop so multiple games can be played in one run.

## How to Run

1. Make sure you have **Java JDK 17+** installed (the code uses Java text blocks and switch expressions).
2. Clone or download this repository.
3. Compile the program:
   ```bash
   javac Main.java
   ```
4. Run it:
   ```bash
   java Main
   ```

## How to Play

- The board uses standard chess coordinates: rows `1–8` and columns `a–h`.
- To make a move, enter the starting row, starting column, ending row, and ending column, separated by spaces.
  - Example: `2 a 3 b`
  - This means: pick up the piece at row 2, column a, and move it to row 3, column b.
- White always moves first.
- If a move would put your own king in check, the game will reject it and ask you to try again.
- If you're in check, the game will warn you. If there's no legal move left, it's checkmate and the game ends.

## Piece Notation

| Code | Piece  | Color |
|------|--------|-------|
| wK   | King   | White |
| wQ   | Queen  | White |
| wR   | Rook   | White |
| wB   | Bishop | White |
| wN   | Knight | White |
| wP   | Pawn   | White |
| bK   | King   | Black |
| bQ   | Queen  | Black |
| bR   | Rook   | Black |
| bB   | Bishop | Black |
| bN   | Knight | Black |
| bP   | Pawn   | Black |

## Project Structure

```
.
├── Main.java        # Entire game logic (board, rules, console I/O)
└── GameRecord.txt   # (Optional, auto-generated) saved move history
```

## What I Learned

This project was built to practice and apply core Java concepts, including:
- 2D arrays for board representation
- Methods and modular function design
- Conditional logic for implementing real-world rules (chess move legality)
- File I/O (reading/writing game history with `FileReader`/`FileWriter`)
- Exception handling for invalid user input
- Loops and game-state management (turns, check, checkmate)

## Future Improvements

- Add castling and en passant rules
- Add pawn promotion
- Add a simple GUI (JavaFX or Swing)
- Add an AI opponent for single-player mode
- Add move notation history in standard algebraic notation (e.g. `Nf3`, `exd5`)

## License

This project is open source and free to use for learning purposes.
