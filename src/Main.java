import board.Board;
import board.Game;

import javax.swing.*;
import java.awt.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        System.out.println("Hello Johan");

        JFrame frame = new JFrame();
        frame.setMaximumSize(new Dimension(800,800));
        frame.setMinimumSize(new Dimension(800,800));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try (DirectoryStream<Path> temp = Files.newDirectoryStream(Paths.get("src/board/pictures/tiles/dragon/pieces"))) {
            List<Path> paths = new ArrayList<>();
            for (Path path : temp) paths.add(path);

            Game game = new Game(paths, 4, 4, "src/board/pictures/background/wood.png");
            frame.add(game.getBoard().getPane());

            frame.setVisible(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}