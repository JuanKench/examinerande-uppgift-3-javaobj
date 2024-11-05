import board.Game;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MenuMain extends JFrame {

    public MenuMain() {
        JMenuBar menuBar = new JMenuBar();
        JPanel panel = new JPanel();
        JLabel intro = new JLabel("Välkommen till vårt amazing, one of a kind 15 spel!");
        JLabel val = new JLabel("Vill du spela spelet eller vill du kolla vem som är bäst på det?");
        JButton start = new JButton("Start");
        JButton highscore = new JButton("Highscore");
        ImageIcon image = new ImageIcon("src/board/pictures/frontpage/numbers_falling.png");
        JLabel imageLabel = new JLabel(image);
        JLabel startGameLabel = new JLabel(new ImageIcon("src/board/pictures/startpage/sloth.png"));
        ImageIcon icon = new ImageIcon("src/leif.jpg");
        setIconImage(icon.getImage());

        JPanel NorthPanel = new JPanel();
        JPanel SouthPanel = new JPanel();

        setTitle("15 Spelet");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 600);

        start.addActionListener(i -> {
           JFrame startFrame = new JFrame();
           JPanel startPanel = new JPanel();
           JButton startDragon = new JButton("Start Dragon");
           JButton startNumbered = new JButton("Start Numbered");
//           JButton startCustom = new JButton("Start Custom");

           startFrame.setBounds(100, 100, 500, 620);

           startFrame.add(startPanel);
           startPanel.add(startDragon);
           startPanel.add(startNumbered);
//           startPanel.add(startCustom);
           startPanel.add(startGameLabel);

           startFrame.setVisible(true);
           startNumbered.addActionListener(a -> {
                JFrame frame = new JFrame();
                frame.setMaximumSize(new Dimension(800,800));
                frame.setMinimumSize(new Dimension(800,800));

                try (DirectoryStream<Path> temp = Files.newDirectoryStream(Paths.get("src/board/pictures/tiles/numbers"))) {
                    List<Path> paths = new ArrayList<>();
                    for (Path path : temp) paths.add(path);

                    Game game = new Game(paths, 4, 4, "src/board/pictures/background/wood.png");
                    frame.add(game.getBoard().getPane());

                    frame.setVisible(true);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            });

           startDragon.addActionListener(a -> {
               JFrame frame = new JFrame();
               frame.setMaximumSize(new Dimension(800,800));
               frame.setMinimumSize(new Dimension(800,800));

               try (DirectoryStream<Path> temp = Files.newDirectoryStream(Paths.get("src/board/pictures/tiles/dragon/pieces"))) {
                   List<Path> paths = new ArrayList<>();
                   for (Path path : temp) paths.add(path);

                   Game game = new Game(paths, 4, 4, "src/board/pictures/background/wood.png");
                   frame.add(game.getBoard().getPane());

                   frame.setVisible(true);
               } catch (Exception e) {
                   System.out.println(e.getMessage());
               }
           });
        });

        highscore.addActionListener(i -> {
            setTitle("Highscore");

            ArrayList<ScoreEntry> scores = new ArrayList<>();

            try (
                    BufferedReader reader = new BufferedReader(new FileReader("src/highScores"));
            ) {
                StringBuilder highScoresDisplay = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split(" - ");
                    if (split.length == 2) {
                        String name = split[0];
                        int score = Integer.parseInt(split[1].toString());
                        scores.add(new ScoreEntry(name, score));
                    }
                }
                Collections.sort(scores, Comparator.comparingInt(ScoreEntry::getScore));
                for (ScoreEntry entry : scores) {
                    highScoresDisplay.append(entry.getName()).append (" - ").append(entry.getScore()).append("\n");
                }
                JOptionPane.showMessageDialog(this, highScoresDisplay.toString(), "Highscores: ", JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "File not found");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Whomp whomp något gick fel");
            }
            setVisible(true);
        });


        panel.setLayout(new BorderLayout());
        panel.add(NorthPanel, BorderLayout.NORTH);
        panel.add(SouthPanel, BorderLayout.SOUTH);
        NorthPanel.setLayout(new GridLayout(3,1));
        SouthPanel.setLayout(new FlowLayout());
        NorthPanel.add(intro);
        NorthPanel.add(val);
        SouthPanel.add(start);
        SouthPanel.add(highscore);
        panel.revalidate();
        panel.repaint();
        panel.add(imageLabel);

        add(panel);

        setJMenuBar(menuBar);
        setVisible(true);
    }
}