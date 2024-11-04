package board;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//TODO: Should probably be covered by tests.
public class Grid {
    private final int columns, rows;
    private final Tile[][] content; //TODO: Might cause problems, what is even a final array?

    public Grid(List<Tile> tiles, int columns, int rows) {
        content = setupGrid(columns, rows, tiles);
        this.columns = columns;
        this.rows = rows;
    }

    //TODO: Can later change so that it applies a shuffle function when setting up, after which it ideally is checked if the shuffled result is solvable before returning the grid.
    public Tile[][] setupGrid(int columns, int rows, List<Tile> tiles) {
        Tile[][] content = new Tile[columns][rows];

        int counter = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (counter < tiles.size()) {
                    Tile tile = tiles.get(counter);
                    content[i][j] = tile;
                    counter++;
                }
            }
        }

        return content;
    }

    public Tile[][] getContent() {
        return content;
    }

    public Optional<Coordinate> tileCoordinate(Tile tile) {
        for (int column = 0; column < content.length; column++) {
            for (int row = 0; row < content.length; row++) {
                if (content[column][row] == tile) {
                    return Optional.of(new Coordinate(column, row));
                }
            }
        }

        return Optional.empty();
    }

    public Coordinate vacantSquareCoordinate() {
        for (int column = 0; column < content.length; column++) {
            for (int row = 0; row < content.length; row++) {
                if (content[column][row].getNr() == 0) {
                    return new Coordinate(column, row);
                }
            }
        }

        throw new IllegalStateException("No vacant square coordinate.");
    }

    public boolean adjacentToVacantSquare(Tile tile) {
        //1. Identify tile coordinate
        Optional<Coordinate> coordinate = tileCoordinate(tile);

        //2. If present, check surroundings.
        if (coordinate.isPresent()) {
            Coordinate current = coordinate.get();

            Coordinate above = new Coordinate(current.getX(), current.getY() - 1);
            Coordinate below = new Coordinate(current.getX(), current.getY() + 1);
            Coordinate left = new Coordinate(current.getX() - 1, current.getY());
            Coordinate right = new Coordinate(current.getX() + 1, current.getY());

            return containsVacantSquare(List.of(above, below, left, right));
        } else {
            throw new IllegalArgumentException("Tile not part of grid content.");
        }
    }

    public boolean containsVacantSquare(List<Coordinate> coordinates) {
        for (Coordinate coordinate : coordinates) {
            int x = coordinate.getX();
            int y = coordinate.getY();

            if (x >= 0 && x < columns && y >= 0 && y < rows) {
                if (content[x][y].getNr() == 0) {
                    return true;
                }
            }
        }

        return false;
    }

    public void swap(Tile t1, Tile t2) {
        Optional<Coordinate> c1 = tileCoordinate(t1);
        Optional<Coordinate> c2 = tileCoordinate(t2);

        if (c1.isPresent() && c2.isPresent()) {
            Coordinate one = c1.get();
            Coordinate two = c2.get();

            content[one.getX()][one.getY()] = t2;
            content[two.getX()][two.getY()] = t1;
        } else {
            throw new IllegalArgumentException("Tile(s) not part of grid content.");
        }
    }

    public void swapWithVacant(Tile t) {
        Coordinate vacantSquareCoordinate = vacantSquareCoordinate();
        Tile vacantSquare = content[vacantSquareCoordinate.getX()][vacantSquareCoordinate.getY()];

        swap(t, vacantSquare);
    }

    public Tile[][] generateSolutionGrid(int columns, int rows, Board board) {
        Tile[][] solutionGrid = new Tile[columns][rows];
        int counter = 1;
        for (int i = 0;i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i == rows - 1 && j == columns - 1) {
                    solutionGrid[j][i] = new Tile(0);
                }else {
                    ImageIcon icon = new ImageIcon("src/board/pictures/tiles/dragon/pieces/" + counter + ".png");
                    solutionGrid[i][j] = new Tile(counter, icon, board);
                    counter++;
                }
            }
        }
        return solutionGrid;
    }

    public boolean checkWin(){
        Tile[][] grid = this.getContent();
        int counter = 1;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].getNr() != counter && counter != 16){
                    return false;
                }
                counter++;
            }
        }
        return true;
    }

    public Optional<Tile> tileAtCoordinate(Coordinate coordinate) {
        if (coordinate.getX() >= 0 && coordinate.getX() < columns && coordinate.getY() >= 0 && coordinate.getY() < rows) {
            return Optional.of(content[coordinate.getX()][coordinate.getY()]);
        } else {
            return Optional.empty();
        }
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }
}
