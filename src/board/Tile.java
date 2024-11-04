package board;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class Tile extends JButton {
    private final int nr;
//    private Grid grid;
    private Board board;
//    private Coordinate coordinate;
//    private final JButton button;

    public Tile(int nr, ImageIcon image, Board board) {
        this.nr = nr;
        this.setIcon(image);
        this.board = board;
//        this.coordinate = Optional.empty();

        this.addActionListener(e -> {
            board.sweep(this);
        });
    }

    public Tile(int nr) {
        this.nr = nr;
        this.setBorder(BorderFactory.createLineBorder(Color.black)); //TODO: Why on earth is this necessary for the tile to be transparent?
    }

    public int getNr() {
        return nr;
    }

//    public Coordinate getCoordinate() {
//        return coordinate;
//    }
//
//    public void setCoordinate(Coordinate coordinate) {
//        this.coordinate = coordinate;
//    }
}
