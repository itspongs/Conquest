package ConquestPkg;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class QuizResults {

    public QuizResults(JFrame frame, String playerName, int score, int total, String category) {

        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
        String time = now.format(DateTimeFormatter.ofPattern("hh:mm a"));

        showLoadingScreen(frame, playerName, category, () ->
            showResults(frame, playerName, score, total, category, date, time)
        );
    }

    private JPanel makeBg() {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, new Color(139, 0, 0),
                                              getWidth(), getHeight(), new Color(80, 0, 0)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                int cx = getWidth()/2, cy = getHeight()/2;
                float r = Math.max(getWidth(), getHeight()) * 0.6f;
                g2.setPaint(new RadialGradientPaint(cx, cy, r,
                    new float[]{0f, 0.5f, 1f},
                    new Color[]{new Color(200,50,50,80), new Color(139,0,0,40), new Color(80,0,0,0)}
                ));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
    }

    private void showLoadingScreen(JFrame frame, String playerName, String category, Runnable onDone) {

        frame.getContentPane().removeAll();

        JPanel bg = makeBg();
        bg.setLayout(new GridBagLayout());

        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 25));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.setColor(new Color(255, 255, 255, 60));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 28, 28);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(420, 300));
        card.setBorder(BorderFactory.createEmptyBorder(36, 50, 36, 50));

        JLabel title = new JLabel("ConQuest");
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel cat = new JLabel("Category: " + category);
        cat.setFont(new Font("Arial", Font.BOLD, 20));
        cat.setForeground(new Color(255, 200, 200));
        cat.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel loading = new JLabel("Loading...");
        loading.setFont(new Font("Arial", Font.PLAIN, 18));
        loading.setForeground(new Color(255, 220, 220));
        loading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel waiting = new JLabel("Waiting for Results!");
        waiting.setFont(new Font("Arial", Font.BOLD, 22));
        waiting.setForeground(Color.WHITE);
        waiting.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(cat);
        card.add(Box.createRigidArea(new Dimension(0, 36)));
        card.add(loading);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(waiting);
        card.add(Box.createVerticalGlue());

        bg.add(card);
        frame.setContentPane(bg);
        frame.revalidate();
        frame.repaint();

        new javax.swing.Timer(3000, e -> {
            ((javax.swing.Timer) e.getSource()).stop();
            onDone.run();
        }).start();
    }

    private void showResults(JFrame frame, String playerName, int score, int total,
                             String category, String date, String time) {

        saveScore(playerName, category, score, total, date, time);
        playVictorySound();

        frame.getContentPane().removeAll();

        JPanel bg = makeBg();
        bg.setLayout(new GridBagLayout());

        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 25));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.setColor(new Color(255, 255, 255, 60));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 28, 28);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(500, 400));
        card.setBorder(BorderFactory.createEmptyBorder(32, 56, 32, 56));

        JLabel header = new JLabel("RESULTS", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 32));
        header.setForeground(Color.WHITE);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel wellDone = new JLabel("Well Done, " + playerName + "!", JLabel.CENTER);
        wellDone.setFont(new Font("Arial", Font.BOLD, 18));
        wellDone.setForeground(new Color(255, 200, 200));
        wellDone.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 255, 255, 60));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        JLabel scoreLabel = new JLabel("Your Quiz score is", JLabel.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        scoreLabel.setForeground(new Color(255, 220, 220));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreNum = new JLabel(score + " / " + total, JLabel.CENTER);
        scoreNum.setFont(new Font("Arial", Font.BOLD, 60));
        scoreNum.setForeground(Color.WHITE);
        scoreNum.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel catLabel = new JLabel("Category: " + category, JLabel.CENTER);
        catLabel.setFont(new Font("Arial", Font.BOLD, 16));
        catLabel.setForeground(new Color(255, 200, 200));
        catLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel dateLabel = new JLabel("Date: " + date, JLabel.CENTER);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateLabel.setForeground(new Color(255, 220, 220));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel timeLabel = new JLabel("Time: " + time, JLabel.CENTER);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        timeLabel.setForeground(new Color(255, 220, 220));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton backBtn = new JButton("Go back to Main Menu") {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hovered ? new Color(210, 110, 110) : new Color(188, 74, 74));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(0,0,0,40));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        backBtn.setFont(new Font("Arial", Font.BOLD, 16));
        backBtn.setForeground(Color.WHITE);
        backBtn.setPreferredSize(new Dimension(240, 48));
        backBtn.setMaximumSize(new Dimension(240, 48));
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setContentAreaFilled(false);
        backBtn.setOpaque(false);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> new MainMenu(frame, playerName));

        card.add(header);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(wellDone);
        card.add(Box.createRigidArea(new Dimension(0, 16)));
        card.add(sep);
        card.add(Box.createRigidArea(new Dimension(0, 16)));
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

        bg.add(card);
        frame.setContentPane(bg);
        frame.revalidate();
        frame.repaint();
    }

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
            volume.setValue(volume.getMinimum() + (volume.getMaximum() - volume.getMinimum()) * 0.75f);
            clip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void saveScore(String playerName, String category, int score, int total, String date, String time) {
        String entry = "Player: " + playerName + "\n"
                     + "Category: " + category + "\n"
                     + "Score: " + score + "/" + total + "\n"
                     + "Date: " + date + "\n"
                     + "Time: " + time + "\n"
                     + "-----------------------------\n";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("ConquestPkg/score.txt", true))) {
            writer.write(entry);
        } catch (IOException e) { e.printStackTrace(); }
    }
}
