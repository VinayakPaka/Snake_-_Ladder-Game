package com.game.snakesladder.difficulty;

public class HardStrategy extends RandomDifficultyBase {
    @Override
    protected int getSnakeCount(int boardSize) {
        return Math.max(5, boardSize + boardSize / 2);
    }

    @Override
    protected int getLadderCount(int boardSize) {
        return Math.max(2, boardSize / 3);
    }
}


