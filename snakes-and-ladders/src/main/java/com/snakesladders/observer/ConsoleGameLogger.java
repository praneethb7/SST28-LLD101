package com.snakesladders.observer;

import com.snakesladders.model.Player;

/**
 * Concrete observer that logs all game events to the console.
 * Decoupled from the game engine via the GameEventObserver interface.
 */
public class ConsoleGameLogger implements GameEventObserver {

    @Override
    public void onDiceRolled(Player player, int diceValue) {
        System.out.printf("  🎲  %s rolled a %d%n", player.getName(), diceValue);
    }

    @Override
    public void onPlayerMoved(Player player, int from, int to) {
        System.out.printf("  ➡️  %s moved from %d to %d%n", player.getName(), from, to);
    }

    @Override
    public void onSnakeBite(Player player, int from, int to) {
        System.out.printf("  🐍  Oh no! %s was bitten by a snake at %d and slid down to %d%n",
                player.getName(), from, to);
    }

    @Override
    public void onLadderClimb(Player player, int from, int to) {
        System.out.printf("  🪜  Lucky! %s climbed a ladder from %d to %d%n",
                player.getName(), from, to);
    }

    @Override
    public void onPlayerWon(Player player, int rank) {
        System.out.printf("%n  🏆  %s finished in position #%d!%n", player.getName(), rank);
    }

    @Override
    public void onPlayerEliminated(Player player) {
        System.out.printf("  ❌  %s has been eliminated (only one player left).%n", player.getName());
    }

    @Override
    public void onTurnSkipped(Player player, int diceValue, int currentPosition) {
        System.out.printf("  ⛔  %s rolled %d but stays at %d (move would go beyond the board).%n",
                player.getName(), diceValue, currentPosition);
    }
}
