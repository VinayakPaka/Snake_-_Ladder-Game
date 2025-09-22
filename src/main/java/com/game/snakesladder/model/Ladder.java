package com.game.snakesladder.model;

public class Ladder {
    private final int start; // lower index
    private final int end;   // higher index

    public Ladder(int start, int end) {
        if (end <= start) {
            throw new IllegalArgumentException("Ladder end must be above start");
        }
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}



