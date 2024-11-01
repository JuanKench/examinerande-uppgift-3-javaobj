package board;

import javax.swing.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Game {
    private final Board board;
    private int moves;

    public Game(List<Path> imageLocations, int columns, int rows, String backgroundLocation) {
        board = new Board(backgroundLocation, columns, rows, imageLocations);
        board.addAutoWinButton();
    }

    public Board getBoard() {
        return board;
    }
}
