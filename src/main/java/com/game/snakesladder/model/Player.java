package com.game.snakesladder.model;

import java.util.Objects;

public class Player {
    private final String name;
    private int position; // cell index, 0 is start

    public Player(String name) {
        this.name = Objects.requireNonNull(name);
        this.position = 0;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}



