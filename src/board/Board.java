package board;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//Must sit on a JFrame of exactly 800x800 in dimension, with a background of size 800x800 and a container of size 480x480. Tiles must share equally of the 480x480 pixels.
public class Board {
    private final List<Tile> tiles;
    private final Tile[][] grid;
    private final JLayeredPane pane;
    private final JLabel background;
    private final JPanel container;

    public Board(int columns, int rows, List<Path> paths, String backgroundLocation) {
        //Initializing things
        pane = new JLayeredPane();
        background = new JLabel(new ImageIcon(backgroundLocation));

        //1. Setup tiles
        tiles = setupTiles(rows * columns - 1, paths);

        //2. Setup grid
        grid = setupGrid(rows, columns, tiles);

        //3. Setup container
        container = setupContainer(grid);

        //Placing components correctly on the pane
        background.setBounds(0,0,800,800); //x & y starts from top left corner.
        container.setBounds(160,160,480,480);

        pane.add(background, Integer.valueOf(0));
        pane.add(container, Integer.valueOf(1)); //Number corresponds to the layer, layer 1 is on top of layer 0.
    }

    //TODO: Should be forcing the board setup to abort somehow if tile setup fails.
    public List<Tile> setupTiles(int quantity, List<Path> paths) {
        List<Tile> tiles = new ArrayList<>();

        for (int i = 1; i < quantity + 1; i++) {
            //Get path that says i.png
            String wanted = String.format("%s.png", i);
            Optional<Path> path = paths.stream().filter(p -> p.endsWith(wanted)).findFirst();

            if (path.isPresent()) {
                ImageIcon icon = new ImageIcon(path.get().toString());
                Tile tile = new Tile(i, icon);
                tiles.add(tile);
            } else {
                throw new IllegalStateException("Image file for a tile was not found.");
            }
        }

        return tiles;
    }

    //TODO: Can later change so that it applies a shuffle function when setting up, after which it ideally is checked if the shuffled result is solvable before returning the grid.
    public Tile[][] setupGrid(int columns, int rows, List<Tile> tiles) {
        Tile[][] grid = new Tile[columns][rows];

        int counter = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (counter < tiles.size()) {
                    Tile tile = tiles.get(counter);
                    grid[i][j] = tile;
                    counter++;
                }
            }
        }

        return grid;
    }

    public JPanel setupContainer(Tile[][] grid) {
        int columns = grid.length;
        int rows = grid[0].length;

        JPanel container = new JPanel(new GridLayout(columns, rows, 1, 1));
        container.setOpaque(false); //So that the tiles panel has a transparent background visible through the gaps.

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (grid[row][column] != null) { //If there is a tile at the spot, add the (tiles) button to the container
                    Tile tile = grid[row][column];
                    JButton button = tile.getButton();

                    container.add(button);
                } else { //Else add an invisible button (open tile)
                    JButton button = new JButton();
                    button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }
            }
        }

        return container;
    }

    public JLayeredPane getPane() {
        return pane;
    }
}
