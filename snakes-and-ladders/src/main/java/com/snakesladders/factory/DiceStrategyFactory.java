package com.snakesladders.factory;

import com.snakesladders.strategy.DiceRollStrategy;
import com.snakesladders.strategy.EasyDiceStrategy;
import com.snakesladders.strategy.HardDiceStrategy;

/**
 * Factory that creates the appropriate DiceRollStrategy based on difficulty.
 * Factory Pattern — centralises creation logic and decouples clients from
 * concrete strategy classes.
 */
public class DiceStrategyFactory {

    public enum Difficulty {
        EASY, HARD;

        public static Difficulty from(String input) {
            switch (input.trim().toLowerCase()) {
                case "easy": return EASY;
                case "hard": return HARD;
                default:
                    throw new IllegalArgumentException(
                        "Unknown difficulty: '" + input + "'. Use 'easy' or 'hard'.");
            }
        }
    }

    public static DiceRollStrategy create(Difficulty difficulty) {
        switch (difficulty) {
            case EASY: return new EasyDiceStrategy();
            case HARD: return new HardDiceStrategy();
            default:
                throw new IllegalArgumentException("Unhandled difficulty: " + difficulty);
        }
    }

    private DiceStrategyFactory() { /* utility class */ }
}
