package com.snakesladders.strategy;

import com.snakesladders.model.Board;

/**
 * Strategy interface for placing snakes and ladders on the board.
 * Different difficulty levels can use different placement strategies.
 */
public interface BoardSetupStrategy {
    /**
     * Place n snakes and n ladders randomly on the given board.
     * @param board the board to set up
     * @param n     number of snakes and number of ladders to place
     */
    void setup(Board board, int n);
}
