package com.snakesladders.model;

/**
 * Represents a connector on the board — either a Snake or a Ladder.
 *
 * Snake  : start (head) > end (tail)  → player moves DOWN
 * Ladder : start (base) < end (top)   → player moves UP
 *
 * Invariant is enforced by the factory/builder that creates connectors.
 */
public class Connector {
    private final ConnectorType type;
    private final int start; // head of snake / base of ladder
    private final int end;   // tail of snake / top of ladder

    public Connector(ConnectorType type, int start, int end) {
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public ConnectorType getType() {
        return type;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public String toString() {
        if (type == ConnectorType.SNAKE) {
            return String.format("Snake[head=%d, tail=%d]", start, end);
        } else {
            return String.format("Ladder[base=%d, top=%d]", start, end);
        }
    }
}
