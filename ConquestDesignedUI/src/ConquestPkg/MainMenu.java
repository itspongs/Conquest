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

        SwingUtilities.invokeLater(() -> {
            Timer holdTimer = new Timer(2500, e ->
                animateTitleToHeader(titlePanel, welcomeLabel, logoLabel, startLogoW)
            );
            holdTimer.setRepeats(false);
            holdTimer.start();
        });
    }

    // ===================== SHRINK + PAN UP ANIMATION =====================

    private void animateTitleToHeader(BackgroundPanel titlePanel, JLabel welcomeLabel, JLabel logoLabel, int startLogoW) {
        int screenW = frame.getWidth();

        Point wPt  = SwingUtilities.convertPoint(welcomeLabel.getParent(), welcomeLabel.getLocation(), titlePanel);
        Point lgPt = SwingUtilities.convertPoint(logoLabel.getParent(),    logoLabel.getLocation(),    titlePanel);

        int startWX    = wPt.x;
        int startWY    = wPt.y;
        int startLogoX = lgPt.x;
        int startLogoY = lgPt.y;
        int startLogoH = logoLabel.getHeight();

        int targetLogoW = 320;
        int targetLogoH = (logoImage != null)
            ? (int) ((double) logoNaturalH / logoNaturalW * targetLogoW)
            : targetLogoW / 3;

        Dimension wDim = welcomeLabel.getPreferredSize();

        int topPad       = 44;
        int targetWY     = topPad;
        int targetWX     = (screenW - wDim.width) / 2;
        int targetLogoY2 = targetWY + wDim.height + 14;
        int targetLogoX2 = (screenW - targetLogoW) / 2;

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
            int curLogoX = (int) lerp(startLogoX, targetLogoX2, t);
            int curLogoY = (int) lerp(startLogoY, targetLogoY2, t);
            int curWX    = (screenW - wDim.width) / 2;
            int curWY    = (int) lerp(startWY, targetWY, t);

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
        // KEY FIX: Override paintComponent to set a hard clip rect equal to the
        // panel's own bounds. This makes children positioned outside (e.g. SCORE
        // starting below the panel) truly invisible until they slide into view.
        JPanel clipPanel = new JPanel(null) {
            @Override
            public boolean isOptimizedDrawingEnabled() { return false; }

            @Override
            protected void paintComponent(Graphics g) {
                // Clip painting strictly to this panel's visible area so that any
                // button whose current bounds are outside the panel is not rendered.
                g.setClip(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
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
        }

        // Park ALL buttons far off-screen BEFORE adding them to the panel.
        // This prevents a single-frame flash where the SCORE button appears at (0,0)
        // before the ComponentListener fires and repositions it.
        for (JButton btn : buttons) {
            btn.setBounds(-9999, -9999, 340, 150);
        }

        for (JButton btn : buttons) {
            clipPanel.add(btn);
        }

        // Use a ComponentListener so we measure the panel after it gets its real size.
        clipPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            private boolean animated = false;
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                if (!animated && clipPanel.getWidth() > 0 && clipPanel.getHeight() > 0) {
                    animated = true;
                    // Slight delay so layout settles fully before we snapshot sizes.
                    Timer startDelay = new Timer(30, ev -> {
                        ((Timer) ev.getSource()).stop();
                        animateButtonsIn(clipPanel, buttons);
                    });
                    startDelay.setRepeats(false);
                    startDelay.start();
                }
            }
        });

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

        final float startX0 = -btnW;   // PLAY:  off left edge
        final float startX2 = panelW;  // EXIT:  off right edge

        // Store resting Y so hover-expand knows its anchor.
        buttons[0].putClientProperty("restingY", centerY);
        buttons[1].putClientProperty("restingY", centerY);
        buttons[2].putClientProperty("restingY", centerY);

        // PLAY and EXIT start off-screen; SCORE starts at its final position, fully transparent.
        buttons[0].setBounds((int) startX0, centerY, btnW, btnH);
        buttons[1].setBounds(scoreTargetX,  centerY, btnW, btnH);
        buttons[2].setBounds((int) startX2, centerY, btnW, btnH);

        // SCORE fades in via a client property read inside paintComponent.
        buttons[1].putClientProperty("fadeAlpha", 0.0f);
        clipPanel.repaint();

        float[] progress = {0.0f};

        Timer panTimer = new Timer(16, null);
        panTimer.addActionListener(e -> {
            progress[0] += 0.025f;
            boolean done = progress[0] >= 1.0f;
            if (done) progress[0] = 1.0f;

            float t = easeOut(progress[0]);

            // PLAY slides in from left
            buttons[0].setBounds(
                (int) lerp(startX0, playTargetX, t),
                centerY, btnW, btnH);

            // SCORE fades in at its final position
            buttons[1].putClientProperty("fadeAlpha", t);
            buttons[1].setBounds(scoreTargetX, centerY, btnW, btnH);

            // EXIT slides in from right
            buttons[2].setBounds(
                (int) lerp(startX2, exitTargetX, t),
                centerY, btnW, btnH);

            clipPanel.repaint();

            if (done) {
                buttons[1].putClientProperty("fadeAlpha", null); // clear so normal paint takes over
                ((Timer) e.getSource()).stop();
            }
        });
        panTimer.start();
    }

    // ===================== MENU BUTTON =====================

    private JButton buildMenuButton(String label, String description) {
        final int collapsedH = 150;
        final int expandedH  = 260;
        final int btnW       = 340;

        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                // If a fadeAlpha is set (during intro animation), wrap everything in that alpha.
                Object fa = getClientProperty("fadeAlpha");
                Graphics2D g2 = (Graphics2D) g.create();
                if (fa instanceof Float) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (Float) fa));
                }
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                // Button background
                g2.setColor(getModel().isRollover() ? new Color(210, 155, 145) : new Color(190, 130, 120));
                g2.fillRoundRect(10, 5, w - 20, h - 10, 25, 25);

                float descAlpha  = getDescAlpha();
                float expandProg = getExpandProgress();

                // Label text — MAROON
                g2.setFont(new Font("Arial", Font.BOLD, 34));
                g2.setColor(new Color(128, 0, 0));
                FontMetrics fm = g2.getFontMetrics();
                int textX     = (w - fm.stringWidth(label)) / 2;
                int centeredY = (h - fm.getHeight()) / 2 + fm.getAscent();
                int topY      = 36 + fm.getAscent();
                int textY     = (int) lerp(centeredY, topY, expandProg);
                g2.drawString(label, textX, textY);

                // Description text — MAROON, fades in as button expands
                if (descAlpha > 0.01f) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, descAlpha));
                    g2.setFont(new Font("Arial", Font.PLAIN, 19));
                    g2.setColor(new Color(128, 0, 0));
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
                Object fa = getClientProperty("fadeAlpha");
                if (fa instanceof Float) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (Float) fa));
                }
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
        btn.setForeground(new Color(128, 0, 0)); // maroon
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> handleMenuAction(label));

        float[] currentH = {collapsedH};

        Timer hoverTimer = new Timer(16, e -> {
            boolean hovered  = btn.getModel().isRollover();
            float   targetH  = hovered ? expandedH : collapsedH;
            float   speed    = 12f;

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
            frame.setSize(1440, 900);
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