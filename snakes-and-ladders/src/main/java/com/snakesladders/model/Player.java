package com.snakesladders.model;

/**
 * Represents a player in the Snakes & Ladders game.
 */
public class Player {
    private final String name;
    private int position;

    public Player(String name) {
        this.name = name;
        this.position = 0;
    }

    public String getName() { return name; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }

    @Override
    public String toString() {
        return String.format("Player{name='%s', position=%d}", name, position);
    }
}
