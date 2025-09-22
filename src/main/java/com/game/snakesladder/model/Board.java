package com.game.snakesladder.model;

import java.util.*;

public class Board {
    private final int size; // N x N
    private final int lastIndex; // N*N - 1 (zero-based)
    private final Map<Integer, Snake> snakesByHead;
    private final Map<Integer, Ladder> laddersByStart;

    public Board(int size, List<Snake> snakes, List<Ladder> ladders) {
        if (size < 2) {
            throw new IllegalArgumentException("Board size must be >= 2");
        }
        this.size = size;
        this.lastIndex = size * size - 1;
        this.snakesByHead = new HashMap<>();
        this.laddersByStart = new HashMap<>();

        if (snakes != null) {
            for (Snake s : snakes) {
                validateInBounds(s.getTail());
                validateInBounds(s.getHead());
                if (snakesByHead.containsKey(s.getHead())) {
                    throw new IllegalArgumentException("Duplicate snake head at " + s.getHead());
                }
                snakesByHead.put(s.getHead(), s);
            }
        }
        if (ladders != null) {
            for (Ladder l : ladders) {
                validateInBounds(l.getStart());
                validateInBounds(l.getEnd());
                if (laddersByStart.containsKey(l.getStart())) {
                    throw new IllegalArgumentException("Duplicate ladder start at " + l.getStart());
                }
                laddersByStart.put(l.getStart(), l);
            }
        }
        // Ensure no snake head equals ladder start to avoid ambiguity
        for (Integer head : snakesByHead.keySet()) {
            if (laddersByStart.containsKey(head)) {
                throw new IllegalArgumentException("Snake head and ladder start overlap at " + head);
            }
        }
    }

    public int getSize() {
        return size;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    // Convert zero-based cell index to human-friendly 1-based label
    public int toHumanCellNumber(int zeroBasedIndex) {
        return zeroBasedIndex + 1;
    }

    public Optional<Snake> getSnakeAt(int index) {
        return Optional.ofNullable(snakesByHead.get(index));
    }

    public Optional<Ladder> getLadderAt(int index) {
        return Optional.ofNullable(laddersByStart.get(index));
    }

    private void validateInBounds(int index) {
        if (index < 0 || index > lastIndex) {
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }
    }
}



