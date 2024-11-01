package board;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//Must sit on a JFrame of exactly 800x800 in dimension, with a background of size 800x800 and a container of size 480x480. Tiles must share equally of the 480x480 pixels.
public class Board {
    private final List<Tile> tiles;
    private final Grid grid;
    private final JLayeredPane pane;
    private final JLabel background;
    private final JPanel container;

    public Board(String backgroundLocation, int columns, int rows, List<Path> imageLocations) {
        //Initializing things
        pane = new JLayeredPane();
        background = new JLabel(new ImageIcon(backgroundLocation));

        tiles = setupTiles(columns * rows - 1, imageLocations);

        //Setup grid
        grid = new Grid(tiles, columns, rows);

        //Setup container
        container = setupContainer(grid);

        //Placing components correctly on the pane
        background.setBounds(0,0,800,800); //x & y starts from top left corner.
        container.setBounds(160,160,480,480);

        pane.add(background, Integer.valueOf(0));
        pane.add(container, Integer.valueOf(1)); //Number corresponds to the layer, layer 1 is on top of layer 0.
    }

    //TODO: Should be forcing the board setup to abort somehow if tile setup fails.
    public List<Tile> setupTiles(int quantity, List<Path> imageLocations) {
        List<Tile> tiles = new ArrayList<>();
        Tile emptyTile = new Tile(0);
        tiles.add(emptyTile);

        for (int i = 1; i < quantity + 1; i++) {
            //Get path that says i.png
            String wanted = String.format("%s.png", i);
            Optional<Path> path = imageLocations.stream().filter(p -> p.endsWith(wanted)).findFirst();

            if (path.isPresent()) {
                ImageIcon icon = new ImageIcon(path.get().toString());
                Tile tile = new Tile(i, icon, this);
                tiles.add(tile);
            } else {
                throw new IllegalStateException("Image file for a tile was not found.");
            }
        }

        return tiles;
    }

    public JPanel setupContainer(Grid grid) {
        Tile[][] content = grid.getContent();

        int columns = content.length;
        int rows = content[0].length;

        JPanel container = new JPanel(new GridLayout(columns, rows, 1, 1));
        container.setOpaque(false); //So that the tiles panel has a transparent background visible through the gaps.

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (content[row][column] != null) { //If there is a tile at the spot, add the (tiles) button to the container
                    Tile tile = content[row][column];
//                    JButton button = tile.getButton();

                    container.add(tile);
                }
            }
        }

        return container;
    }

    public void updateContainer() {
        //1. Remove old content //TODO: Is it best to remove and update or to return a completely new container?
        container.removeAll();

        //2. Add content in
        Tile[][] content = grid.getContent();

        for (int row = 0; row < grid.getRows(); row++) {
            for (int column = 0; column < grid.getColumns(); column++) {
                if (content[row][column] != null) { //If there is a tile at the spot, add the (tiles) button to the container
                    Tile tile = content[row][column];
//                    JButton button = tile.getButton();
                    if (tile != null) {
                        container.add(tile);
                    }
                }
            }
        }
        container.revalidate();
        container.repaint();

        if(grid.checkWin()) {
            winScren();
        }
    }

    public JLayeredPane getPane() {
        return pane;
    }

    public void sweep(Tile tile) {
        if (grid.adjacentToVacantSquare(tile)) {
            grid.swapWithVacant(tile);

            updateContainer();
        }
    }

    public void winScren(){
        JOptionPane.showMessageDialog(pane, "You win!");
    }

    public void autoWin() {
        Tile[][]solution = grid.generateSolutionGrid(grid.getColumns(), grid.getRows(), this);

        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getColumns(); col++) {
                grid.getContent()[row][col] = solution[row][col];
            }
        }
        updateContainer();
    }

    public void addAutoWinButton() {
        JButton autoWinButton = new JButton("Auto Win");
        autoWinButton.addActionListener(e -> autoWin());
        pane.add(autoWinButton, Integer.valueOf(2));
        autoWinButton.setBounds(10,10,100,30);
    }
}
