package com.game.snakesladder.difficulty;

public class EasyStrategy extends RandomDifficultyBase {
    @Override
    protected int getSnakeCount(int boardSize) {
        return Math.max(2, boardSize / 2);
    }

    @Override
    protected int getLadderCount(int boardSize) {
        return Math.max(3, boardSize);
    }
}


