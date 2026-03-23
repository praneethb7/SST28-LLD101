package com.snakesladders.strategy;

import java.util.Random;

/**
 * Easy difficulty dice strategy.
 * Rolls a fair six-sided die: random value from 1 to 6.
 */
public class EasyDiceStrategy implements DiceRollStrategy {
    private final Random random;

    public EasyDiceStrategy() {
        this.random = new Random();
    }

    @Override
    public int roll() {
        return random.nextInt(6) + 1;
    }
}
