package com.snakesladders;

import com.snakesladders.game.GameEngine;
import com.snakesladders.game.GameSetup;
import com.snakesladders.model.Player;

import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the Snakes & Ladders application.
 * Reads user inputs and drives the game loop.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║     SNAKES  &  LADDERS  GAME     ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.println();

        // --- Input: board size ---
        int n = 0;
        while (n < 2) {
            System.out.print("Enter board size n (board will be n×n, minimum 2): ");
            try {
                n = Integer.parseInt(scanner.nextLine().trim());
                if (n < 2) System.out.println("  ⚠  Board size must be at least 2.");
            } catch (NumberFormatException e) {
                System.out.println("  ⚠  Please enter a valid integer.");
            }
        }

        // --- Input: number of players ---
        int playerCount = 0;
        while (playerCount < 2) {
            System.out.print("Enter number of players (minimum 2): ");
            try {
                playerCount = Integer.parseInt(scanner.nextLine().trim());
                if (playerCount < 2) System.out.println("  ⚠  Need at least 2 players.");
            } catch (NumberFormatException e) {
                System.out.println("  ⚠  Please enter a valid integer.");
            }
        }

        // --- Input: difficulty ---
        String difficulty = "";
        while (!difficulty.equals("easy") && !difficulty.equals("hard")) {
            System.out.print("Enter difficulty (easy/hard): ");
            difficulty = scanner.nextLine().trim().toLowerCase();
            if (!difficulty.equals("easy") && !difficulty.equals("hard")) {
                System.out.println("  ⚠  Please type 'easy' or 'hard'.");
            }
        }

        System.out.println();
        System.out.printf("Starting a %d×%d board game with %d players on %s difficulty.%n",
                n, n, playerCount, difficulty);
        System.out.printf("Board has cells 1 – %d, with %d snakes and %d ladders placed randomly.%n",
                n * n, n, n);
        System.out.println("──────────────────────────────────────────────────────────");

        // --- Build the engine ---
        GameEngine engine = GameSetup.build(n, playerCount, difficulty);

        // --- Game loop ---
        int turn = 1;
        while (!engine.isGameOver()) {
            System.out.printf("%n[ Turn %d ]%n", turn++);
            engine.playTurn();
        }

        // --- Results ---
        System.out.println();
        System.out.println("══════════════════════════════════");
        System.out.println("           FINAL RESULTS          ");
        System.out.println("══════════════════════════════════");
        List<Player> finished = engine.getFinishedPlayers();
        if (finished.isEmpty()) {
            System.out.println("No player finished — game ended with fewer than 2 active players.");
        } else {
            for (int i = 0; i < finished.size(); i++) {
                System.out.printf("  #%d  %s%n", i + 1, finished.get(i).getName());
            }
        }
        System.out.println("══════════════════════════════════");
    }
}
