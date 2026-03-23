# Snakes & Ladders — LLD Implementation

Plain Java (no Maven/Gradle). Implements all SOLID principles and behavioral design patterns.

## Project Structure
```
src/main/java/com/snakesladders/
├── Main.java                          # Entry point (reads user input, runs game loop)
├── model/
│   ├── Board.java                     # n×n board, holds Cell array
│   ├── Cell.java                      # Single cell, optionally holds a Connector
│   ├── Connector.java                 # Snake or Ladder (start → end)
│   ├── ConnectorType.java             # Enum: SNAKE / LADDER
│   └── Player.java                    # Player name + position
├── strategy/
│   ├── DiceRollStrategy.java          # <<interface>> Strategy Pattern
│   ├── EasyDiceStrategy.java          # Fair 1-6 random roll
│   ├── HardDiceStrategy.java          # min(roll1, roll2) — skews low
│   ├── BoardSetupStrategy.java        # <<interface>> Strategy Pattern
│   └── RandomBoardSetupStrategy.java  # Places n snakes + n ladders randomly
├── observer/
│   ├── GameEventObserver.java         # <<interface>> Observer Pattern
│   └── ConsoleGameLogger.java         # Logs all events to console
├── factory/
│   └── DiceStrategyFactory.java       # Factory Pattern — creates dice by difficulty
└── game/
    ├── GameEngine.java                # Core game loop, applies all rules
    └── GameSetup.java                 # Wires all components together (SRP)
```

## Design Patterns Used
| Pattern   | Where |
|-----------|-------|
| **Strategy**  | `DiceRollStrategy` (easy/hard dice), `BoardSetupStrategy` (random placement) |
| **Observer**  | `GameEventObserver` + `ConsoleGameLogger` — game events decoupled from I/O |
| **Factory**   | `DiceStrategyFactory` — creates correct dice strategy by difficulty |

## SOLID Principles
- **S** — Each class has one responsibility (Board ≠ GameEngine ≠ Logger ≠ Setup)
- **O** — New dice strategies or loggers can be added without touching existing code
- **L** — `EasyDiceStrategy` and `HardDiceStrategy` are fully substitutable
- **I** — `GameEventObserver` exposes only event methods; `DiceRollStrategy` only `roll()`
- **D** — `GameEngine` depends on interfaces, not concrete dice/setup classes

## How to Run
```bash
# Compile
find src -name "*.java" > sources.txt
javac -d out @sources.txt

# Run
java -cp out com.snakesladders.Main
```

### Input Prompts
```
Enter board size n (board will be n×n, minimum 2): 10
Enter number of players (minimum 2): 3
Enter difficulty (easy/hard): easy
```

## Rules Implemented
- Board: cells 1 to n² 
- n snakes + n ladders placed randomly (no cycles, no overlap)
- Snake: head > tail (player moves down)
- Ladder: base < top (player moves up)
- If dice roll would exceed last cell → player stays (no move)
- Game ends when fewer than 2 players remain active
