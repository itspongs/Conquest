package ConquestPkg;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainMenu {

    public MainMenu(JFrame frame, String playerName) {

        frame.setLayout(new GridBagLayout());
        frame.getContentPane().removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        // 🔥 Welcome Text
        JLabel welcome = new JLabel("Welcome " + playerName);
        welcome.setFont(new Font("Arial", Font.BOLD, 24));

        gbc.gridy = 0;
        gbc.insets = new Insets(5, 10, 2, 10);
        frame.add(welcome, gbc);

        // 🔥 Title
        JLabel title = new JLabel("ConQuest!");
        title.setFont(new Font("Arial", Font.BOLD, 40));

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 15, 10);
        frame.add(title, gbc);

        // 🔥 Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton playBtn = new JButton("PLAY");
        JButton scoreBtn = new JButton("SCORE");
        JButton exitBtn = new JButton("EXIT");

        Font btnFont = new Font("Arial", Font.BOLD, 18);
        Dimension btnSize = new Dimension(130, 50);

        playBtn.setFont(btnFont);
        scoreBtn.setFont(btnFont);
        exitBtn.setFont(btnFont);

        playBtn.setPreferredSize(btnSize);
        scoreBtn.setPreferredSize(btnSize);
        exitBtn.setPreferredSize(btnSize);

        buttonPanel.add(playBtn);
        buttonPanel.add(scoreBtn);
        buttonPanel.add(exitBtn);

        gbc.gridy = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        frame.add(buttonPanel, gbc);

        // 🔥 Description Label (hover text)
        JLabel description = new JLabel(" ");
        description.setFont(new Font("Arial", Font.PLAIN, 16));
        description.setHorizontalAlignment(JLabel.CENTER);

        gbc.gridy = 3;
        gbc.insets = new Insets(20, 10, 10, 10);
        frame.add(description, gbc);

        // 🔥 HOVER EFFECTS

        playBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                description.setText("Test your knowledge! Face the challenge and prove your intellect now.");
            }

            public void mouseExited(MouseEvent e) {
                description.setText(" ");
            }
        });

        scoreBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                description.setText("View the history of your scores!");
            }

            public void mouseExited(MouseEvent e) {
                description.setText(" ");
            }
        });

        exitBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                description.setText("Are you sure you want to quit?");
            }

            public void mouseExited(MouseEvent e) {
                description.setText(" ");
            }
        });

        // 🔥 BUTTON ACTIONS

        // PLAY → QuizMenu
        playBtn.addActionListener(e -> {
            new QuizMenu(frame, playerName);
        });

        // SCORE
        scoreBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "View Scores here!");
        });

        // EXIT
        exitBtn.addActionListener(e -> {
            System.exit(0);
        });

        frame.revalidate();
        frame.repaint();
    }
}