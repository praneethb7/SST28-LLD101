package com.snakesladders.strategy;

import com.snakesladders.model.Board;
import com.snakesladders.model.Connector;
import com.snakesladders.model.ConnectorType;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Places n snakes and n ladders randomly on the board.
 *
 * Rules enforced:
 *  - Snake  : head > tail  (player moves down)
 *  - Ladder : base < top   (player moves up)
 *  - No two connectors share the same start cell
 *  - No connector starts or ends on cell 1 or the last cell
 *  - No cycles (a connector's end must not be another connector's start)
 */
public class RandomBoardSetupStrategy implements BoardSetupStrategy {
    private final Random random;

    public RandomBoardSetupStrategy() {
        this.random = new Random();
    }

    @Override
    public void setup(Board board, int n) {
        int total = board.getTotalCells();
        // Track occupied start positions to avoid overlap
        Set<Integer> occupiedStarts = new HashSet<>();
        // Track connector ends to prevent cycles
        Set<Integer> connectorEnds = new HashSet<>();

        // Place n snakes
        int snakesPlaced = 0;
        while (snakesPlaced < n) {
            // Head must be > 1 and < last cell
            int head = randomBetween(2, total - 1);
            // Tail must be strictly less than head and >= 1
            int tail = randomBetween(1, head - 1);

            if (isValidPlacement(head, tail, occupiedStarts, connectorEnds, total)) {
                board.placeConnector(new Connector(ConnectorType.SNAKE, head, tail));
                occupiedStarts.add(head);
                connectorEnds.add(tail);
                snakesPlaced++;
            }
        }

        // Place n ladders
        int laddersPlaced = 0;
        while (laddersPlaced < n) {
            // Base must be >= 1 and < last cell
            int base = randomBetween(1, total - 1);
            // Top must be strictly greater than base and <= last cell
            int top = randomBetween(base + 1, total);

            if (isValidPlacement(base, top, occupiedStarts, connectorEnds, total)) {
                board.placeConnector(new Connector(ConnectorType.LADDER, base, top));
                occupiedStarts.add(base);
                connectorEnds.add(top);
                laddersPlaced++;
            }
        }
    }

    /**
     * Validates that a new connector (start→end) can be placed:
     *  1. start not already occupied
     *  2. start is not the end of another connector (no chaining cycles)
     *  3. end is not already a connector start (no chaining cycles)
     *  4. neither start nor end is cell 1 or last cell (game boundary cells)
     */
    private boolean isValidPlacement(int start, int end,
                                     Set<Integer> occupiedStarts,
                                     Set<Integer> connectorEnds,
                                     int total) {
        if (occupiedStarts.contains(start)) return false;
        if (connectorEnds.contains(start)) return false;  // cycle: end of another → this start
        if (occupiedStarts.contains(end)) return false;   // cycle: this end → start of another
        if (start == 1 || start == total) return false;
        if (end == 1 || end == total) return false;
        return true;
    }

    private int randomBetween(int min, int max) {
        if (min > max) return min;
        return random.nextInt(max - min + 1) + min;
    }
}
