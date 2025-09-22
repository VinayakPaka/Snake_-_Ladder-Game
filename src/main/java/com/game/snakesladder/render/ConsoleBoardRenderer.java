package com.game.snakesladder.render;

import com.game.snakesladder.model.Board;
import com.game.snakesladder.model.Player;
import com.game.snakesladder.model.Snake;
import com.game.snakesladder.model.Ladder;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConsoleBoardRenderer implements BoardRenderer {
    // Use Unicode escapes to avoid platform encoding issues
    private static final String SNAKE_EMOJI = "\uD83D\uDC0D"; // 🐍 snake head
    private static final String LADDER_EMOJI = "\u2B06"; // ⬆ ladder start (arrow up as ladder marker)
    // Colorful player tokens (emoji-safe, deterministic selection)
    private static final String[] PLAYER_TOKENS = new String[] {
            "\uD83D\uDD34", // 🔴 red circle
            "\uD83D\uDD35", // 🔵 blue circle
            "\u26AA",       // ⚪ white circle
            "\u26AB",       // ⚫ black circle
            "\uD83D\uDD36", // 🔶 large orange diamond
            "\uD83D\uDD37", // 🔷 large blue diamond
            "\uD83D\uDD38", // 🔸 small orange diamond
            "\uD83D\uDD39"  // 🔹 small blue diamond
    };
    private static final String[] DIGIT_TOKENS = new String[] {
            "\u0031\uFE0F\u20E3", // 1️⃣
            "\u0032\uFE0F\u20E3", // 2️⃣
            "\u0033\uFE0F\u20E3", // 3️⃣
            "\u0034\uFE0F\u20E3", // 4️⃣
            "\u0035\uFE0F\u20E3", // 5️⃣
            "\u0036\uFE0F\u20E3", // 6️⃣
            "\u0037\uFE0F\u20E3", // 7️⃣
            "\u0038\uFE0F\u20E3", // 8️⃣
            "\u0039\uFE0F\u20E3", // 9️⃣
            "\uD83D\uDD1F"  // 🔟
    };
    private static final String[] COLOR_DOTS = new String[] {
            "\uD83D\uDD34", // 🔴
            "\uD83D\uDD35", // 🔵
            "\u26AA",       // ⚪
            "\u26AB",       // ⚫
            "\uD83D\uDD36", // 🔶
            "\uD83D\uDD37", // 🔷
            "\uD83D\uDD38", // 🔸
            "\uD83D\uDD39"  // 🔹
    };
    private static final int CELL_WIDTH = 3;

    @Override
    public void render(Board board, List<Player> players) {
        int n = board.getSize();
        int last = board.getLastIndex();

        // Assign unique player symbols from palette in order
        Map<Player, String> playerSymbol = new HashMap<>();
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            String symbol;
            if (i < PLAYER_TOKENS.length) {
                symbol = PLAYER_TOKENS[i];
            } else if (i - PLAYER_TOKENS.length < DIGIT_TOKENS.length) {
                symbol = DIGIT_TOKENS[i - PLAYER_TOKENS.length];
            } else {
                symbol = "P" + (i + 1);
            }
            playerSymbol.put(p, symbol);
        }

        // Build a map of cell to players on that cell
        Map<Integer, List<Player>> playersByCell = new HashMap<>();
        for (Player p : players) {
            int pos = p.getPosition();
            playersByCell.computeIfAbsent(pos, k -> new ArrayList<>()).add(p);
        }

        // Mark snakes (heads) and ladders (starts)
        boolean[] isSnakeHead = new boolean[last + 1];
        boolean[] isLadderStart = new boolean[last + 1];
        List<Snake> snakes = new ArrayList<>();
        List<Ladder> ladders = new ArrayList<>();
        for (int i = 0; i <= last; i++) {
            Optional<Snake> s = board.getSnakeAt(i);
            if (s.isPresent()) {
                isSnakeHead[i] = true;
                snakes.add(s.get());
            }
            Optional<Ladder> l = board.getLadderAt(i);
            if (l.isPresent()) {
                isLadderStart[i] = true;
                ladders.add(l.get());
            }
        }

        // Print rows top to bottom, human-friendly serpentine numbering like classic boards
        System.out.println();
        for (int displayRow = n - 1; displayRow >= 0; displayRow--) {
            int row = displayRow; // 0 is bottom row, n-1 is top row
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < n; c++) {
                int effectiveCol = (row % 2 == 0) ? c : (n - 1 - c);
                int idx = row * n + effectiveCol;
                String label = computeLabel(idx, row, effectiveCol, n, playersByCell, playerSymbol, isSnakeHead, isLadderStart);
                sb.append(padCenter(label, CELL_WIDTH));
                if (c < n - 1) sb.append(' ');
            }
            System.out.println(sb.toString());
        }
        // Legend with exact positions and symbols
        StringBuilder legendPlayers = new StringBuilder("Players: ");
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            legendPlayers.append(playerSymbol.get(p)).append("=").append(p.getName())
                    .append("@").append(p.getPosition());
            if (i < players.size() - 1) legendPlayers.append(" | ");
        }
        System.out.println(legendPlayers.toString());

        // Snakes and ladders legend
        if (!snakes.isEmpty()) {
            StringBuilder sLegend = new StringBuilder(SNAKE_EMOJI + " (head->tail): ");
            for (int i = 0; i < snakes.size(); i++) {
                Snake s = snakes.get(i);
                sLegend.append(s.getHead()).append("->").append(s.getTail());
                if (i < snakes.size() - 1) sLegend.append(", ");
            }
            System.out.println(sLegend.toString());
        }
        if (!ladders.isEmpty()) {
            StringBuilder lLegend = new StringBuilder(LADDER_EMOJI + " (start->end): ");
            for (int i = 0; i < ladders.size(); i++) {
                Ladder l = ladders.get(i);
                lLegend.append(l.getStart()).append("->").append(l.getEnd());
                if (i < ladders.size() - 1) lLegend.append(", ");
            }
            System.out.println(lLegend.toString());
        }
        System.out.println();
    }

    private String computeLabel(
            int index,
            int row,
            int col,
            int n,
            Map<Integer, List<Player>> playersByCell,
            Map<Player, String> playerSymbol,
            boolean[] isSnakeHead,
            boolean[] isLadderStart
    ) {
        List<Player> onCell = playersByCell.get(index);
        if (onCell != null && !onCell.isEmpty()) {
            if (onCell.size() == 1) {
                return playerSymbol.get(onCell.get(0));
            } else {
                int count = onCell.size();
                return count < 10 ? ("✳" + count) : "**";
            }
        }
        if (isSnakeHead[index]) return SNAKE_EMOJI;
        if (isLadderStart[index]) return LADDER_EMOJI;
        // Human-friendly serpentine numbering (1-based like traditional board)
        int human;
        if (row % 2 == 0) {
            human = row * n + col + 1;
        } else {
            human = (row + 1) * n - col;
        }
        return String.valueOf(human);
    }
    
    private static String padCenter(String s, int width) {
        if (s == null) s = "";
        // Strip any line breaks just in case
        s = s.replace('\n', ' ').replace('\r', ' ');
        int len = s.length();
        if (len >= width) return s.substring(0, Math.min(len, width));
        int totalPad = width - len;
        int padStart = totalPad / 2;
        int padEnd = totalPad - padStart;
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < padStart; i++) b.append(' ');
        b.append(s);
        for (int i = 0; i < padEnd; i++) b.append(' ');
        return b.toString();
    }
}


