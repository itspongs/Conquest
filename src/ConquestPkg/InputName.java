package ConquestPkg;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;

public class InputName extends JFrame {

    private boolean isFullScreen = true;
    private GraphicsDevice gd;
    private JTextField nameField;
    private JButton proceedBtn;

    public InputName() {
        setupWindow();
        buildUI();
        setVisible(true);
    }

    // ===================== WINDOW SETUP =====================

    private void setupWindow() {
        setTitle("Conquest");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(this);
    }

    // ===================== BUILD UI =====================

    private void buildUI() {
        BackgroundPanel outerPanel = new BackgroundPanel();
        outerPanel.setLayout(new BorderLayout());
        setContentPane(outerPanel);

        outerPanel.add(buildTopPanel(), BorderLayout.NORTH);
        outerPanel.add(buildCenterPanel(), BorderLayout.CENTER);
    }

    // ===================== TOP PANEL =====================

    private JPanel buildTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        topPanel.add(buildToggleButton());
        return topPanel;
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

    // ===================== CENTER PANEL =====================

    private JPanel buildCenterPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        // Add components in order
        gbc.gridy = 0; centerPanel.add(buildTitle(), gbc);
        gbc.gridy = 1; centerPanel.add(buildNameField(), gbc);
        gbc.gridy = 2; centerPanel.add(buildProceedButton(), gbc);

        return centerPanel;
    }

    private JLabel buildTitle() {
        JLabel title = new JLabel("INPUT PLAYER NAME", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(new Color(245, 230, 210));
        return title;
    }

    // ===================== NAME FIELD =====================

    private JTextField buildNameField() {
        nameField = new JTextField("Name", 20) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(220, 185, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(180, 100, 100));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30);
                g2.dispose();
            }
        };

        nameField.setPreferredSize(new Dimension(500, 55));
        nameField.setFont(new Font("Serif", Font.PLAIN, 22));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setOpaque(false);
        nameField.setForeground(new Color(80, 30, 30));
        nameField.setCaretColor(new Color(80, 30, 30));
        nameField.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        nameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                resetFieldBorder();
                if (nameField.getText().equals("Name")) nameField.setText("");
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (nameField.getText().isEmpty()) nameField.setText("Name");
            }
        });

        return nameField;
    }

    // ===================== PROCEED BUTTON =====================

    private JButton buildProceedButton() {
        proceedBtn = new JButton("PROCEED") {
            @Override
            protected void paintComponent(Graphics g) {
                float s = getScale();
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                // Scale from center
                g2.translate(w / 2.0, h / 2.0);
                g2.scale(s, s);
                g2.translate(-w / 2.0, -h / 2.0);

                // Draw button background
                g2.setColor(getModel().isRollover() ? new Color(90, 15, 15) : new Color(60, 10, 10));
                g2.fillRoundRect(10, 5, w - 20, h - 10, 35, 35);

                // Draw text
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
                g2.setColor(new Color(180, 80, 80));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(11, 6, w - 22, h - 12, 35, 35);
                g2.dispose();
            }

            // Helper to read scale from client property
            private float getScale() {
                Object prop = getClientProperty("scale");
                return (prop instanceof Float) ? (Float) prop : 1.0f;
            }
        };

        proceedBtn.setPreferredSize(new Dimension(320, 65));
        proceedBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        proceedBtn.setForeground(Color.WHITE);
        proceedBtn.setContentAreaFilled(false);
        proceedBtn.setBorderPainted(false);
        proceedBtn.setFocusPainted(false);
        proceedBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        proceedBtn.addActionListener(e -> handleProceed());
        startHoverAnimation();

        return proceedBtn;
    }

    // ===================== ACTIONS & LOGIC =====================

    private void handleProceed() {
        String name = nameField.getText().trim();
        if (isValidName(name)) {
            GameData.playerName = name;
            dispose(); // close InputName window
            new MainMenu(); // open MainMenu
        } else {
            highlightFieldError();
        }
    }

    private boolean isValidName(String name) {
        return !name.isEmpty() && !name.equals("Name");
    }

    private void highlightFieldError() {
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.RED, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
    }

    private void resetFieldBorder() {
        nameField.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    private void startHoverAnimation() {
        float[] scale = {1.0f};
        float hoverScale = 1.05f;
        float normalScale = 1.0f;
        float speed = 0.005f;

        Timer hoverTimer = new Timer(10, e -> {
            float target = proceedBtn.getModel().isRollover() ? hoverScale : normalScale;

            if (Math.abs(scale[0] - target) < speed) {
                scale[0] = target;
            } else {
                scale[0] += scale[0] < target ? speed : -speed;
            }

            proceedBtn.putClientProperty("scale", scale[0]);
            proceedBtn.repaint();
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

    // ===================== BACKGROUND PANEL =====================

    class BackgroundPanel extends JPanel {
        private BufferedImage bgImage;

        public BackgroundPanel() {
            bgImage = loadImage("/resources/background.png");
        }

        private BufferedImage loadImage(String path) {
            try {
                return ImageIO.read(getClass().getResourceAsStream(path));
            } catch (Exception e) {
                return null; // fallback to gradient
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bgImage != null) {
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                drawFallbackGradient(g);
            }
        }

        private void drawFallbackGradient(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(
                0, 0, new Color(120, 10, 10),
                0, getHeight(), new Color(60, 5, 5)
            ));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}