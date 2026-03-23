package com.snakesladders.model;

/**
 * Represents a single cell on the Snakes & Ladders board.
 * A cell may optionally have a connector (snake or ladder) attached to it.
 */
public class Cell {
    private final int position;
    private Connector connector; // null if no snake/ladder at this cell

    public Cell(int position) {
        this.position = position;
        this.connector = null;
    }

    public int getPosition() {
        return position;
    }

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public boolean hasConnector() {
        return connector != null;
    }
}
