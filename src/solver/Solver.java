package solver;

import board.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

//Works by moving (/'walking') the empty tile across the board.
public class Solver {
    private Game game;
    private List<Coordinate> roundabout; //TODO: Should be like immutable maybe? Order matters
    private final Board board;
    private final Grid grid;
    private List<Tile> solvedTiles; //TODO: Should maybe be a stack?
    private Queue<Tile> unsolvedTiles; //TODO: Should probably be a queue?
    private Coordinate topLeftCorner;

    //1. Define circulation area - Is it possible? FIRST INTERMEDIARY POSITION, THEN ACTUAL POSITION
    //2. Move empty tile inside circulation area. Is it possible?
    //3. Circulate until in correct position.

    public Solver(Game game) {
        this.game = game;
        grid = game.getBoard().getGrid();
        board = game.getBoard();
        solvedTiles = new ArrayList<>();
        unsolvedTiles = new LinkedList<>(game.getBoard().getTiles()); //Create copy, should reference same objects but not same list... hopefully?
        roundabout = new ArrayList<>();
    }

    public void run() {
        while(!grid.checkWin()) {
            solveNext();
        }
    }

    public void routineStandard() {

    }

    public void routineEdge() {

    }


    public void solveNext() {
        //1. Get next unsolved tile.
        Tile nextTile = unsolvedTiles.poll(); //If no left, maybe like... quit run...?

        //2. Generate roundabout for intermediary
        Coordinate lastSolvedCoordinate = grid.tileCoordinate(solvedTiles.getLast()).get(); //TODO: Too tired to fix.
        Coordinate intermediaryCoordinate = new Coordinate(lastSolvedCoordinate.getX() + 1, lastSolvedCoordinate.getY() + 1);
        drawRoundabout(nextTile, intermediaryCoordinate); //Drawing and pulling in vacant tile
        //3. Rotate to intermediary spot
        rotateToCoordinate(nextTile, intermediaryCoordinate);

        //4. Generate roundabout for final spot
        Coordinate finalSpotCoordinate = new Coordinate(lastSolvedCoordinate.getX() + 1, lastSolvedCoordinate.getY());
        drawRoundabout(nextTile, finalSpotCoordinate);
        //5. Rotate to final spot
        rotateToCoordinate(nextTile, finalSpotCoordinate);
        solvedTiles.add(nextTile);
    }



    public void moveVacantIntoRoundabout(Tile targetTile) {
        //Check if vacant is in roundabout
        //If not, call move vacant closer
        while (!vacantIsInRoundabout()) {
            moveVacantCloserToRoundabout(targetTile);
        }
    }

    public boolean vacantIsInRoundabout() {
        Coordinate vacantCoordinate = grid.vacantSquareCoordinate();

        return roundabout.stream().anyMatch(coordinate -> coordinate.getX() == vacantCoordinate.getX() && coordinate.getY() == vacantCoordinate.getY());
    }

