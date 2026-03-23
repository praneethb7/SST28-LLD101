package com.snakesladders.model;

/**
 * Represents the n×n Snakes & Ladders board.
 * Cells are 1-indexed from 1 to n^2.
 */
public class Board {
    private final int size;
    private final Cell[] cells;

    public Board(int size) {
        this.size = size;
        int total = size * size;
        this.cells = new Cell[total + 1];
        for (int i = 1; i <= total; i++) {
            cells[i] = new Cell(i);
        }
    }

    public int getSize() { return size; }
    public int getTotalCells() { return size * size; }

    public Cell getCell(int position) {
        if (position < 1 || position > getTotalCells())
            throw new IllegalArgumentException("Position out of range: " + position);
        return cells[position];
    }

    public void placeConnector(Connector connector) {
        cells[connector.getStart()].setConnector(connector);
    }
}
