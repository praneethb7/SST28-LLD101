package com.snakesladders.strategy;

/**
 * Strategy interface for dice rolling.
 * Allows different dice behaviours (e.g., easy/hard difficulty) to be swapped
 * without changing the game logic — Strategy Pattern.
 */
public interface DiceRollStrategy {
    /**
     * Roll the dice and return the result.
     * @return value rolled (1-6 for a standard die)
     */
    int roll();
}
