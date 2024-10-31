package board;

import javax.swing.*;
import java.awt.*;

public class Tile extends JButton {
    private final int nr;
//    private Grid grid;
    private Board board;
//    private final JButton button;

    public Tile(int nr, ImageIcon image, Board board) {
        this.nr = nr;
        this.setIcon(image);
        this.board = board;

        this.addActionListener(e -> {
            System.out.println("Clicked tile nr: " + nr);
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
}
