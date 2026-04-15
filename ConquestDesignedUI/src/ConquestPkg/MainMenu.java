package ConquestPkg;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MainMenu {

    private JFrame frame;
    private boolean isFullScreen = false;
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

    private void installResizeListener() {
        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            private Timer debounce = null;
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                if (!mainMenuShowing) return;
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

    // ===================== LOGO LABEL =====================

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

    private void showTitleCard() {
        BackgroundPanel titlePanel = new BackgroundPanel();
        titlePanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel welcomeLabel = new JLabel("Welcome, " + GameData.playerName, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 18, 0);
        titlePanel.add(welcomeLabel, gbc);

        int startLogoW = 420;
        JLabel logoLabel = buildLogoLabel(startLogoW);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        titlePanel.add(logoLabel, gbc);

        frame.setContentPane(titlePanel);
        frame.revalidate();
        frame.repaint();

        // FIX 3: let the layout settle fully before we snapshot positions
        SwingUtilities.invokeLater(() -> {
            Timer holdTimer = new Timer(2500, e ->
                animateTitleToHeader(titlePanel, welcomeLabel, logoLabel, startLogoW)
            );
            holdTimer.setRepeats(false);
            holdTimer.start();
        });
    }

    // ===================== SHRINK + PAN UP ANIMATION =====================
    // FIX 3: Snapshot the ACTUAL rendered positions from the laid-out components,
    //         so the first frame of animation is pixel-identical to the title card.

    private void animateTitleToHeader(BackgroundPanel titlePanel, JLabel welcomeLabel, JLabel logoLabel, int startLogoW) {
        int screenW = frame.getWidth();
        int screenH = frame.getHeight();

        // Snapshot real positions BEFORE switching to null layout
        Point wPt   = SwingUtilities.convertPoint(welcomeLabel.getParent(), welcomeLabel.getLocation(), titlePanel);
        Point lgPt  = SwingUtilities.convertPoint(logoLabel.getParent(),    logoLabel.getLocation(),    titlePanel);

        int startWX    = wPt.x;
        int startWY    = wPt.y;
        int startLogoX = lgPt.x;
        int startLogoY = lgPt.y;
        int startLogoH = logoLabel.getHeight();

        // Target sizes
        int targetLogoW = 320;
        int targetLogoH = (logoImage != null)
            ? (int) ((double) logoNaturalH / logoNaturalW * targetLogoW)
            : targetLogoW / 3;

        Dimension wDim = welcomeLabel.getPreferredSize();

        // Target positions — just below toggle row (~36px tall)
        int topPad      = 44;
        int targetWY    = topPad;
        int targetWX    = (screenW - wDim.width) / 2;
        int targetLogoY = targetWY + wDim.height + 14;
        int targetLogoX = (screenW - targetLogoW) / 2;

        // Switch to null layout — place components at their snapshotted positions first
        titlePanel.setLayout(null);
        welcomeLabel.setBounds(startWX, startWY, wDim.width, wDim.height);
        logoLabel.setBounds(startLogoX, startLogoY, startLogoW, startLogoH);
        titlePanel.add(welcomeLabel);
        titlePanel.add(logoLabel);
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

            // Keep welcome label always horizontally centered
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

    private JPanel buildMenuHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);

        JPanel toggleRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        toggleRow.setOpaque(false);
        toggleRow.add(buildToggleButton());
        header.add(toggleRow);

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
        String[] descs  = {
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

    // FIX 1: Score button lag — start position is just off the bottom edge (panelH + 1),
    //         animation stops cleanly, no double-set of final bounds.
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

        // Keep exact off-screen starts as float arrays so lerp is clean
        final float startX0 = -btnW;
        final float startY1 = panelH + 1;  // FIX 1: just 1px off bottom, not a full extra panelH
        final float startX2 = panelW;

        buttons[0].putClientProperty("restingY", centerY);
        buttons[1].putClientProperty("restingY", centerY);
        buttons[2].putClientProperty("restingY", centerY);

        // Park buttons at start positions, all invisible-ish until timer fires
        buttons[0].setBounds((int) startX0, centerY,      btnW, btnH);
        buttons[1].setBounds(scoreTargetX,  (int) startY1, btnW, btnH);
        buttons[2].setBounds((int) startX2, centerY,      btnW, btnH);

        float[] progress = {0.0f};

        Timer panTimer = new Timer(16, null);
        panTimer.addActionListener(e -> {
            progress[0] += 0.025f;
            boolean done = progress[0] >= 1.0f;
            if (done) progress[0] = 1.0f;

            float t = easeOut(progress[0]);

            buttons[0].setBounds((int) lerp(startX0, playTargetX,  t), centerY, btnW, btnH);
            buttons[1].setBounds(scoreTargetX, (int) lerp(startY1, centerY,     t), btnW, btnH);
            buttons[2].setBounds((int) lerp(startX2, exitTargetX,  t), centerY, btnW, btnH);

            clipPanel.repaint();

            if (done) ((Timer) e.getSource()).stop();
        });
        panTimer.start();
    }

    // ===================== MENU BUTTON WITH HOVER EXPAND =====================
    // FIX 4: 500 ms hover delay before expansion starts.
    //         A pending timer is cancelled if the mouse leaves before it fires.

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

                float descAlpha   = getDescAlpha();
                float expandProg  = getExpandProgress();

                g2.setFont(new Font("Arial", Font.BOLD, 34));
                g2.setColor(new Color(245, 230, 210));
                FontMetrics fm = g2.getFontMetrics();
                int textX = (w - fm.stringWidth(label)) / 2;

                int centeredY = (h - fm.getHeight()) / 2 + fm.getAscent();
                int topY      = 36 + fm.getAscent();
                int textY     = (int) lerp(centeredY, topY, expandProg);
                g2.drawString(label, textX, textY);

                if (descAlpha > 0.01f) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, descAlpha));
                    g2.setFont(new Font("Arial", Font.PLAIN, 19));
                    FontMetrics fd = g2.getFontMetrics();

                    int hMargin = 48;
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

            private float getDescAlpha()     { Object p = getClientProperty("descAlpha");     return (p instanceof Float) ? (Float) p : 0f; }
            private float getExpandProgress(){ Object p = getClientProperty("expandProgress"); return (p instanceof Float) ? (Float) p : 0f; }
        };

        btn.setFont(new Font("Arial", Font.BOLD, 34));
        btn.setForeground(new Color(245, 230, 210));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> handleMenuAction(label));

        float[] currentH       = {collapsedH};
        boolean[] expanding    = {false};   // FIX 4: true once the 500ms delay has fired
        Timer[]   delayTimer   = {null};    // FIX 4: holds the pending delay so we can cancel it

        // Main animation ticker — runs always, but only expands when expanding[0] is true
        Timer hoverTimer = new Timer(16, e -> {
            boolean hovered = btn.getModel().isRollover();

            // Determine target based on hover state AND whether delay has elapsed
            float targetH = (hovered && expanding[0]) ? expandedH : collapsedH;
            float speed   = 6f;

            if (Math.abs(currentH[0] - targetH) < speed) {
                currentH[0] = targetH;
            } else {
                currentH[0] += currentH[0] < targetH ? speed : -speed;
            }

            float expandProgress = (currentH[0] - collapsedH) / (float)(expandedH - collapsedH);
            float descAlpha      = Math.max(0f, (expandProgress - 0.5f) * 2f);
            btn.putClientProperty("expandProgress", expandProgress);
            btn.putClientProperty("descAlpha", descAlpha);

            Object ry = btn.getClientProperty("restingY");
            if (ry instanceof Integer) {
                int restingY     = (Integer) ry;
                int centerOfRest = restingY + collapsedH / 2;
                int newH         = (int) currentH[0];
                int newY         = centerOfRest - newH / 2;
                btn.setBounds(btn.getBounds().x, newY, btnW, newH);
                if (btn.getParent() != null) btn.getParent().repaint();
            }
        });
        hoverTimer.start();

        // FIX 4: Mouse listeners manage the 500ms delay
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                // Cancel any old pending timer
                if (delayTimer[0] != null && delayTimer[0].isRunning()) delayTimer[0].stop();
                expanding[0] = false;
                // Start 500ms delay
                delayTimer[0] = new Timer(500, ev -> {
                    expanding[0] = true;
                    ((Timer) ev.getSource()).stop();
                });
                delayTimer[0].setRepeats(false);
                delayTimer[0].start();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                // Cancel delay — button collapses immediately if mouse leaves before 500ms
                if (delayTimer[0] != null && delayTimer[0].isRunning()) delayTimer[0].stop();
                expanding[0] = false;
            }
        });

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
        if (!isFullScreen) {
            frame.dispose();
            frame.setUndecorated(true);
            frame.setVisible(true);
            gd.setFullScreenWindow(frame);
            isFullScreen = true;
        } else {
            gd.setFullScreenWindow(null);
            frame.dispose();
            frame.setUndecorated(false);
            frame.setSize(1440, 900);   // FIX 2: wider window
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            isFullScreen = false;
        }

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