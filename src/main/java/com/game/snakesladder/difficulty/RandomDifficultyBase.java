package com.game.snakesladder.difficulty;

import com.game.snakesladder.model.Ladder;
import com.game.snakesladder.model.Snake;

import java.util.*;

abstract class RandomDifficultyBase implements DifficultyStrategy {
    protected abstract int getSnakeCount(int boardSize);
    protected abstract int getLadderCount(int boardSize);

    private final Random random = new Random();

    @Override
    public List<Snake> generateSnakes(int boardSize, int lastIndex) {
        int count = getSnakeCount(boardSize);
        Set<Integer> usedHeads = new HashSet<>();
        List<Snake> snakes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int head;
            int tail;
            // ensure head not at last cell and tail not at 0, and reasonable distance
            do {
                head = 1 + random.nextInt(lastIndex - 1);
                tail = random.nextInt(head);
            } while (head <= boardSize || head == lastIndex || tail == 0 || head - tail < Math.max(2, boardSize / 2) || usedHeads.contains(head));
            usedHeads.add(head);
            snakes.add(new Snake(head, tail));
        }
        return snakes;
    }

    @Override
    public List<Ladder> generateLadders(int boardSize, int lastIndex) {
        int count = getLadderCount(boardSize);
        Set<Integer> usedStarts = new HashSet<>();
        List<Ladder> ladders = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int start;
            int end;
            do {
                start = random.nextInt(lastIndex);
                end = start + 1 + random.nextInt(lastIndex - start);
            } while (start == 0 || end == lastIndex || end - start < Math.max(2, boardSize / 2) || usedStarts.contains(start));
            usedStarts.add(start);
            ladders.add(new Ladder(start, end));
        }
        return ladders;
    }
}



