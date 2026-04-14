package ConquestPkg;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MainMenu {

    private JFrame frame;
    private boolean isFullScreen = true;
    private GraphicsDevice gd;

    public MainMenu(JFrame existingFrame) {
        this.frame = existingFrame;
        gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        showTitleCard();
    }

    // ===================== TITLE CARD =====================

    private void showTitleCard() {
        BackgroundPanel titlePanel = new BackgroundPanel();
        titlePanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel welcomeLabel = new JLabel("Welcome, " + GameData.playerName, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 48));
        welcomeLabel.setForeground(new Color(245, 230, 210));
        gbc.gridy = 0;
        titlePanel.add(welcomeLabel, gbc);

        // Logo image
        JLabel logoLabel = buildLogoLabel(450);
        gbc.gridy = 1;
        titlePanel.add(logoLabel, gbc);

        frame.setContentPane(titlePanel);
        frame.revalidate();
        frame.repaint();

        // After 2.5s, start shrink + pan up transition
        Timer holdTimer = new Timer(2500, e -> animateTitleToHeader(titlePanel, welcomeLabel, logoLabel));
        holdTimer.setRepeats(false);
        holdTimer.start();
    }

    // ===================== SHRINK + PAN UP ANIMATION =====================

    private void animateTitleToHeader(BackgroundPanel titlePanel, JLabel welcomeLabel, JLabel logoLabel) {
        // Get screen center
        int screenW = frame.getWidth();
        int screenH = frame.getHeight();

        // We'll animate using absolute positioning on a layered pane approach
        // Switch to null layout for animation
        titlePanel.setLayout(null);

        // Place welcome label
        Dimension wSize = welcomeLabel.getPreferredSize();
        welcomeLabel.setBounds(
            (screenW - wSize.width) / 2,
            screenH / 2 - 180,
            wSize.width, wSize.height
        );

        // Place logo label
        int logoSize = 320;
        logoLabel.setBounds(
            (screenW - logoSize) / 2,
            screenH / 2 - 80,
            logoSize, logoSize
        );

        titlePanel.add(welcomeLabel);
        titlePanel.add(logoLabel);
        titlePanel.revalidate();
        titlePanel.repaint();

        // Target positions (top area — where header will be)
        int targetLogoSize = 90;
        int targetLogoX = (screenW - targetLogoSize) / 2;
        int targetLogoY = 50;

        // Welcome label target
        Dimension wSizeSmall = new Dimension(wSize.width, wSize.height);
        int targetWX = (screenW - wSize.width) / 2;
        int targetWY = 10;

        // Animation state
        float[] progress = {0.0f};
        int startLogoX = (screenW - logoSize) / 2;
        int startLogoY = screenH / 2 - 80;
        int startWX = (screenW - wSize.width) / 2;
        int startWY = screenH / 2 - 180;

        Timer shrinkTimer = new Timer(16, null);
        shrinkTimer.addActionListener(e -> {
            progress[0] += 0.018f;
            if (progress[0] >= 1.0f) {
                progress[0] = 1.0f;
                shrinkTimer.stop();
                morphToMainMenu();
            }

            float t = easeInOut(progress[0]);

            // Interpolate logo position and size
            int curLogoSize = (int) lerp(logoSize, targetLogoSize, t);
            int curLogoX = (int) lerp(startLogoX, targetLogoX, t);
            int curLogoY = (int) lerp(startLogoY, targetLogoY, t);

            // Interpolate welcome label position
            int curWX = (int) lerp(startWX, targetWX, t);
            int curWY = (int) lerp(startWY, targetWY, t);

            // Resize logo image dynamically
            logoLabel.setIcon(scaleLogoIcon(curLogoSize));
            logoLabel.setBounds(curLogoX, curLogoY, curLogoSize, curLogoSize);

            // Scale welcome font
            float fontSize = lerp(48f, 22f, t);
            welcomeLabel.setFont(new Font("Serif", Font.PLAIN, (int) fontSize));
            Dimension newWSize = welcomeLabel.getPreferredSize();
            int centeredWX = (screenW - newWSize.width) / 2;
            welcomeLabel.setBounds(centeredWX, curWY, newWSize.width, newWSize.height);

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
            alpha[0] += 0.03f;
            if (alpha[0] >= 1.0f) {
                alpha[0] = 1.0f;
                fadeTimer.stop();
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

    private JPanel buildMenuHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        // Toggle button — top right
        JPanel toggleWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toggleWrapper.setOpaque(false);
        toggleWrapper.add(buildToggleButton());
        header.add(toggleWrapper, BorderLayout.NORTH);

        // Welcome + logo — centered
        JPanel logoArea = new JPanel(new GridBagLayout());
        logoArea.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel welcomeLabel = new JLabel("Welcome, " + GameData.playerName, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.PLAIN, 22));
        welcomeLabel.setForeground(new Color(245, 230, 210));
        gbc.gridy = 0;
        logoArea.add(welcomeLabel, gbc);

        gbc.gridy = 1;
        logoArea.add(buildLogoLabel(140), gbc);

        header.add(logoArea, BorderLayout.CENTER);
        return header;
    }

    // ===================== ANIMATED BUTTON PANEL =====================

    private JPanel buildAnimatedButtonPanel() {
        // Outer panel clips the buttons during pan-in
        JPanel clipPanel = new JPanel(null) {
            @Override
            public boolean isOptimizedDrawingEnabled() {
                return false;
            }
        };
        clipPanel.setOpaque(false);

        JButton playBtn  = buildMenuButton("PLAY");
        JButton scoreBtn = buildMenuButton("SCORE");
        JButton exitBtn  = buildMenuButton("EXIT");

        int btnW = 280, btnH = 120;

        clipPanel.add(playBtn);
        clipPanel.add(scoreBtn);
        clipPanel.add(exitBtn);

        // Animate after panel is laid out
        SwingUtilities.invokeLater(() -> {
            int panelW = clipPanel.getWidth();
            int panelH = clipPanel.getHeight();

            // Center Y for all buttons
            int centerY = (panelH - btnH) / 2;
            int centerX = (panelW - btnW) / 2;

            // Starting positions (off-screen)
            int[] playStartX   = {-btnW};               // from left
            int[] scoreStartY  = {panelH};               // from bottom
            int[] exitStartX   = {panelW};               // from right

            // Target positions
            int playTargetX  = centerX - btnW - 40;
            int scoreTargetX = centerX;
            int exitTargetX  = centerX + btnW + 40;
            int targetY      = centerY;

            // Set initial off-screen positions
            playBtn.setBounds(playStartX[0],  centerY,  btnW, btnH);
            scoreBtn.setBounds(scoreTargetX,  scoreStartY[0], btnW, btnH);
            exitBtn.setBounds(exitStartX[0],  centerY,  btnW, btnH);

            float[] progress = {0.0f};
            Timer panTimer = new Timer(16, null);
            panTimer.addActionListener(e -> {
                progress[0] += 0.025f;
                if (progress[0] >= 1.0f) {
                    progress[0] = 1.0f;
                    panTimer.stop();
                }

                float t = easeOut(progress[0]);

                playBtn.setBounds(
                    (int) lerp(playStartX[0], playTargetX, t), targetY, btnW, btnH
                );
                scoreBtn.setBounds(
                    scoreTargetX, (int) lerp(scoreStartY[0], targetY, t), btnW, btnH
                );
                exitBtn.setBounds(
                    (int) lerp(exitStartX[0], exitTargetX, t), targetY, btnW, btnH
                );

                clipPanel.repaint();
            });

            // Small delay so panel has time to size itself
            Timer startDelay = new Timer(100, ev -> panTimer.start());
            startDelay.setRepeats(false);
            startDelay.start();
        });

        return clipPanel;
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

        btn.setFont(new Font("SansSerif", Font.BOLD, 28));
        btn.setForeground(new Color(245, 230, 210));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        startHoverAnimation(btn);
        btn.addActionListener(e -> handleMenuAction(label));

        return btn;
    }

    // ===================== LOGO HELPERS =====================

    private ImageIcon scaleLogoIcon(int maxSize) {
    try {
        BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/resources/conquest_logo.png"));
        if (img == null) return null;
        
        int origW = img.getWidth();
        int origH = img.getHeight();
        float aspect = (float) origW / origH;
        
        // Calculate dimensions that fit within maxSize while preserving aspect ratio
        int newW, newH;
        if (aspect > 1) { // Wider than tall
            newW = maxSize;
            newH = Math.round(maxSize / aspect);
        } else { // Taller than wide or square
            newH = maxSize;
            newW = Math.round(maxSize * aspect);
        }
        
        Image scaled = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    } catch (Exception e) {
        return null;
    }
}

    private JLabel buildLogoLabel(int size) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon icon = scaleLogoIcon(size);
        if (icon != null) {
            label.setIcon(icon);
        } else {
            // Fallback to text logo if image missing
            label.setText("ConQuest");
            label.setFont(new Font("Serif", Font.BOLD, size / 4));
            label.setForeground(new Color(245, 230, 210));
        }
        label.setPreferredSize(new Dimension(size, size));
        return label;
    }

    // ===================== CONQUEST TEXT LOGO (fallback) =====================

    private JPanel buildConQuestLogo(int fontSize) {
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        logoPanel.setOpaque(false);

        JLabel conLabel = new JLabel("Con");
        conLabel.setFont(new Font("Serif", Font.BOLD, fontSize));
        conLabel.setForeground(new Color(60, 10, 10));

        JLabel qLabel = new JLabel("Q");
        qLabel.setFont(new Font("Serif", Font.BOLD, fontSize));
        qLabel.setForeground(new Color(245, 230, 210));

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
        float speed = 0.005f;
        Timer hoverTimer = new Timer(10, e -> {
            float target = btn.getModel().isRollover() ? 1.05f : 1.0f;
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

    // ===================== EASING & LERP HELPERS =====================

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
                g2.setPaint(new GradientPaint(0, 0, new Color(120, 10, 10), 0, getHeight(), new Color(60, 5, 5)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
            g2.dispose();
        }
    }
}