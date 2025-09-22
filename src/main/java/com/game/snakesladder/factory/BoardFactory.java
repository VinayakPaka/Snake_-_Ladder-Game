package com.game.snakesladder.factory;

import com.game.snakesladder.difficulty.DifficultyStrategy;
import com.game.snakesladder.model.Board;
import com.game.snakesladder.model.Ladder;
import com.game.snakesladder.model.Snake;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoardFactory {
    private final DifficultyStrategy difficultyStrategy;

    public BoardFactory(DifficultyStrategy difficultyStrategy) {
        this.difficultyStrategy = difficultyStrategy;
    }

    public Board create(int size) {
        int lastIndex = size * size - 1;
        // Generate until snakes and ladders do not overlap on the same cell
        // Avoid rare collisions from randomized strategies
        List<Snake> snakes;
        List<Ladder> ladders;
        int attempts = 0;
        do {
            snakes = difficultyStrategy.generateSnakes(size, lastIndex);
            ladders = difficultyStrategy.generateLadders(size, lastIndex);
            attempts++;
        } while (hasOverlap(snakes, ladders) && attempts < 20);

        // If still overlapping after attempts, filter ladders that clash with snake heads as a last resort
        if (hasOverlap(snakes, ladders)) {
            Set<Integer> snakeHeads = new HashSet<>();
            for (Snake s : snakes) snakeHeads.add(s.getHead());
            ladders.removeIf(l -> snakeHeads.contains(l.getStart()));
        }

        return new Board(size, snakes, ladders);
    }

    private boolean hasOverlap(List<Snake> snakes, List<Ladder> ladders) {
        Set<Integer> snakeHeads = new HashSet<>();
        for (Snake s : snakes) {
            snakeHeads.add(s.getHead());
        }
        for (Ladder l : ladders) {
            if (snakeHeads.contains(l.getStart())) return true;
        }
        return false;
    }
}



