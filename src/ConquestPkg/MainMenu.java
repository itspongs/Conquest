package ConquestPkg;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MainMenu extends JFrame {

    private boolean isFullScreen = true;
    private GraphicsDevice gd;

    public MainMenu() {
        setupWindow();
        showTitleCard();
    }

    // ===================== WINDOW SETUP =====================

    private void setupWindow() {
        setTitle("Conquest");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(this);
        setVisible(true);
    }

    // ===================== TITLE CARD =====================

    private void showTitleCard() {
        BackgroundPanel titlePanel = new BackgroundPanel();
        titlePanel.setLayout(new GridBagLayout());
        setContentPane(titlePanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        // "Welcome Player_Name"
        JLabel welcomeLabel = new JLabel("Welcome " + GameData.playerName, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.PLAIN, 52));
        welcomeLabel.setForeground(new Color(245, 230, 210));
        gbc.gridy = 0;
        titlePanel.add(welcomeLabel, gbc);

        // "ConQuest" logo label
        gbc.gridy = 1;
        titlePanel.add(buildConQuestLogo(72), gbc);

        revalidate();
        repaint();

        // After 2.5 seconds, transition to main menu
        Timer transitionTimer = new Timer(2500, e -> morphToMainMenu());
        transitionTimer.setRepeats(false);
        transitionTimer.start();
    }

    // ===================== MORPH TRANSITION =====================

    private void morphToMainMenu() {
        // Fade out title, fade in main menu using alpha animation
        JPanel mainMenuPanel = buildMainMenuPanel();
        mainMenuPanel.setOpaque(false);

        BackgroundPanel wrapper = new BackgroundPanel();
        wrapper.setLayout(new BorderLayout());
        wrapper.add(mainMenuPanel, BorderLayout.CENTER);

        // Swap content
        setContentPane(wrapper);
        revalidate();
        repaint();

        // Animate: shrink welcome text by transitioning font size via Timer
        animateTitleShrink(wrapper, mainMenuPanel);
    }

    private void animateTitleShrink(BackgroundPanel wrapper, JPanel mainMenuPanel) {
        float[] alpha = {0.0f};

        // Fade in the main menu panel
        Timer fadeTimer = new Timer(16, null);
        fadeTimer.addActionListener(e -> {
            alpha[0] += 0.03f;
            if (alpha[0] >= 1.0f) {
                alpha[0] = 1.0f;
                fadeTimer.stop();
                mainMenuPanel.setOpaque(true);
                revalidate();
                repaint();
            }
            wrapper.setAlpha(alpha[0]);
            wrapper.repaint();
        });
        fadeTimer.start();
    }

    // ===================== MAIN MENU PANEL =====================

    private JPanel buildMainMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        panel.add(buildMenuHeader(), BorderLayout.NORTH);
        panel.add(buildMenuButtons(), BorderLayout.CENTER);
        panel.add(buildTogglePanel(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildMenuHeader() {
        JPanel header = new JPanel(new GridBagLayout());
        header.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(30, 0, 5, 0);

        // "Welcome Player_Name" (smaller)
        JLabel welcomeLabel = new JLabel("Welcome " + GameData.playerName, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.PLAIN, 28));
        welcomeLabel.setForeground(new Color(245, 230, 210));
        gbc.gridy = 0;
        header.add(welcomeLabel, gbc);

        // "ConQuest" logo (smaller)
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        header.add(buildConQuestLogo(36), gbc);

        return header;
    }

    private JPanel buildMenuButtons() {
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 20, 0, 20);

        // Button labels
        String[] labels = {"PLAY", "SCORE", "EXIT"};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = i;
            buttonPanel.add(buildMenuButton(labels[i]), gbc);
        }

        return buttonPanel;
    }

    private JButton buildMenuButton(String label) {
        JButton btn = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                float s = getScale();
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                g2.translate(w / 2.0, h / 2.0);
                g2.scale(s, s);
                g2.translate(-w / 2.0, -h / 2.0);

                g2.setColor(getModel().isRollover() ? new Color(200, 150, 140) : new Color(190, 130, 120));
                g2.fillRoundRect(10, 5, w - 20, h - 10, 25, 25);

                g2.setFont(getFont());
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int textX = (w - fm.stringWidth(getText())) / 2;
                int textY = (h - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), textX, textY);

                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                float s = getScale();
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2.translate(w / 2.0, h / 2.0);
                g2.scale(s, s);
                g2.translate(-w / 2.0, -h / 2.0);
                g2.setColor(new Color(120, 60, 60));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(11, 6, w - 22, h - 12, 25, 25);
                g2.dispose();
            }

            private float getScale() {
                Object prop = getClientProperty("scale");
                return (prop instanceof Float) ? (Float) prop : 1.0f;
            }
        };

        btn.setPreferredSize(new Dimension(280, 120));
        btn.setFont(new Font("SansSerif", Font.BOLD, 28));
        btn.setForeground(new Color(245, 230, 210));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover animation
        startHoverAnimation(btn);

        // Button actions
        btn.addActionListener(e -> handleMenuAction(label));

        return btn;
    }

    private JPanel buildTogglePanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(buildToggleButton());
        return bottomPanel;
    }

    // ===================== CONQUEST LOGO =====================

    private JPanel buildConQuestLogo(int fontSize) {
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        logoPanel.setOpaque(false);

        // "Con" part
        JLabel conLabel = new JLabel("Con");
        conLabel.setFont(new Font("Serif", Font.BOLD, fontSize));
        conLabel.setForeground(new Color(60, 10, 10));

        // "Q" highlighted
        JLabel qLabel = new JLabel("Q");
        qLabel.setFont(new Font("Serif", Font.BOLD, fontSize));
        qLabel.setForeground(new Color(245, 230, 210));

        // "uest" part
        JLabel uestLabel = new JLabel("uest");
        uestLabel.setFont(new Font("Serif", Font.BOLD, fontSize));
        uestLabel.setForeground(new Color(60, 10, 10));

        logoPanel.add(conLabel);
        logoPanel.add(qLabel);
        logoPanel.add(uestLabel);

        return logoPanel;
    }

    // ===================== ACTIONS =====================

    private void handleMenuAction(String action) {
        switch (action) {
            case "PLAY":
                System.out.println("Starting game...");
                // TODO: open game screen
                break;
            case "SCORE":
                System.out.println("Opening scoreboard...");
                // TODO: open score screen
                break;
            case "EXIT":
                System.exit(0);
                break;
        }
    }

    private void startHoverAnimation(JButton btn) {
        float[] scale = {1.0f};
        float hoverScale = 1.05f;
        float normalScale = 1.0f;
        float speed = 0.005f;

        Timer hoverTimer = new Timer(10, e -> {
            float target = btn.getModel().isRollover() ? hoverScale : normalScale;
            if (Math.abs(scale[0] - target) < speed) {
                scale[0] = target;
            } else {
                scale[0] += scale[0] < target ? speed : -speed;
            }
            btn.putClientProperty("scale", scale[0]);
            btn.repaint();
        });
        hoverTimer.start();
    }

    private void toggleScreenMode() {
        if (isFullScreen) {
            gd.setFullScreenWindow(null);
            dispose();
            setUndecorated(false);
            setSize(1024, 768);
            setLocationRelativeTo(null);
            setVisible(true);
            isFullScreen = false;
        } else {
            dispose();
            setUndecorated(true);
            setVisible(true);
            gd.setFullScreenWindow(this);
            isFullScreen = true;
        }
    }

    private JButton buildToggleButton() {
        JButton toggleBtn = new JButton("⛶");
        toggleBtn.setFont(new Font("SansSerif", Font.PLAIN, 20));
        toggleBtn.setForeground(new Color(245, 230, 210));
        toggleBtn.setContentAreaFilled(false);
        toggleBtn.setBorderPainted(false);
        toggleBtn.setFocusPainted(false);
        toggleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleBtn.setToolTipText("Toggle Fullscreen / Windowed");
        toggleBtn.addActionListener(e -> toggleScreenMode());
        return toggleBtn;
    }

    // ===================== BACKGROUND PANEL =====================

    class BackgroundPanel extends JPanel {
        private BufferedImage bgImage;
        private float alpha = 1.0f;

        public BackgroundPanel() {
            bgImage = loadImage("/resources/background.png");
        }

        public void setAlpha(float alpha) {
            this.alpha = alpha;
        }

        private BufferedImage loadImage(String path) {
            try {
                return ImageIO.read(getClass().getResourceAsStream(path));
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            if (bgImage != null) {
                g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                drawFallbackGradient(g2);
            }
            g2.dispose();
        }

        private void drawFallbackGradient(Graphics2D g2) {
            g2.setPaint(new GradientPaint(
                0, 0, new Color(120, 10, 10),
                0, getHeight(), new Color(60, 5, 5)
            ));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}