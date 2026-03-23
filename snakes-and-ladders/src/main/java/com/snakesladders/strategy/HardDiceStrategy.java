package com.snakesladders.strategy;

import java.util.Random;

/**
 * Hard difficulty dice strategy.
 * Rolls two dice and takes the minimum, making higher values harder to get.
 * This effectively skews rolls toward lower numbers, making the game harder
 * (more snake bites, less ladder climbing).
 */
public class HardDiceStrategy implements DiceRollStrategy {
    private final Random random;

    public HardDiceStrategy() {
        this.random = new Random();
    }

    @Override
    public int roll() {
        int roll1 = random.nextInt(6) + 1;
        int roll2 = random.nextInt(6) + 1;
        // Take minimum — biases toward small moves, making the game harder
        return Math.min(roll1, roll2);
    }
}
