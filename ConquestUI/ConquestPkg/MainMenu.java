package ConquestPkg;

import java.awt.*;
import javax.swing.*;

public class MainMenu {

    JFrame frame;

    public MainMenu(String playerName) {

        frame = new JFrame("Conquest");
        frame.setSize(450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);

        // Welcome
        JLabel welcome = new JLabel("Welcome " + playerName);
        welcome.setBounds(150, 30, 200, 30);

        // Title
        JLabel title = new JLabel("ConQuest!");
        title.setBounds(160, 60, 200, 30);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        // Buttons
        JButton playBtn = new JButton("PLAY");
        playBtn.setBounds(60, 120, 100, 40);

        JButton scoreBtn = new JButton("SCORE");
        scoreBtn.setBounds(170, 120, 100, 40);

        JButton exitBtn = new JButton("EXIT");
        exitBtn.setBounds(280, 120, 100, 40);

        // Actions
        playBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Start Quiz here!");
        });

        scoreBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "View Scores here!");
        });

        exitBtn.addActionListener(e -> {
            System.exit(0);
        });

        // Add to frame
        frame.add(welcome);
        frame.add(title);
        frame.add(playBtn);
        frame.add(scoreBtn);
        frame.add(exitBtn);

        frame.setVisible(true);
    }
}