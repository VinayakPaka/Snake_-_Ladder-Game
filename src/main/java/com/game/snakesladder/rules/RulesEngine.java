package com.game.snakesladder.rules;

import com.game.snakesladder.model.Board;
import com.game.snakesladder.model.Ladder;
import com.game.snakesladder.model.Snake;

import java.util.Optional;

public class RulesEngine {
    private final Board board;

    public RulesEngine(Board board) {
        this.board = board;
    }

    public TurnOutcome applyTurn(int startPosition, int[] rolls) {
        int position = startPosition;
        int consecutiveSixes = 0;
        StringBuilder message = new StringBuilder();

        for (int i = 0; i < rolls.length; i++) {
            int roll = rolls[i];

            if (roll == 6) {
                consecutiveSixes++;
            } else {
                consecutiveSixes = 0;
            }

            if (consecutiveSixes == 3) {
                message.append("Three sixes rolled. Turn revoked. ");
                return new TurnOutcome(startPosition, false, true, false, message.toString());
            }

            int tentative = position + roll;
            if (tentative > board.getLastIndex()) {
                message.append("Overshoot ignored (need exact). ");
                // do not move on overshoot
            } else {
                position = tentative;
                // check snake or ladder
                Optional<Snake> snake = board.getSnakeAt(position);
                if (snake.isPresent()) {
                    position = snake.get().getTail();
                    message.append("Bitten by snake to ").append(position).append(". ");
                } else {
                    Optional<Ladder> ladder = board.getLadderAt(position);
                    if (ladder.isPresent()) {
                        position = ladder.get().getEnd();
                        message.append("Climbed ladder to ").append(position).append(". ");
                    }
                }
            }

            if (position == board.getLastIndex()) {
                message.append("Reached last cell. ");
                return new TurnOutcome(position, false, false, true, message.toString());
            }
        }

        boolean extra = rolls.length > 0 && rolls[rolls.length - 1] == 6 && consecutiveSixes < 3;
        return new TurnOutcome(position, extra, false, false, message.toString());
    }
}



