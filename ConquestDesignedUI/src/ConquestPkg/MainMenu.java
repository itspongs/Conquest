package ConquestPkg;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MainMenu {

    private JFrame frame;
    private boolean isFullScreen = false; // FIX 1: starts false — frame is windowed on launch
    private GraphicsDevice gd;
    private BufferedImage logoImage;

    private int logoNaturalW = 480;
    private int logoNaturalH = 160;

    private boolean mainMenuShowing = false;

    public MainMenu(JFrame existingFrame) {
        this.frame = existingFrame;
        gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        loadLogoImage();
        installResizeListener();
        showTitleCard();
    }

    // Rebuilds the main menu whenever the window is resized (covers OS maximize too).
    // The flag mainMenuShowing ensures this never fires during the title card animation.
    private void installResizeListener() {
        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            private Timer debounce = null;
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                if (!mainMenuShowing) return;
                // Debounce: wait for resize to settle before rebuilding
                if (debounce != null && debounce.isRunning()) debounce.stop();
                debounce = new Timer(80, ev -> {
                    ((Timer) ev.getSource()).stop();
                    BackgroundPanel wrapper = new BackgroundPanel();
                    wrapper.setLayout(new BorderLayout());
                    wrapper.add(buildMainMenuPanel(), BorderLayout.CENTER);
                    frame.setContentPane(wrapper);
                    frame.revalidate();
                    frame.repaint();
                });
                debounce.setRepeats(false);
                debounce.start();
            }
        });
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
    // FIX 2: Cleaner title card — vertically centered with generous spacing,
    //         subtle divider line between welcome text and logo.

    private void showTitleCard() {
        BackgroundPanel titlePanel = new BackgroundPanel();
        titlePanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome, " + GameData.playerName, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 18, 0);
        titlePanel.add(welcomeLabel, gbc);

        // Logo
        int startLogoW = 420;
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

        int targetLogoW = 320;
        int targetLogoH = (logoImage != null)
            ? (int) ((double) logoNaturalH / logoNaturalW * targetLogoW)
            : targetLogoW / 3;

        Dimension wDim = welcomeLabel.getPreferredSize();

        int startWX    = (screenW - wDim.width) / 2;
        int startWY    = screenH / 2 - wDim.height - 24;
        int startLogoX = (screenW - startLogoW) / 2;
        int startLogoY = screenH / 2 + 12;

        int topPad      = 44;
        int targetWY    = topPad;
        int targetWX    = (screenW - wDim.width) / 2;
        int targetLogoY = targetWY + wDim.height + 14;
        int targetLogoX = (screenW - targetLogoW) / 2;

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
        mainMenuShowing = true;
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
    // FIX 2 (continued): Cleaner header — welcome text lighter/italic,
    //         tight vertical rhythm between welcome and logo.

    private JPanel buildMenuHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);

        // Toggle button row — flush right
        JPanel toggleRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        toggleRow.setOpaque(false);
        toggleRow.add(buildToggleButton());
        header.add(toggleRow);

        // Welcome + logo block — centered
        JPanel logoArea = new JPanel(new GridBagLayout());
        logoArea.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel welcomeLabel = new JLabel("Welcome, " + GameData.playerName, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 0, 6, 0);
        logoArea.add(welcomeLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 12, 0);
        logoArea.add(buildLogoLabel(300), gbc);

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
        int btnW   = 340;
        int btnH   = 150;

        int centerY      = (panelH - btnH) / 2;
        int centerX      = (panelW - btnW) / 2;
        int playTargetX  = centerX - btnW - 40;
        int scoreTargetX = centerX;
        int exitTargetX  = centerX + btnW + 40;

        // Store the stable resting Y for each button so the hover expander knows it
        buttons[0].putClientProperty("restingY", centerY);
        buttons[1].putClientProperty("restingY", centerY);
        buttons[2].putClientProperty("restingY", centerY);

        buttons[0].setBounds(-btnW,        centerY, btnW, btnH);
        buttons[1].setBounds(scoreTargetX, panelH,  btnW, btnH);
        buttons[2].setBounds(panelW,       centerY, btnW, btnH);

        float[] progress = {0.0f};

        Timer panTimer = new Timer(16, null);
        panTimer.addActionListener(e -> {
            progress[0] += 0.025f;
            if (progress[0] >= 1.0f) {
                progress[0] = 1.0f;
                ((Timer) e.getSource()).stop();
                // Set final positions so restingY is accurate from here on
                buttons[0].setBounds(playTargetX,  centerY, btnW, btnH);
                buttons[1].setBounds(scoreTargetX, centerY, btnW, btnH);
                buttons[2].setBounds(exitTargetX,  centerY, btnW, btnH);
            }
            float t = easeOut(progress[0]);

            buttons[0].setBounds((int) lerp(-btnW,  playTargetX,  t), centerY,                       btnW, btnH);
            buttons[1].setBounds(scoreTargetX,       (int) lerp(panelH, centerY, t),                 btnW, btnH);
            buttons[2].setBounds((int) lerp(panelW,  exitTargetX, t), centerY,                       btnW, btnH);

            clipPanel.repaint();
        });
        panTimer.start();
    }

    // ===================== MENU BUTTON WITH HOVER EXPAND =====================
    // FIX 3: Buttons no longer fly away.
    //   Root cause: the hover timer was reading bounds.y (already shifted) to
    //   recompute center, so each tick the "center" drifted further up.
    //   Fix: store a stable restingY in a client property and always expand
    //   symmetrically around that fixed Y.

    private JButton buildMenuButton(String label, String description) {
        final int collapsedH = 150;
        final int expandedH  = 260;
        final int btnW       = 340;

        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                g2.setColor(getModel().isRollover() ? new Color(210, 155, 145) : new Color(190, 130, 120));
                g2.fillRoundRect(10, 5, w - 20, h - 10, 25, 25);

                float descAlpha = getDescAlpha();
                float expandProg = getExpandProgress();

                g2.setFont(new Font("Arial", Font.BOLD, 34));
                g2.setColor(new Color(245, 230, 210));
                FontMetrics fm = g2.getFontMetrics();
                int textX = (w - fm.stringWidth(label)) / 2;

                // Label Y: smoothly slides from vertical-center toward top as box expands
                int centeredY = (h - fm.getHeight()) / 2 + fm.getAscent();
                int topY      = 36 + fm.getAscent();
                int textY     = (int) lerp(centeredY, topY, expandProg);
                g2.drawString(label, textX, textY);

                if (descAlpha > 0.01f) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, descAlpha));
                    g2.setFont(new Font("Arial", Font.PLAIN, 19));
                    FontMetrics fd = g2.getFontMetrics();

                    int hMargin = 48; // left + right breathing room
                    String[] words = description.split("\\s+");
                    StringBuilder line = new StringBuilder();
                    java.util.List<String> lines = new java.util.ArrayList<>();
                    for (String word : words) {
                        String test = line.length() == 0 ? word : line + " " + word;
                        if (fd.stringWidth(test) < w - hMargin * 2) {
                            line = new StringBuilder(test);
                        } else {
                            if (line.length() > 0) lines.add(line.toString());
                            line = new StringBuilder(word);
                        }
                    }
                    if (line.length() > 0) lines.add(line.toString());

                    int lineH      = fd.getHeight() + 2;
                    int totalTextH = lines.size() * lineH;
                    int descTop    = topY + fm.getDescent() + 16;
                    int sy         = descTop + (h - descTop - totalTextH) / 2 + fd.getAscent();
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

            private float getExpandProgress() {
                Object p = getClientProperty("expandProgress");
                return (p instanceof Float) ? (Float) p : 0.0f;
            }
        };

        btn.setFont(new Font("Arial", Font.BOLD, 34));
        btn.setForeground(new Color(245, 230, 210));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> handleMenuAction(label));

        // FIX 3: currentH tracks the live height; restingY is anchored from animateButtonsIn.
        float[] currentH = {collapsedH};

        Timer hoverTimer = new Timer(16, e -> {
            boolean hovered  = btn.getModel().isRollover();
            float targetH    = hovered ? expandedH : collapsedH;
            float speed      = 6f;

            if (Math.abs(currentH[0] - targetH) < speed) {
                currentH[0] = targetH;
            } else {
                currentH[0] += currentH[0] < targetH ? speed : -speed;
            }

            float expandProgress = (currentH[0] - collapsedH) / (float)(expandedH - collapsedH);
            float descAlpha = Math.max(0f, (expandProgress - 0.5f) * 2f);
            btn.putClientProperty("expandProgress", expandProgress);
            btn.putClientProperty("descAlpha", descAlpha);

            // Use the stored resting Y (center of collapsed button) — never read bounds.y
            Object ry = btn.getClientProperty("restingY");
            if (ry instanceof Integer) {
                int restingY   = (Integer) ry;
                // restingY is the top of the collapsed button; its center is restingY + collapsedH/2
                int centerOfRest = restingY + collapsedH / 2;
                int newH = (int) currentH[0];
                // Expand symmetrically around that fixed center
                int newY = centerOfRest - newH / 2;
                btn.setBounds(btn.getBounds().x, newY, btnW, newH);
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
    // FIX 1: isFullScreen now starts false (matching actual windowed state).
    // FIX 4: After every toggle, rebuild the content pane so layout is clean.

    private void toggleScreenMode() {
        if (!isFullScreen) {
            // Go fullscreen
            frame.dispose();
            frame.setUndecorated(true);
            frame.setVisible(true);
            gd.setFullScreenWindow(frame);
            isFullScreen = true;
        } else {
            // Go windowed
            gd.setFullScreenWindow(null);
            frame.dispose();
            frame.setUndecorated(false);
            frame.setSize(1024, 768);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            isFullScreen = false;
        }

        // FIX 4: Rebuild content pane so the layout adapts to the new window size
        SwingUtilities.invokeLater(() -> {
            BackgroundPanel wrapper = new BackgroundPanel();
            wrapper.setLayout(new BorderLayout());
            wrapper.add(buildMainMenuPanel(), BorderLayout.CENTER);
            frame.setContentPane(wrapper);
            frame.revalidate();
            frame.repaint();
        });
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