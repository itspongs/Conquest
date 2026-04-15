package ConquestPkg;

import java.awt.*;
import javax.swing.*;

public class QuizMenu {

    public QuizMenu(JFrame frame, String playerName) {

        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        // 🔥 OUTER PANEL (adds margin like your image)
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // 🔥 TOP: Welcome
        JLabel welcome = new JLabel("Welcome " + playerName, JLabel.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(welcome, BorderLayout.NORTH);

        // ================= CENTER AREA =================
        JPanel centerPanel = new JPanel(new BorderLayout());

        // 🔥 LEFT BUTTONS
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(3, 1, 10, 15));

        JButton geoBtn = new JButton("Geography");
        JButton driveBtn = new JButton("Driving");
        JButton mediaBtn = new JButton("Media");

        // 🔥 RIGHT BUTTONS
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(3, 1, 10, 15));

        JButton genBtn = new JButton("<html><center>General<br>Knowledge</center></html>");
        JButton brainBtn = new JButton("Brainrot");
        JButton progBtn = new JButton("Programming");

        JButton[] buttons = {
            geoBtn, driveBtn, mediaBtn,
            genBtn, brainBtn, progBtn
        };

        // 🔥 BUTTON STYLE
        Font btnFont = new Font("Arial", Font.PLAIN, 16);
        Dimension btnSize = new Dimension(160, 45);

        for (JButton btn : buttons) {
            btn.setFont(btnFont);
            btn.setPreferredSize(btnSize);
            btn.setFocusPainted(false);
        }

        leftPanel.add(geoBtn);
        leftPanel.add(driveBtn);
        leftPanel.add(mediaBtn);

        rightPanel.add(genBtn);
        rightPanel.add(brainBtn);
        rightPanel.add(progBtn);

        // 🔥 TITLE
        JLabel title = new JLabel("ConQuest", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 40));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(title, BorderLayout.CENTER);

        JPanel middleWrapper = new JPanel(new BorderLayout(30, 0));
        middleWrapper.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        middleWrapper.add(leftPanel, BorderLayout.WEST);
        middleWrapper.add(titlePanel, BorderLayout.CENTER);
        middleWrapper.add(rightPanel, BorderLayout.EAST);

        centerPanel.add(middleWrapper, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // ================= BOTTOM =================
        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        backBtn.setPreferredSize(new Dimension(120, 40));

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backBtn);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // 🔥 ADD TO FRAME
        frame.add(mainPanel, BorderLayout.CENTER);

        // ================= ACTIONS =================

        backBtn.addActionListener(e -> {
            new MainMenu(frame, playerName);
        });

        geoBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Geography Quiz"));
        mediaBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Media Quiz"));
        genBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "General Knowledge Quiz"));
        brainBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Brainrot Quiz"));
        progBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Programming Quiz"));

        // 🚗 FIXED: Driving button opens DrivingQuiz
        driveBtn.addActionListener(e -> {
            new DrivingQuiz(frame, playerName);
        });

        frame.revalidate();
        frame.repaint();
    }
}