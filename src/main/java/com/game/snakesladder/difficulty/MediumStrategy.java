package com.game.snakesladder.difficulty;

public class MediumStrategy extends RandomDifficultyBase {
    @Override
    protected int getSnakeCount(int boardSize) {
        return Math.max(3, boardSize);
    }

    @Override
    protected int getLadderCount(int boardSize) {
        return Math.max(3, boardSize / 2);
    }
}


