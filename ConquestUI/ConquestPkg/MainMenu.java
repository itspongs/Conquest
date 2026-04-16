package ConquestPkg;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainMenu {

    public MainMenu(JFrame frame, String playerName) {

        MusicPlayer.play("menu_music.wav");

        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        header.setOpaque(false);

        JLabel welcome = new JLabel("Welcome " + playerName, JLabel.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 36));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("ConQuest!", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 64));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(welcome);
        header.add(Box.createRigidArea(new Dimension(0, 4)));
        header.add(title);

        
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

        frame.add(header, BorderLayout.NORTH);
        frame.add(centerWrapper, BorderLayout.CENTER);

        playCard.onClick(() -> new QuizMenu(frame, playerName));
        scoreCard.onClick(() -> new QuizScores(frame, playerName));
        exitCard.onClick(() -> showExitConfirm(frame));

        frame.revalidate();
        frame.repaint();
    }

    static class HoverCard extends JPanel {

        private static final int W          = 260;
        private static final int H_CLOSED   = 100;
        private static final int H_OPEN     = 220;
        private static final int ARC        = 24;
        private static final int ANIM_MS    = 10;

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
            descLabel.setFont(new Font("Arial", Font.PLAIN, 15));
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
            int targetH = open ? H_OPEN : H_CLOSED;
            float targetA = open ? 1f : 0f;

            anim = new Timer(ANIM_MS, null);
            anim.addActionListener(e -> {

                //LORD TABANG KALIBOG
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

                int descTop = H_CLOSED + 4;
                int descH   = curH - descTop - 8;
                descLabel.setBounds(8, descTop, W - 16, Math.max(0, descH));

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

            // HEEEEEEEEEEEEEEEEEEEEEE
            Color bg = (alpha > 0) ? new Color(173, 216, 230) : UIManager.getColor("Panel.background");
            if (bg == null) bg = new Color(238, 238, 238);
          
            Color base = UIManager.getColor("Panel.background");
            if (base == null) base = new Color(238, 238, 238);
            int r = (int)(base.getRed()   + (173 - base.getRed())   * alpha);
            int gv= (int)(base.getGreen() + (216 - base.getGreen()) * alpha);
            int b = (int)(base.getBlue()  + (230 - base.getBlue())  * alpha);
            g2.setColor(new Color(r, gv, b));
            g2.fillRoundRect(0, 0, getWidth(), curH, ARC, ARC);

    
            g2.setColor(Color.DARK_GRAY);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(1, 1, getWidth() - 2, curH - 2, ARC, ARC);

            // button label 
            g2.setFont(new Font("Arial", Font.BOLD, 28));
            g2.setColor(Color.BLACK);
            FontMetrics fm = g2.getFontMetrics();
            int tx = (getWidth() - fm.stringWidth(label)) / 2;
            int ty = H_CLOSED / 2 + fm.getAscent() / 2 - 2;
            g2.drawString(label, tx, ty);

            g2.dispose();
        }
    }

    private void showExitConfirm(JFrame frame) {
        JDialog dialog = new JDialog(frame, true);
        dialog.setUndecorated(true);
        dialog.setSize(420, 200);
        dialog.setLocationRelativeTo(frame);

        JPanel panel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 245, 245));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.setColor(Color.DARK_GRAY);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 24, 24);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(28, 32, 24, 32));

        JLabel msg = new JLabel("Are you sure you're going to leave?", JLabel.CENTER);
        msg.setFont(new Font("Arial", Font.BOLD, 18));

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
                Color bg = hovered ? new Color(173, 216, 230) : UIManager.getColor("Panel.background");
                if (bg == null) bg = new Color(238, 238, 238);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(Color.DARK_GRAY);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setPreferredSize(new Dimension(120, 44));
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
