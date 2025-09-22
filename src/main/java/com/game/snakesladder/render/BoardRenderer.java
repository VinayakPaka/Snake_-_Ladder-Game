package com.game.snakesladder.render;

import com.game.snakesladder.model.Board;
import com.game.snakesladder.model.Player;

import java.util.List;

public interface BoardRenderer {
    void render(Board board, List<Player> players);
}



