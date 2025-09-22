package com.game.snakesladder;

import com.game.snakesladder.dice.StandardDie;
import com.game.snakesladder.dice.Die;
import com.game.snakesladder.difficulty.DifficultyStrategy;
import com.game.snakesladder.difficulty.EasyStrategy;
import com.game.snakesladder.difficulty.MediumStrategy;
import com.game.snakesladder.difficulty.HardStrategy;
import com.game.snakesladder.engine.GameEngine;
import com.game.snakesladder.factory.BoardFactory;
import com.game.snakesladder.model.Board;
import com.game.snakesladder.model.Player;
import com.game.snakesladder.render.ConsoleBoardRenderer;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Snakes & Ladders (Java CLI)\n");

        int size = readInt(scanner, "Enter board size N (e.g., 7 for N x N): ", 2, 50);
        DifficultyStrategy difficulty = readDifficulty(scanner);
        int numPlayers = readInt(scanner, "Enter number of players (>=2): ", 2, 6);
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Enter name for player " + i + ": ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                name = "Player" + i;
            }
            players.add(new Player(name));
        }

        BoardFactory boardFactory = new BoardFactory(difficulty);
        Board board = boardFactory.create(size);
        Die die = new StandardDie(6);
        GameEngine engine = new GameEngine(board, players, die, new ConsoleBoardRenderer());

        System.out.println("\nBoard ready: " + size + "x" + size);
        System.out.println("Last cell index: " + board.getLastIndex());
        System.out.println("Starting game. Good luck!\n");

        engine.playInteractive(scanner);
    }

    private static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                int val = Integer.parseInt(line.trim());
                if (val < min || val > max) {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                    continue;
                }
                return val;
            } catch (NumberFormatException ex) {
                System.out.println("Invalid number. Try again.") ;
            }
        }
    }

    private static DifficultyStrategy readDifficulty(Scanner scanner) {
        while (true) {
            System.out.print("Choose difficulty (easy/medium/hard): ");
            String diff = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            switch (diff) {
                case "easy":
                    return new EasyStrategy();
                case "medium":
                    return new MediumStrategy();
                case "hard":
                    return new HardStrategy();
                default:
                    System.out.println("Unknown difficulty. Try again.");
            }
        }
    }
}


