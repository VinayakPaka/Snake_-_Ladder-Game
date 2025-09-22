package com.game.snakesladder.factory;

import com.game.snakesladder.difficulty.DifficultyStrategy;
import com.game.snakesladder.model.Board;

public class BoardFactory {
    private final DifficultyStrategy difficultyStrategy;

    public BoardFactory(DifficultyStrategy difficultyStrategy) {
        this.difficultyStrategy = difficultyStrategy;
    }

    public Board create(int size) {
        int lastIndex = size * size - 1;
        return new Board(
                size,
                difficultyStrategy.generateSnakes(size, lastIndex),
                difficultyStrategy.generateLadders(size, lastIndex)
        );
    }
}



