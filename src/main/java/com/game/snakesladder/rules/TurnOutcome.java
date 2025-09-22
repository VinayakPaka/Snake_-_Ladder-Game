package com.game.snakesladder.rules;

public class TurnOutcome {
    public final int finalPosition;
    public final boolean getsExtraTurn;
    public final boolean turnRevoked;
    public final boolean won;
    public final String message;

    public TurnOutcome(int finalPosition, boolean getsExtraTurn, boolean turnRevoked, boolean won, String message) {
        this.finalPosition = finalPosition;
        this.getsExtraTurn = getsExtraTurn;
        this.turnRevoked = turnRevoked;
        this.won = won;
        this.message = message;
    }
}