    public void moveVacantCloserToRoundabout(Tile targetTile) {
        Coordinate vacantCoordinate = grid.vacantSquareCoordinate();
        Coordinate targetTileCoordinate = grid.tileCoordinate(targetTile).get();

        List<Coordinate> adjacentCoordinates = adjacentCoordinates(vacantCoordinate);

        for (Coordinate coordinate : adjacentCoordinates) {
            if (isCloserToRoundabout(coordinate, vacantCoordinate) && !(vacantCoordinate.getX() == targetTileCoordinate.getX() && vacantCoordinate.getY() == targetTileCoordinate.getY())) {
                Tile vacant = grid.tileAtCoordinate(vacantCoordinate).get(); //TODO: Too tired.
                Tile destination = grid.tileAtCoordinate(coordinate).get(); //TODO: Too tired.

                grid.swap(vacant, destination);
                board.updateContainer();

                try { //Trying to get the program to pause after rotation
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return; //TODO: Lol this can't be right?
            }
        }
        //If there only is roundabout coordinate to the right, move to the right.
        //When there is an available roundabout coordinate (that is, a coordinate that is not occupied by the target tile) in a straight line either above to the left or right
        //It should always pick a tile that is closer to the edge of the roundabout.

    }

    //TODO: Must later make sure not to move into coordinates occupied by solved tiles and that the coordinate is actually within the grid
    public List<Coordinate> adjacentCoordinates(Coordinate coordinate) {
        List<Coordinate> adjacentCoordinates = new ArrayList<>();
        adjacentCoordinates.add(new Coordinate(coordinate.getX() - 1, coordinate.getY()));
        adjacentCoordinates.add(new Coordinate(coordinate.getX() + 1, coordinate.getY()));
        adjacentCoordinates.add(new Coordinate(coordinate.getX(), coordinate.getY() - 1));
        adjacentCoordinates.add(new Coordinate(coordinate.getX(), coordinate.getY() + 1));

        return adjacentCoordinates;
    }

    //Euclidean distance
    public double calculateDistance(Coordinate c1, Coordinate c2) {
        return Math.sqrt(Math.pow((c2.getX() - c1.getX()), 2) + Math.pow((c2.getY() - c1.getY()), 2));
    }

    //Something is closer if the difference is lesser than before
    public boolean isCloserToRoundabout(Coordinate suggestion, Coordinate current) {
        double currentDistance = calculateDistance(current, topLeftCorner);
        double suggestedDistance = calculateDistance(suggestion, topLeftCorner);

        return suggestedDistance < currentDistance; //If new distance is less, then it is closer to roundabout
    }

    //Is actually both drawing and setting up
    public void drawRoundabout(Tile targetTile, Coordinate targetSpotCoordinate) {
        roundabout = new ArrayList<>();

        Coordinate targetTileCoordinate = grid.tileCoordinate(targetTile).get(); //TODO: Too tired to fix.
//        Coordinate targetSpotCoordinate = new Coordinate(solvedTiles.getLast().getX() + 1, solvedTiles.getLast().getY() + 1);

        topLeftCorner = new Coordinate(targetSpotCoordinate.getX(), targetSpotCoordinate.getY());

        //Draw top
        int xStart = targetSpotCoordinate.getX();
        int xEnd = targetTileCoordinate.getX();
        int row = targetSpotCoordinate.getY();

        for (int i = xStart; i <= xEnd; i++) {
            roundabout.add(new Coordinate(i, row));
        }

        //Draw left side
        int yStart = targetSpotCoordinate.getY() + 1; //Because the first was drawn when drawing the top
        int yEnd = targetTileCoordinate.getY();
        int column = targetTileCoordinate.getX();

        for (int i = yStart; i <= yEnd; i++) {
            roundabout.add(new Coordinate(i, column));
        }

        //Draw bottom
        xStart = targetTileCoordinate.getX() + 1; //Because no duplicates
        xEnd = targetSpotCoordinate.getX();
        row = targetTileCoordinate.getY();

        for (int i = xStart; i >= xEnd; i--) {
            roundabout.add(new Coordinate(i, row));
        }

        //Draw right side
        yStart = targetTileCoordinate.getY() - 1; //Because no duplicate
        yEnd = targetSpotCoordinate.getY() + 1;
        column = targetSpotCoordinate.getX();

        for (int i = yStart; i >= yEnd; i--) {
            roundabout.add(new Coordinate(i, column));
        }

        moveVacantIntoRoundabout(targetTile); //Now the rotating can begin
    }

    public boolean tileInCorrectSpot(Coordinate targetTile, Coordinate correctSpot) {
        return targetTile.getX() == correctSpot.getX() && targetTile.getY() == correctSpot.getY();
    }

    public void rotateToCoordinate(Tile targetTile, Coordinate targetCoordinate) {
        Coordinate targetTileCoordinate = grid.tileCoordinate(targetTile).get();

        while (!tileInCorrectSpot(targetTileCoordinate, targetCoordinate)) {
            rotate();
        }
    }

    public void rotate() {
        Coordinate vacantCoordinate = grid.vacantSquareCoordinate(); //Get vacant square coordinate
        Optional<Tile> vacantTile = grid.tileAtCoordinate(vacantCoordinate);

        //Get the coordinate that comes after this in the roundabout
        Coordinate nextCoordinate = nextCoordinate(vacantCoordinate);
        Optional<Tile> nextTile = grid.tileAtCoordinate(nextCoordinate);

        if (vacantTile.isPresent() && nextTile.isPresent()) {
            grid.swap(vacantTile.get(), nextTile.get()); //Swap vacant with next tile in roundabout
            board.updateContainer();
            try { //Trying to get the program to pause after rotation
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //Returns the next coordinate in the roundabout
    public Coordinate nextCoordinate(Coordinate current) {
        int indexNext = roundabout.indexOf(current) + 1;

        if (indexNext == roundabout.size()) {
            return roundabout.getFirst();
        } else {
            return roundabout.get(indexNext);
        }
    }

    public boolean hasCoordinate(int x, int y) {
        return roundabout.stream().anyMatch(coordinate -> coordinate.getX() == x && coordinate.getY() == y);
    }
}
