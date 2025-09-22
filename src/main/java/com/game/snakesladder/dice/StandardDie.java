package com.game.snakesladder.dice;

import java.util.Random;

public class StandardDie implements Die {
    private final Random random;
    private final int faces;

    public StandardDie() {
        this(6);
    }

    public StandardDie(int faces) {
        if (faces < 2) {
            throw new IllegalArgumentException("Die must have at least 2 faces");
        }
        this.faces = faces;
        this.random = new Random();
    }

    @Override
    public int roll() {
        return random.nextInt(faces) + 1;
    }
}



