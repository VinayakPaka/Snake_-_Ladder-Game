package com.game.snakesladder.render;

import com.game.snakesladder.model.Board;
import com.game.snakesladder.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ConsoleBoardRenderer implements BoardRenderer {
    @Override
    public void render(Board board, List<Player> players) {
        int n = board.getSize();
        int last = board.getLastIndex();

        // Build a map of cell to players' initials
        List<String> cellLabels = new ArrayList<>(last + 1);
        Map<Integer, List<Player>> playersByCell = new HashMap<>();
        for (int i = 0; i <= last; i++) {
            cellLabels.add(String.format("%02d", i));
        }
        for (Player p : players) {
            int pos = p.getPosition();
            playersByCell.computeIfAbsent(pos, k -> new ArrayList<>()).add(p);
        }
        for (Map.Entry<Integer, List<Player>> e : playersByCell.entrySet()) {
            int cell = e.getKey();
            List<Player> onCell = e.getValue();
            if (onCell.size() == 1) {
                Player p = onCell.get(0);
                String initial = p.getName().isEmpty() ? "?" : p.getName().substring(0, 1).toUpperCase();
                String base = cellLabels.get(cell);
                cellLabels.set(cell, initial + base.substring(1));
            } else {
                int count = onCell.size();
                String countStr = count < 10 ? ("." + count) : "**"; // .N for <=9, ** for 10+
                cellLabels.set(cell, countStr);
            }
        }

        // Print rows in alternating order (serpentine), top row shows highest indices
        System.out.println();
        for (int row = n - 1; row >= 0; row--) {
            StringBuilder sb = new StringBuilder();
            if ((n - 1 - row) % 2 == 0) {
                // left to right
                for (int col = 0; col < n; col++) {
                    int idx = row * n + col;
                    sb.append("[" ).append(cellLabels.get(idx)).append("] ");
                }
            } else {
                // right to left
                for (int col = n - 1; col >= 0; col--) {
                    int idx = row * n + col;
                    sb.append("[" ).append(cellLabels.get(idx)).append("] ");
                }
            }
            System.out.println(sb.toString());
        }
        // Legend with exact player positions
        StringBuilder legend = new StringBuilder("Players: ");
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            legend.append(p.getName()).append("@").append(p.getPosition());
            if (i < players.size() - 1) legend.append(" | ");
        }
        System.out.println(legend.toString());
        System.out.println();
    }
}


