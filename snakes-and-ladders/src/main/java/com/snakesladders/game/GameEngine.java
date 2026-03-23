package com.snakesladders.game;

import com.snakesladders.model.*;
import com.snakesladders.observer.GameEventObserver;
import com.snakesladders.strategy.BoardSetupStrategy;
import com.snakesladders.strategy.DiceRollStrategy;

import java.util.*;

/**
 * Core game engine for Snakes & Ladders.
 *
 * Responsibilities:
 *  - Maintain the list of active players (turn order via a Queue)
 *  - Drive the game loop (one turn per call to {@link #playTurn()})
 *  - Apply movement rules (dice roll, snake, ladder, boundary check)
 *  - Notify registered observers of every game event (Observer Pattern)
 *  - Declare the game over when ≤ 1 player remains
 *
 * The engine is intentionally free of I/O — all output goes through observers.
 */
public class GameEngine {

    private final Board board;
    private final DiceRollStrategy diceStrategy;
    private final Queue<Player> activePlayers;   // players still in the game
    private final List<GameEventObserver> observers;
    private final List<Player> finishedPlayers;  // ordered list of finishers
    private boolean gameOver;

    public GameEngine(Board board,
                      DiceRollStrategy diceStrategy,
                      BoardSetupStrategy setupStrategy,
                      List<Player> players,
                      int n) {
        this.board = board;
        this.diceStrategy = diceStrategy;
        this.activePlayers = new LinkedList<>(players);
        this.observers = new ArrayList<>();
        this.finishedPlayers = new ArrayList<>();
        this.gameOver = false;

        // Delegate board population to the strategy (Strategy Pattern)
        setupStrategy.setup(board, n);
    }

    /** Register an observer to receive game events. */
    public void addObserver(GameEventObserver observer) {
        observers.add(observer);
    }

    /** @return true if the game has ended */
    public boolean isGameOver() {
        return gameOver;
    }

    /** @return an unmodifiable view of players who finished, in finishing order */
    public List<Player> getFinishedPlayers() {
        return Collections.unmodifiableList(finishedPlayers);
    }

    /**
     * Execute one full turn for the next player in the queue.
     * After the call, internal state reflects the result of that turn.
     */
    public void playTurn() {
        if (gameOver) return;

        Player current = activePlayers.poll();
        if (current == null) return;

        int diceValue = diceStrategy.roll();
        notifyDiceRolled(current, diceValue);

        int from = current.getPosition();
        int proposedPosition = from + diceValue;
        int lastCell = board.getTotalCells();

        // Rule: if proposed move goes beyond last cell, player does not move
        if (proposedPosition > lastCell) {
            notifyTurnSkipped(current, diceValue, from);
            activePlayers.offer(current); // back of queue
            checkGameOver();
            return;
        }

        // Move the player
        current.setPosition(proposedPosition);
        notifyPlayerMoved(current, from, proposedPosition);

        // Apply connector if present at new position
        Cell cell = board.getCell(proposedPosition);
        if (cell.hasConnector()) {
            Connector connector = cell.getConnector();
            int connectorEnd = connector.getEnd();
            if (connector.getType() == ConnectorType.SNAKE) {
                notifySnakeBite(current, proposedPosition, connectorEnd);
            } else {
                notifyLadderClimb(current, proposedPosition, connectorEnd);
            }
            current.setPosition(connectorEnd);
        }

        // Check for win condition
        if (current.getPosition() == lastCell) {
            finishedPlayers.add(current);
            notifyPlayerWon(current, finishedPlayers.size());
            checkGameOver();
            return;
        }

        activePlayers.offer(current); // back of queue for next turn
        checkGameOver();
    }

    /**
     * Game ends when fewer than 2 players remain active.
     * The last remaining player (if any) is eliminated/declared last.
     */
    private void checkGameOver() {
        if (activePlayers.size() < 2) {
            // If exactly 1 player remains and game has active players, eliminate them
            if (activePlayers.size() == 1) {
                Player last = activePlayers.poll();
                notifyPlayerEliminated(last);
            }
            gameOver = true;
        }
    }

    // -----------------------------------------------------------------------
    // Observer notification helpers
    // -----------------------------------------------------------------------

    private void notifyDiceRolled(Player player, int value) {
        for (GameEventObserver obs : observers) obs.onDiceRolled(player, value);
    }

    private void notifyPlayerMoved(Player player, int from, int to) {
        for (GameEventObserver obs : observers) obs.onPlayerMoved(player, from, to);
    }

    private void notifySnakeBite(Player player, int from, int to) {
        for (GameEventObserver obs : observers) obs.onSnakeBite(player, from, to);
    }

    private void notifyLadderClimb(Player player, int from, int to) {
        for (GameEventObserver obs : observers) obs.onLadderClimb(player, from, to);
    }

    private void notifyPlayerWon(Player player, int rank) {
        for (GameEventObserver obs : observers) obs.onPlayerWon(player, rank);
    }

    private void notifyPlayerEliminated(Player player) {
        for (GameEventObserver obs : observers) obs.onPlayerEliminated(player);
    }

    private void notifyTurnSkipped(Player player, int diceValue, int position) {
        for (GameEventObserver obs : observers) obs.onTurnSkipped(player, diceValue, position);
    }
}
