package com.snakesladders.observer;

import com.snakesladders.model.Player;

/**
 * Observer interface for game events — Observer Pattern.
 * Any component interested in game state changes implements this interface.
 */
public interface GameEventObserver {
    void onDiceRolled(Player player, int diceValue);
    void onPlayerMoved(Player player, int from, int to);
    void onSnakeBite(Player player, int from, int to);
    void onLadderClimb(Player player, int from, int to);
    void onPlayerWon(Player player, int rank);
    void onPlayerEliminated(Player player);
    void onTurnSkipped(Player player, int diceValue, int currentPosition);
}
