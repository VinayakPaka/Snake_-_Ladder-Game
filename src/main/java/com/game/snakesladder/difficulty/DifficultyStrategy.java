package com.game.snakesladder.difficulty;

import com.game.snakesladder.model.Ladder;
import com.game.snakesladder.model.Snake;

import java.util.List;

public interface DifficultyStrategy {
    List<Snake> generateSnakes(int boardSize, int lastIndex);
    List<Ladder> generateLadders(int boardSize, int lastIndex);
}



