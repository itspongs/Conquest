package ConquestPkg;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainMenu {

    public MainMenu(JFrame frame, String playerName) {

        MusicPlayer.play("menu_music.wav");

        frame.getContentPane().removeAll();

        JPanel bg = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
       
                g2.setPaint(new GradientPaint(0, 0, new Color(139, 0, 0),
                                              getWidth(), getHeight(), new Color(80, 0, 0)));
                g2.fillRect(0, 0, getWidth(), getHeight());
    
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                float radius = Math.max(getWidth(), getHeight()) * 0.6f;
                float[] dist = {0.0f, 0.5f, 1.0f};
                Color[] colors = {
                    new Color(200, 50, 50, 80),   
                    new Color(139, 0, 0, 40),     
                    new Color(80, 0, 0, 0)        
                };
                RadialGradientPaint radial = new RadialGradientPaint(
                    cx, cy, radius, dist, colors
                );
                g2.setPaint(radial);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                g2.setPaint(new RadialGradientPaint(
                    cx, cy, radius * 1.2f,
                    new float[]{0.6f, 1.0f},
                    new Color[]{new Color(0,0,0,0), new Color(0,0,0,100)}
                ));
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                g2.dispose();
            }
        };
        bg.setOpaque(false);

        float[] floatOffset = { 0f }; 

        JPanel header = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int baseY = 110;
                int yOffset = (int) floatOffset[0];

                g2.setFont(new Font("Arial", Font.BOLD, 36));
                g2.setColor(new Color(255, 200, 200));
                FontMetrics fmW = g2.getFontMetrics();
                String welcomeStr = "Welcome " + playerName;
                int wx = (getWidth() - fmW.stringWidth(welcomeStr)) / 2;
                int wy = baseY + yOffset;
                g2.drawString(welcomeStr, wx, wy);

                g2.setFont(new Font("Arial", Font.BOLD, 80));
                g2.setColor(Color.WHITE);
                FontMetrics fmT = g2.getFontMetrics();
                String titleStr = "ConQuest!";
                int tx = (getWidth() - fmT.stringWidth(titleStr)) / 2;
                int ty = wy + 12 + fmT.getAscent();
                g2.drawString(titleStr, tx, ty);

                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(0, 220));

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonPanel.setOpaque(false);

        HoverCard playCard  = new HoverCard("PLAY",
                "<html><center>Test your knowledge!<br>Face the challenge and<br>prove your intellect now.</center></html>");
        HoverCard scoreCard = new HoverCard("SCORES",
                "<html><center>View the history<br>of your scores!</center></html>");
        HoverCard exitCard  = new HoverCard("EXIT",
                "<html><center>Why are you<br>Leaving dawg :(</center></html>");

        buttonPanel.add(playCard);
        buttonPanel.add(scoreCard);
        buttonPanel.add(exitCard);
        centerWrapper.add(buttonPanel);

        bg.add(header,        BorderLayout.NORTH);
        bg.add(centerWrapper, BorderLayout.CENTER);

        JLabel credits = new JLabel(
            "<html><div style='text-align:right; line-height:1.3'>" +
            "Group 5:<br>" +
            "Joseph James Pungyan<br>" +
            "Zeus Angelo Gavira<br>" +
            "Frances Vega<br>" +
            "Francis Lawrence De Jesus" +
            "</div></html>",
            JLabel.RIGHT
        );
        credits.setFont(new Font("Arial", Font.PLAIN, 11));
        credits.setForeground(new Color(255, 255, 255, 120));
        credits.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 16));
        
        JPanel bottomRight = new JPanel(new BorderLayout());
        bottomRight.setOpaque(false);
        bottomRight.add(credits, BorderLayout.EAST);
        
        bg.add(bottomRight, BorderLayout.SOUTH);

        frame.setContentPane(bg);

        playCard.onClick(() -> new QuizMenu(frame, playerName));
        scoreCard.onClick(() -> new QuizScores(frame, playerName));
        exitCard.onClick(() -> showExitConfirm(frame));

        frame.revalidate();
        frame.repaint();

        final JPanel headerRef = header;
        float[] time = { 0f };
        new Timer(30, e -> {
            time[0] += 0.08f;
            floatOffset[0] = (float) (Math.sin(time[0]) * 8);
            headerRef.repaint();
        }).start();
    }

    static class HoverCard extends JPanel {

        private static final int W        = 260;
        private static final int H_CLOSED = 100;
        private static final int H_OPEN   = 220;
        private static final int ARC      = 24;
        private static final int ANIM_MS  = 10;

        private final String label;
        private final JLabel descLabel;

        private int   curH  = H_CLOSED;
        private float alpha = 0f;
        private Timer anim;

        HoverCard(String label, String desc) {
            this.label = label;
            setLayout(null);
            setOpaque(false);
            setPreferredSize(new Dimension(W, H_CLOSED));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            descLabel = new JLabel(desc, JLabel.CENTER) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            descLabel.setForeground(Color.WHITE);
            descLabel.setHorizontalAlignment(JLabel.CENTER);
            descLabel.setVerticalAlignment(JLabel.CENTER);
            add(descLabel);

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { animateTo(true); }
                @Override public void mouseExited(MouseEvent e)  { animateTo(false); }
            });
        }

        void onClick(Runnable action) {
            addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { action.run(); }
            });
        }

        private void animateTo(boolean open) {
            if (anim != null && anim.isRunning()) anim.stop();
            int   targetH = open ? H_OPEN : H_CLOSED;
            float targetA = open ? 1f : 0f;

            anim = new Timer(ANIM_MS, null);
            anim.addActionListener(e -> {
                int hDiff = targetH - curH;
                int hStep = hDiff / 4;
                if (hStep == 0) hStep = Integer.signum(hDiff);
                curH += hStep;
                if (Math.abs(targetH - curH) <= 1) curH = targetH;

                float aDiff = targetA - alpha;
                float aStep = aDiff / 5f;
                if (Math.abs(aStep) < 0.015f) aStep = Math.signum(aDiff) * 0.015f;
                alpha += aStep;
                alpha = Math.max(0f, Math.min(1f, alpha));
                if (Math.abs(targetA - alpha) < 0.015f) alpha = targetA;

                if (curH == targetH && alpha == targetA) anim.stop();

                descLabel.setBounds(8, H_CLOSED + 4, W - 16, Math.max(0, curH - H_CLOSED - 12));
                setPreferredSize(new Dimension(W, curH));
                revalidate();
                repaint();
                Container p = getParent();
                if (p != null) { p.revalidate(); p.repaint(); }
            });
            anim.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            float a = alpha;
            Color base   = new Color(255, 255, 255, 30);
            Color hovered = new Color(210, 110, 110);
            int r  = (int)(base.getRed()   + (hovered.getRed()   - base.getRed())   * a);
            int gv = (int)(base.getGreen() + (hovered.getGreen() - base.getGreen()) * a);
            int b  = (int)(base.getBlue()  + (hovered.getBlue()  - base.getBlue())  * a);
            int al = (int)(30             + (255 - 30)                              * a);
            g2.setColor(new Color(r, gv, b, Math.min(255, al)));
            g2.fillRoundRect(0, 0, W, curH, ARC, ARC);

            g2.setColor(new Color(255, 255, 255, 80));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(1, 1, W - 2, curH - 2, ARC, ARC);

            g2.setFont(new Font("Arial", Font.BOLD, 26));
            g2.setColor(Color.WHITE);
            FontMetrics fm = g2.getFontMetrics();
            int tx = (W - fm.stringWidth(label)) / 2;
            int ty = H_CLOSED / 2 + fm.getAscent() / 2 - 2;
            g2.drawString(label, tx, ty);

            g2.dispose();
        }
    }

    private void showExitConfirm(JFrame frame) {
        JDialog dialog = new JDialog(frame, true);
        dialog.setUndecorated(true);
        dialog.setSize(440, 210);
        dialog.setLocationRelativeTo(frame);

        JPanel panel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(139, 0, 0), getWidth(), getHeight(), new Color(80, 0, 0)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.setColor(new Color(255, 255, 255, 60));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 24, 24);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(32, 36, 28, 36));

        JLabel msg = new JLabel("Are you sure you're going to leave?", JLabel.CENTER);
        msg.setFont(new Font("Arial", Font.BOLD, 18));
        msg.setForeground(Color.WHITE);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnRow.setOpaque(false);

        JButton yesBtn = makeDialogBtn("Yes");
        JButton noBtn  = makeDialogBtn("No");

        yesBtn.addActionListener(e -> System.exit(0));
        noBtn.addActionListener(e  -> dialog.dispose());

        btnRow.add(yesBtn);
        btnRow.add(noBtn);

        panel.add(msg,    BorderLayout.CENTER);
        panel.add(btnRow, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.setBackground(new Color(0, 0, 0, 0));
        dialog.setVisible(true);
    }

    private JButton makeDialogBtn(String text) {
        JButton btn = new JButton(text) {
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
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(130, 46));
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
