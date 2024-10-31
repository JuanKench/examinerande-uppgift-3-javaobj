package board;

import javax.swing.*;

public class Tile {
    private final int nr;
    private final JButton button;

    public Tile(int nr, ImageIcon image) {
        this.nr = nr;
        button = new JButton(image);
    }

    public int getNr() {
        return nr;
    }

    public JButton getButton() {
        return button;
    }
}
