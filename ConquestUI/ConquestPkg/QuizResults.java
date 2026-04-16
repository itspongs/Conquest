package ConquestPkg;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class QuizResults {

    public QuizResults(JFrame frame, String playerName, int score, int total, String category) {

        // Capture date/time at the moment results are shown
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
        String time = now.format(DateTimeFormatter.ofPattern("hh:mm a"));

        // ── Loading screen first ──────────────────────────────────
        showLoadingScreen(frame, playerName, category, () ->
            showResults(frame, playerName, score, total, category, date, time)
        );
    }

    // ================= LOADING SCREEN =================
    private void showLoadingScreen(JFrame frame, String playerName, String category, Runnable onDone) {

        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JLabel title = new JLabel("ConQuest");
        title.setFont(new Font("Arial", Font.BOLD, 42));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel cat = new JLabel("Category: " + category);
        cat.setFont(new Font("Arial", Font.BOLD, 20));
        cat.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel loading = new JLabel("Loading...");
        loading.setFont(new Font("Arial", Font.PLAIN, 20));
        loading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel waiting = new JLabel("Waiting for Results!");
        waiting.setFont(new Font("Arial", Font.PLAIN, 20));
        waiting.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(cat);
        panel.add(Box.createRigidArea(new Dimension(0, 50)));
        panel.add(loading);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(waiting);
        panel.add(Box.createVerticalGlue());

        // Wrap in a centering panel so the bordered box doesn't stretch
        JPanel wrapper = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(480, 340));
        wrapper.add(panel);

        frame.add(wrapper, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();

        new javax.swing.Timer(3000, e -> {
            ((javax.swing.Timer) e.getSource()).stop();
            onDone.run();
        }).start();
    }

    // ================= RESULTS SCREEN =================
    private void showResults(JFrame frame, String playerName, int score, int total,
                             String category, String date, String time) {

        saveScore(playerName, category, score, total, date, time);
        playVictorySound();

        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JPanel wrapper = new JPanel(new GridBagLayout());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(235, 235, 235));
        card.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(Color.DARK_GRAY, 2, 20),
                BorderFactory.createEmptyBorder(30, 50, 30, 50)
        ));
        card.setPreferredSize(new Dimension(500, 380));

        // RESULTS header
        JLabel header = new JLabel("RESULTS");
        header.setFont(new Font("Arial", Font.BOLD, 28));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Well Done, player_name
        JLabel wellDone = new JLabel("Well Done, " + playerName);
        wellDone.setFont(new Font("Arial", Font.BOLD, 16));
        wellDone.setAlignmentX(Component.CENTER_ALIGNMENT);

        // "Your Quiz score is"
        JLabel scoreLabel = new JLabel("Your Quiz score is");
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Big score number
        JLabel scoreNum = new JLabel(score + " / " + total);
        scoreNum.setFont(new Font("Arial", Font.BOLD, 52));
        scoreNum.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Category
        JLabel catLabel = new JLabel("Category: " + category);
        catLabel.setFont(new Font("Arial", Font.BOLD, 16));
        catLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Date & Time
        JLabel dateLabel = new JLabel("Date: " + date);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel timeLabel = new JLabel("Time: " + time);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Go back button
        JButton backBtn = new JButton("Go back to Main Menu");
        backBtn.setFont(new Font("Arial", Font.PLAIN, 15));
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(240, 42));
        backBtn.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(Color.DARK_GRAY, 1, 20),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                backBtn.setBackground(new Color(173, 216, 230));
                backBtn.setContentAreaFilled(true);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                backBtn.setContentAreaFilled(false);
            }
        });
        backBtn.addActionListener(e -> new MainMenu(frame, playerName));

        card.add(header);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(wellDone);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(scoreLabel);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(scoreNum);
        card.add(Box.createRigidArea(new Dimension(0, 16)));
        card.add(catLabel);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(dateLabel);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(timeLabel);
        card.add(Box.createRigidArea(new Dimension(0, 24)));
        card.add(backBtn);

        wrapper.add(card);
        frame.add(wrapper, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    // ================= VICTORY SOUND =================
    private void playVictorySound() {
        try {
            java.net.URL url = getClass().getClassLoader().getResource("ConquestPkg/music/victory_sound.wav");
            if (url == null) {
                java.io.File f = new java.io.File("ConquestPkg/music/victory_sound.wav");
                if (f.exists()) url = f.toURI().toURL();
            }
            if (url == null) { System.err.println("[QuizResults] victory_sound.wav not found"); return; }

            javax.sound.sampled.AudioInputStream ais = javax.sound.sampled.AudioSystem.getAudioInputStream(url);
            javax.sound.sampled.Clip clip = javax.sound.sampled.AudioSystem.getClip();
            clip.open(ais);

            javax.sound.sampled.FloatControl volume = (javax.sound.sampled.FloatControl)
                    clip.getControl(javax.sound.sampled.FloatControl.Type.MASTER_GAIN);
            volume.setValue(volume.getMinimum() + (volume.getMaximum() - volume.getMinimum()) * 0.65f);

            clip.start(); // play once, no loop
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ================= SAVE SCORE =================
    private void saveScore(String playerName, String category, int score, int total, String date, String time) {
        String entry = "Player: " + playerName + "\n"
                     + "Category: " + category + "\n"
                     + "Score: " + score + "/" + total + "\n"
                     + "Date: " + date + "\n"
                     + "Time: " + time + "\n"
                     + "-----------------------------\n";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("ConquestPkg/score.txt", true))) {
            writer.write(entry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
