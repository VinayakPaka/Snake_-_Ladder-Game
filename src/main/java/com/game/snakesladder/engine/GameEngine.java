package com.game.snakesladder.engine;

import com.game.snakesladder.dice.Die;
import com.game.snakesladder.model.Board;
import com.game.snakesladder.model.Player;
import com.game.snakesladder.rules.RulesEngine;
import com.game.snakesladder.rules.TurnOutcome;
import com.game.snakesladder.render.BoardRenderer;

import java.util.*;

public class GameEngine {
    private final Board board;
    private final List<Player> players;
    private final Die die;
    private final RulesEngine rulesEngine;
    private final BoardRenderer renderer;

    public GameEngine(Board board, List<Player> players, Die die) {
        if (players == null || players.size() < 2) {
            throw new IllegalArgumentException("At least two players required");
        }
        this.board = board;
        this.players = new ArrayList<>(players);
        this.die = die;
        this.rulesEngine = new RulesEngine(board);
        this.renderer = null;
    }

    public GameEngine(Board board, List<Player> players, Die die, BoardRenderer renderer) {
        if (players == null || players.size() < 2) {
            throw new IllegalArgumentException("At least two players required");
        }
        this.board = board;
        this.players = new ArrayList<>(players);
        this.die = die;
        this.rulesEngine = new RulesEngine(board);
        this.renderer = renderer;
    }

    public void playInteractive(Scanner scanner) {
        boolean won = false;
        int currentIdx = 0;
        while (!won) {
            Player current = players.get(currentIdx);
            if (renderer != null) {
                System.out.println("Current board:");
                renderer.render(board, players);
            }
            System.out.println("\n" + current.getName() + "'s turn. Position: " + current.getPosition());
            System.out.print("Press Enter to roll...");
            scanner.nextLine();

            // Roll logic with extra six and revoke handled by rules engine via batched rolls
            List<Integer> rolls = new ArrayList<>();
            boolean rolling = true;
            int sixesInRow = 0;
            while (rolling) {
                int r = die.roll();
                System.out.println("Rolled: " + r);
                rolls.add(r);
                if (r == 6) {
                    sixesInRow++;
                    if (sixesInRow == 3) {
                        // Stop rolling, let rules engine revoke
                        rolling = false;
                    } else {
                        System.out.println("Got a 6! Extra roll.");
                        // continue loop for extra roll
                    }
                } else {
                    rolling = false;
                }
            }

            TurnOutcome outcome = rulesEngine.applyTurn(current.getPosition(), rolls.stream().mapToInt(Integer::intValue).toArray());
            if (outcome.turnRevoked) {
                System.out.println(outcome.message);
                // no movement
            } else {
                current.setPosition(outcome.finalPosition);
                System.out.println(outcome.message + "New position: " + current.getPosition());
                // Kill rule
                handleKillIfAny(current);
                if (outcome.won) {
                    System.out.println("\nWinner: " + current.getName());
                    won = true;
                    break;
                }
                if (outcome.getsExtraTurn) {
                    System.out.println(current.getName() + " gets an extra turn!");
                    // do not advance index
                    continue;
                }
            }
            if (renderer != null) {
                System.out.println("Board after move:");
                renderer.render(board, players);
            }
            currentIdx = (currentIdx + 1) % players.size();
        }
    }

    private void handleKillIfAny(Player actor) {
        for (Player p : players) {
            if (p == actor) continue;
            if (p.getPosition() == actor.getPosition() && actor.getPosition() != 0) {
                System.out.println(actor.getName() + " killed " + p.getName() + "! Sending to start.");
                p.setPosition(0);
            }
        }
    }
}


