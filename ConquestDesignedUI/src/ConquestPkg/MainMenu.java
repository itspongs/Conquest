package ConquestPkg;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MainMenu {

    private JFrame frame;
    private boolean isFullScreen = true;
    private GraphicsDevice gd;
    private BufferedImage logoImage;

    // Logo natural aspect ratio
    private int logoNaturalW = 480;
    private int logoNaturalH = 160;

    public MainMenu(JFrame existingFrame) {
        this.frame = existingFrame;
        gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        loadLogoImage();
        showTitleCard();
    }

    // ===================== LOAD LOGO ONCE =====================

    private void loadLogoImage() {
        try {
            logoImage = ImageIO.read(getClass().getResourceAsStream("/resources/conquest_logo.png"));
            if (logoImage != null) {
                logoNaturalW = logoImage.getWidth();
                logoNaturalH = logoImage.getHeight();
            }
        } catch (Exception e) {
            logoImage = null;
        }
    }

    // ===================== LOGO LABEL (crisp, aspect-correct) =====================

    private JLabel buildLogoLabel(int targetWidth) {
        // Derive height from actual aspect ratio
        int targetHeight = (logoImage != null)
            ? (int) ((double) logoNaturalH / logoNaturalW * targetWidth)
            : targetWidth / 3;

        JLabel label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (logoImage != null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.drawImage(logoImage, 0, 0, getWidth(), getHeight(), this);
                    g2.dispose();
                } else {
                    super.paintComponent(g);
                }
            }
        };
        label.setPreferredSize(new Dimension(targetWidth, targetHeight));
        if (logoImage == null) {
            label.setText("ConQuest");
            label.setFont(new Font("Serif", Font.BOLD, targetHeight / 2));
            label.setForeground(new Color(245, 230, 210));
            label.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return label;
    }

    // ===================== TITLE CARD =====================

    private void showTitleCard() {
        BackgroundPanel titlePanel = new BackgroundPanel();
        titlePanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel welcomeLabel = new JLabel("Welcome, " + GameData.playerName, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 36));
        welcomeLabel.setForeground(new Color(245, 230, 210));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 6, 0);
        titlePanel.add(welcomeLabel, gbc);

        int startLogoW = 480;
        JLabel logoLabel = buildLogoLabel(startLogoW);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        titlePanel.add(logoLabel, gbc);

        frame.setContentPane(titlePanel);
        frame.revalidate();
        frame.repaint();

        Timer holdTimer = new Timer(2500, e ->
            animateTitleToHeader(titlePanel, welcomeLabel, logoLabel, startLogoW)
        );
        holdTimer.setRepeats(false);
        holdTimer.start();
    }

    // ===================== SHRINK + PAN UP ANIMATION =====================

    private void animateTitleToHeader(BackgroundPanel titlePanel, JLabel welcomeLabel, JLabel logoLabel, int startLogoW) {
        int screenW = frame.getWidth();
        int screenH = frame.getHeight();

        int startLogoH = logoLabel.getPreferredSize().height;

        // Target logo width — moderate size
        int targetLogoW = 320;
        int targetLogoH = (logoImage != null)
            ? (int) ((double) logoNaturalH / logoNaturalW * targetLogoW)
            : targetLogoW / 3;

        // Welcome label stays same font throughout
        Dimension wDim = welcomeLabel.getPreferredSize();

        // Starting positions — centered on screen
        int startWX    = (screenW - wDim.width) / 2;
        int startWY    = screenH / 2 - wDim.height - 6;
        int startLogoX = (screenW - startLogoW) / 2;
        int startLogoY = screenH / 2 - startLogoH / 2 + 10;

        // Target positions — near top, toggle button is ~36px tall so pad below it
        int topPad     = 40;
        int targetWY   = topPad;
        int targetWX   = (screenW - wDim.width) / 2;
        int targetLogoY = targetWY + wDim.height + 6;
        int targetLogoX = (screenW - targetLogoW) / 2;

        // Switch to null layout for free positioning
        titlePanel.setLayout(null);
        welcomeLabel.setBounds(startWX, startWY, wDim.width, wDim.height);
        logoLabel.setBounds(startLogoX, startLogoY, startLogoW, startLogoH);
        titlePanel.add(welcomeLabel);
        titlePanel.add(logoLabel);
        titlePanel.revalidate();
        titlePanel.repaint();

        float[] progress = {0.0f};

        Timer shrinkTimer = new Timer(16, null);
        shrinkTimer.addActionListener(e -> {
            progress[0] += 0.018f;
            if (progress[0] >= 1.0f) {
                progress[0] = 1.0f;
                shrinkTimer.stop();
                morphToMainMenu();
                return;
            }

            float t = easeInOut(progress[0]);

            int curLogoW = (int) lerp(startLogoW, targetLogoW, t);
            int curLogoH = (int) lerp(startLogoH, targetLogoH, t);
            int curLogoX = (int) lerp(startLogoX, targetLogoX, t);
            int curLogoY = (int) lerp(startLogoY, targetLogoY, t);

            // Keep welcome label horizontally centered at all times
            int curWX = (screenW - wDim.width) / 2;
            int curWY = (int) lerp(startWY, targetWY, t);

            welcomeLabel.setBounds(curWX, curWY, wDim.width, wDim.height);
            logoLabel.setBounds(curLogoX, curLogoY, curLogoW, curLogoH);

            titlePanel.repaint();
        });

        shrinkTimer.start();
    }

    // ===================== MORPH TRANSITION =====================

    private void morphToMainMenu() {
        BackgroundPanel wrapper = new BackgroundPanel();
        wrapper.setLayout(new BorderLayout());
        wrapper.add(buildMainMenuPanel(), BorderLayout.CENTER);
        wrapper.setAlpha(0.0f);

        frame.setContentPane(wrapper);
        frame.revalidate();
        frame.repaint();

        float[] alpha = {0.0f};
        Timer fadeTimer = new Timer(16, null);
        fadeTimer.addActionListener(e -> {
            alpha[0] += 0.04f;
            if (alpha[0] >= 1.0f) {
                alpha[0] = 1.0f;
                ((Timer) e.getSource()).stop();
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
        panel.add(buildAnimatedButtonPanel(), BorderLayout.CENTER);
        return panel;
    }

    // ===================== MENU HEADER =====================

    private JPanel buildMenuHeader() {
        // Outer header uses BoxLayout vertically: toggle row on top, then logo area
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);

        // --- Row 1: toggle button flush right ---
        JPanel toggleRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        toggleRow.setOpaque(false);
        toggleRow.add(buildToggleButton());
        header.add(toggleRow);

        // --- Row 2: welcome + logo centered ---
        JPanel logoArea = new JPanel(new GridBagLayout());
        logoArea.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel welcomeLabel = new JLabel("Welcome, " + GameData.playerName, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 36));
        welcomeLabel.setForeground(new Color(245, 230, 210));
        gbc.gridy = 0;
        gbc.insets = new Insets(4, 0, 6, 0);
        logoArea.add(welcomeLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        logoArea.add(buildLogoLabel(320), gbc);

        header.add(logoArea);

        return header;
    }

    // ===================== ANIMATED BUTTON PANEL =====================

    private JPanel buildAnimatedButtonPanel() {
        JPanel clipPanel = new JPanel(null) {
            @Override
            public boolean isOptimizedDrawingEnabled() { return false; }
        };
        clipPanel.setOpaque(false);

        String[] labels = {"PLAY", "SCORE", "EXIT"};
        String[] descs = {
            "Test your knowledge! Face the challenge and prove your intellect now.",
            "View all of your past scores in your past quizzes!",
            "Are you sure you want to quit?"
        };

        JButton[] buttons = new JButton[3];
        for (int i = 0; i < 3; i++) {
            buttons[i] = buildMenuButton(labels[i], descs[i]);
            clipPanel.add(buttons[i]);
        }

        // Wait for real layout size then animate
        Timer waitTimer = new Timer(50, null);
        waitTimer.addActionListener(e -> {
            if (clipPanel.getWidth() > 0 && clipPanel.getHeight() > 0) {
                ((Timer) e.getSource()).stop();
                animateButtonsIn(clipPanel, buttons);
            }
        });
        waitTimer.start();

        return clipPanel;
    }

    private void animateButtonsIn(JPanel clipPanel, JButton[] buttons) {
        int panelW = clipPanel.getWidth();
        int panelH = clipPanel.getHeight();
        int btnW   = 280;
        int btnH   = 120;

        int centerY      = (panelH - btnH) / 2;
        int centerX      = (panelW - btnW) / 2;
        int playTargetX  = centerX - btnW - 40;
        int scoreTargetX = centerX;
        int exitTargetX  = centerX + btnW + 40;

        // Start off-screen
        buttons[0].setBounds(-btnW,        centerY, btnW, btnH); // PLAY  — from left
        buttons[1].setBounds(scoreTargetX, panelH,  btnW, btnH); // SCORE — from bottom
        buttons[2].setBounds(panelW,       centerY, btnW, btnH); // EXIT  — from right

        float[] progress = {0.0f};

        Timer panTimer = new Timer(16, null);
        panTimer.addActionListener(e -> {
            progress[0] += 0.025f;
            if (progress[0] >= 1.0f) {
                progress[0] = 1.0f;
                ((Timer) e.getSource()).stop();
            }
            float t = easeOut(progress[0]);

            buttons[0].setBounds((int) lerp(-btnW,  playTargetX,  t), centerY,                          btnW, btnH);
            buttons[1].setBounds(scoreTargetX,       (int) lerp(panelH, centerY, t),                    btnW, btnH);
            buttons[2].setBounds((int) lerp(panelW,  exitTargetX, t), centerY,                          btnW, btnH);

            clipPanel.repaint();
        });
        panTimer.start();
    }

    // ===================== MENU BUTTON WITH HOVER EXPAND =====================

    private JButton buildMenuButton(String label, String description) {
        int collapsedH = 120;
        int expandedH  = 220;
        int btnW       = 280;

        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                // Background
                g2.setColor(getModel().isRollover() ? new Color(200, 150, 140) : new Color(190, 130, 120));
                g2.fillRoundRect(10, 5, w - 20, h - 10, 25, 25);

                float descAlpha = getDescAlpha();

                // Label
                g2.setFont(new Font("SansSerif", Font.BOLD, 28));
                g2.setColor(new Color(245, 230, 210));
                FontMetrics fm = g2.getFontMetrics();
                int textX = (w - fm.stringWidth(label)) / 2;

                if (descAlpha < 0.01f) {
                    // Vertically centered
                    int textY = (h - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString(label, textX, textY);
                } else {
                    // Near top
                    int textY = 28 + fm.getAscent();
                    g2.drawString(label, textX, textY);

                    // Description fades in
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, descAlpha));
                    g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
                    FontMetrics fd = g2.getFontMetrics();

                    // Word-wrap
                    String[] words = description.split("\\s+");
                    StringBuilder line = new StringBuilder();
                    java.util.List<String> lines = new java.util.ArrayList<>();
                    for (String word : words) {
                        String test = line.length() == 0 ? word : line + " " + word;
                        if (fd.stringWidth(test) < w - 40) {
                            line = new StringBuilder(test);
                        } else {
                            if (line.length() > 0) lines.add(line.toString());
                            line = new StringBuilder(word);
                        }
                    }
                    if (line.length() > 0) lines.add(line.toString());

                    int lineH      = fd.getHeight();
                    int totalTextH = lines.size() * lineH;
                    int sy         = 75 + (h - 75 - totalTextH) / 2 + fd.getAscent();
                    for (String ln : lines) {
                        g2.drawString(ln, (w - fd.stringWidth(ln)) / 2, sy);
                        sy += lineH;
                    }
                }
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2.setColor(new Color(120, 60, 60));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(11, 6, w - 22, h - 12, 25, 25);
                g2.dispose();
            }

            private float getDescAlpha() {
                Object p = getClientProperty("descAlpha");
                return (p instanceof Float) ? (Float) p : 0.0f;
            }
        };

        btn.setFont(new Font("SansSerif", Font.BOLD, 28));
        btn.setForeground(new Color(245, 230, 210));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> handleMenuAction(label));

        // Hover expand timer
        float[] currentH = {collapsedH};

        Timer hoverTimer = new Timer(16, e -> {
            boolean hovered = btn.getModel().isRollover();
            float targetH   = hovered ? expandedH : collapsedH;
            float speed     = 6f;

            if (Math.abs(currentH[0] - targetH) < speed) {
                currentH[0] = targetH;
            } else {
                currentH[0] += currentH[0] < targetH ? speed : -speed;
            }

            float expandProgress = (currentH[0] - collapsedH) / (float)(expandedH - collapsedH);
            float descAlpha = Math.max(0f, (expandProgress - 0.5f) * 2f);
            btn.putClientProperty("descAlpha", descAlpha);

            Rectangle bounds = btn.getBounds();
            if (bounds.width > 0) {
                // Expand from center Y of collapsed position
                int centerY = bounds.y + (int)(currentH[0] > collapsedH
                    ? collapsedH / 2
                    : bounds.height / 2);
                int newH = (int) currentH[0];
                btn.setBounds(bounds.x, centerY - newH / 2, btnW, newH);
                if (btn.getParent() != null) btn.getParent().repaint();
            }
        });
        hoverTimer.start();

        return btn;
    }

    // ===================== ACTIONS =====================

    private void handleMenuAction(String action) {
        switch (action) {
            case "PLAY":
                System.out.println("Starting game...");
                break;
            case "SCORE":
                System.out.println("Opening scoreboard...");
                break;
            case "EXIT":
                System.exit(0);
                break;
        }
    }

    // ===================== SCREEN TOGGLE =====================

    private void toggleScreenMode() {
        if (isFullScreen) {
            gd.setFullScreenWindow(null);
            frame.dispose();
            frame.setUndecorated(false);
            frame.setSize(1024, 768);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            isFullScreen = false;
        } else {
            frame.dispose();
            frame.setUndecorated(true);
            frame.setVisible(true);
            gd.setFullScreenWindow(frame);
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

    // ===================== EASING & LERP =====================

    private float easeInOut(float t) {
        return t < 0.5f ? 2 * t * t : -1 + (4 - 2 * t) * t;
    }

    private float easeOut(float t) {
        return 1 - (1 - t) * (1 - t);
    }

    private float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    // ===================== BACKGROUND PANEL =====================

    class BackgroundPanel extends JPanel {
        private BufferedImage bgImage;
        private float alpha = 1.0f;

        public BackgroundPanel() {
            bgImage = loadImage("/resources/background.png");
        }

        public void setAlpha(float alpha) { this.alpha = alpha; }

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
                g2.setPaint(new GradientPaint(0, 0, new Color(120, 10, 10), 0, getHeight(), new Color(60, 5, 5)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
            g2.dispose();
        }
    }
}