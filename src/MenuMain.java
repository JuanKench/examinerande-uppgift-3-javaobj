import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MenuMain extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MenuMain() {
        JMenuBar menuBar = new JMenuBar();
        JPanel panel = new JPanel();
        JLabel intro = new JLabel("Välkommen till spelet 15 eller vad det än heter.");
        JLabel val = new JLabel("Vill du spela spelet eller vill du kolla vem som är bäst på det");
        JButton start = new JButton("Start");
        JButton highscore = new JButton("highscore");
        ImageIcon image = new ImageIcon("C:\\Users\\PC\\OneDrive\\Bilder\\Saved Pictures\\leif.jpg");
        JLabel imageLabel = new JLabel(image);
        ImageIcon icon = new ImageIcon("C:\\Users\\PC\\OneDrive\\Bilder\\Saved Pictures\\leif.jpg");
        setIconImage(icon.getImage());

        JPanel NorthPanel = new JPanel();
        JPanel SouthPanel = new JPanel();

        setTitle("15 Spelet");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);

        start.addActionListener(i -> {
           JFrame startFrame = new JFrame();
           JPanel startPanel = new JPanel();
           JButton startNormal = new JButton("Start Normal");
           JButton startCustom = new JButton("Start Custom");

           startFrame.setBounds(100, 100, 450, 300);

           startFrame.add(startPanel);
           startPanel.add(startNormal);
           startPanel.add(startCustom);

           startFrame.setVisible(true);
           startNormal.addActionListener(a -> {
               //startNormal alex code;
           });
           startCustom.addActionListener(a -> {
               startCustom.setEnabled(false);
               JButton begin = new JButton("Start Custom");
               JLabel height = new JLabel("vilken höjd på spelet skulle du vilja ha?");
               JTextField heightText = new JTextField(10);
               JLabel length = new JLabel("hur långt vill du att spelet ska vara?");
               JTextField lengthText = new JTextField(10);
               startPanel.add(height);
               startPanel.add(heightText);
               startPanel.add(length);
               startPanel.add(lengthText);
               startPanel.add(begin);
               startFrame.setVisible(true);
           });
        });

        highscore.addActionListener(i -> {
            JFrame highScoreframe = new JFrame();
            JPanel highScorePanel = new JPanel();
            JLabel text = new JLabel("här ser du dem som spelat spelet samt den bästa hittils");
            highScoreframe.setBounds(100, 100, 450, 300);
            highScoreframe.add(highScorePanel);
            highScorePanel.add(text);
            setTitle("highscore");

            ArrayList<ScoreEntry> scores = new ArrayList<>();

            try (
                    BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"));
            ) {
                StringBuilder highScoresDisplay = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split(",");
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
            highScoreframe.setVisible(true);
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