package com.snakesladders.game;

import com.snakesladders.factory.DiceStrategyFactory;
import com.snakesladders.factory.DiceStrategyFactory.Difficulty;
import com.snakesladders.model.Board;
import com.snakesladders.model.Player;
import com.snakesladders.observer.ConsoleGameLogger;
import com.snakesladders.strategy.BoardSetupStrategy;
import com.snakesladders.strategy.DiceRollStrategy;
import com.snakesladders.strategy.RandomBoardSetupStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * GameSetup orchestrates building a GameEngine from raw user inputs.
 *
 * Separates object creation (wiring) from game execution (Single Responsibility).
 */
public class GameSetup {

    /**
     * Build and return a fully configured GameEngine ready to play.
     *
     * @param n              board side length (board is n×n)
     * @param playerCount    number of players
     * @param difficultyStr  "easy" or "hard"
     * @return configured GameEngine
     */
    public static GameEngine build(int n, int playerCount, String difficultyStr) {
        // 1. Parse difficulty and create dice strategy via factory
        Difficulty difficulty = Difficulty.from(difficultyStr);
        DiceRollStrategy diceStrategy = DiceStrategyFactory.create(difficulty);

        // 2. Create the board
        Board board = new Board(n);

        // 3. Create players
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= playerCount; i++) {
            players.add(new Player("Player-" + i));
        }

        // 4. Choose board setup strategy
        BoardSetupStrategy setupStrategy = new RandomBoardSetupStrategy();

        // 5. Wire everything into the engine
        GameEngine engine = new GameEngine(board, diceStrategy, setupStrategy, players, n);

        // 6. Attach console logger observer
        engine.addObserver(new ConsoleGameLogger());

        return engine;
    }

    private GameSetup() { /* utility class */ }
}
