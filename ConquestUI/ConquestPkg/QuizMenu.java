package ConquestPkg;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class QuizMenu {

    public QuizMenu(JFrame frame, String playerName) {

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
                g2.setPaint(new RadialGradientPaint(
                    cx, cy, radius,
                    new float[]{0.0f, 0.5f, 1.0f},
                    new Color[]{
                        new Color(200, 50, 50, 80),
                        new Color(139, 0, 0, 40),
                        new Color(80, 0, 0, 0)
                    }
                ));
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

        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);

        JPanel titleStack = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int yOff = (int) floatOffset[0];

                g2.setFont(new Font("Arial", Font.BOLD, 24));
                g2.setColor(new Color(255, 200, 200));
                FontMetrics fmW = g2.getFontMetrics();
                String welcomeStr = "Welcome " + playerName;
                int wx = (getWidth() - fmW.stringWidth(welcomeStr)) / 2;
                int wy = 50 + yOff;
                g2.drawString(welcomeStr, wx, wy);

                g2.setFont(new Font("Arial", Font.BOLD, 72));
                g2.setColor(Color.WHITE);
                FontMetrics fmT = g2.getFontMetrics();
                String titleStr = "ConQuest";
                int tx = (getWidth() - fmT.stringWidth(titleStr)) / 2;
                int ty = wy + fmW.getDescent() + 6 + fmT.getAscent();
                g2.drawString(titleStr, tx, ty);

                g2.setFont(new Font("Arial", Font.PLAIN, 15));
                g2.setColor(new Color(255, 200, 200));
                FontMetrics fmS = g2.getFontMetrics();
                String subStr = "Choose which quiz will you be taking!";
                int sx = (getWidth() - fmS.stringWidth(subStr)) / 2;
                int sy = ty + fmT.getDescent() + 8 + fmS.getAscent();
                g2.drawString(subStr, sx, sy);

                g2.dispose();
            }
        };
        titleStack.setOpaque(false);
        titleStack.setPreferredSize(new Dimension(500, 200));
        titlePanel.add(titleStack);

        JPanel middle = new JPanel(new BorderLayout(50, 0));
        middle.setOpaque(false);
        middle.setBorder(BorderFactory.createEmptyBorder(0, 50, 10, 50));

        HoverCategoryBtn geoBtn   = new HoverCategoryBtn("Geography");
        HoverCategoryBtn driveBtn = new HoverCategoryBtn("Driving");
        HoverCategoryBtn mediaBtn = new HoverCategoryBtn("Media");
        JPanel leftCol = makeColumn(geoBtn, driveBtn, mediaBtn, floatOffset);

        HoverCategoryBtn genBtn   = new HoverCategoryBtn("General Knowledge");
        HoverCategoryBtn brainBtn = new HoverCategoryBtn("Brainrot");
        HoverCategoryBtn progBtn  = new HoverCategoryBtn("Programming");
        JPanel rightCol = makeColumn(genBtn, brainBtn, progBtn, floatOffset);

        middle.add(leftCol,    BorderLayout.WEST);
        middle.add(titlePanel, BorderLayout.CENTER);
        middle.add(rightCol,   BorderLayout.EAST);

        JButton backBtn = makeBackBtn("Back");
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        bottom.add(backBtn);

        bg.add(titlePanel, BorderLayout.NORTH);
        bg.add(middle,     BorderLayout.CENTER);
        bg.add(bottom,     BorderLayout.SOUTH);

        frame.setContentPane(bg);

        //TO THE ASSIGNED TASKERS
        //ONCE YOU ARE FINISHED WITH YOUR CODES IN categoryQuiz.java
        //REPLACE JOptionPane.showMessageDialog(frame, "Geography — Coming Soon!")); with --> new categoryQuiz(frame, playerName));

        backBtn.addActionListener(e  -> new MainMenu(frame, playerName));
        driveBtn.addActionListener(e -> new DrivingQuiz(frame, playerName));
        brainBtn.addActionListener(e -> new BrainrotQuiz(frame, playerName));
        geoBtn.addActionListener(e   -> JOptionPane.showMessageDialog(frame, "Geography — Coming Soon!"));
        mediaBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Media — Coming Soon!"));
        genBtn.addActionListener(e   -> JOptionPane.showMessageDialog(frame, "General Knowledge — Coming Soon!"));
        progBtn.addActionListener(e  -> JOptionPane.showMessageDialog(frame, "Programming — Coming Soon!"));

        frame.revalidate();
        frame.repaint();

        float[] time = { 0f };
        Timer floatAnim = new Timer(30, null);
        floatAnim.addActionListener(e -> {
            time[0] += 0.08f;
            floatOffset[0] = (float) (Math.sin(time[0]) * 6);
            titleStack.repaint();
        });
        floatAnim.start();
    }

    private JPanel makeColumn(HoverCategoryBtn b1, HoverCategoryBtn b2, HoverCategoryBtn b3, float[] floatOffset) {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setOpaque(false);

        JPanel inner = new JPanel() {
            @Override public Dimension getPreferredSize() {
                int h = b1.curH + b2.curH + b3.curH + 16 * 2;
                return new Dimension(HoverCategoryBtn.W, h);
            }
            @Override public void doLayout() {
                int y = 0;
                b1.setBounds(0, y, HoverCategoryBtn.W, b1.curH); y += b1.curH + 16;
                b2.setBounds(0, y, HoverCategoryBtn.W, b2.curH); y += b2.curH + 16;
                b3.setBounds(0, y, HoverCategoryBtn.W, b3.curH);
            }
        };
        inner.setLayout(null);
        inner.setOpaque(false);
        inner.add(b1);
        inner.add(b2);
        inner.add(b3);

        Runnable relayout = () -> {
            inner.revalidate();
            inner.repaint();
            outer.revalidate();
            outer.repaint();
        };
        b1.onAnimate = relayout;
        b2.onAnimate = relayout;
        b3.onAnimate = relayout;

        outer.add(inner);
        return outer;
    }

    private JButton makeBackBtn(String text) {
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
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.setColor(new Color(0,0,0,40));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 24, 24);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(170, 52));
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    static class HoverCategoryBtn extends JPanel {

        static final int W        = 280;
        static final int H_CLOSED = 70;
        static final int H_OPEN   = 140;
        private static final int ARC     = 30;
        private static final int ANIM_MS = 10;

        private final String category;
        private final JLabel descLabel;

        int   curH  = H_CLOSED;
        float alpha = 0f;
        Runnable onAnimate;

        private Timer anim;

        HoverCategoryBtn(String category) {
            this.category = category;
            setLayout(null);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            descLabel = new JLabel("<html><center>Press to Take<br>" + category + " Quiz!</center></html>", JLabel.CENTER) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            descLabel.setFont(new Font("Arial", Font.PLAIN, 13));
            descLabel.setForeground(Color.WHITE);
            descLabel.setHorizontalAlignment(JLabel.CENTER);
            descLabel.setVerticalAlignment(JLabel.CENTER);
            add(descLabel);

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { animateTo(true); }
                @Override public void mouseExited(MouseEvent e)  { animateTo(false); }
            });
        }

        void addActionListener(ActionListener l) {
            addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    l.actionPerformed(new ActionEvent(HoverCategoryBtn.this, ActionEvent.ACTION_PERFORMED, ""));
                }
            });
        }

        private void animateTo(boolean open) {
            if (anim != null && anim.isRunning()) anim.stop();
            int targetH   = open ? H_OPEN : H_CLOSED;
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
                if (Math.abs(aStep) < 0.02f) aStep = Math.signum(aDiff) * 0.02f;
                alpha += aStep;
                alpha = Math.max(0f, Math.min(1f, alpha));
                if (Math.abs(targetA - alpha) < 0.02f) alpha = targetA;

                if (curH == targetH && alpha == targetA) anim.stop();

                descLabel.setBounds(4, H_CLOSED + 2, W - 8, Math.max(0, curH - H_CLOSED - 8));

                repaint();
                if (onAnimate != null) onAnimate.run();
            });
            anim.start();
        }

        @Override
        public Dimension getPreferredSize() { return new Dimension(W, curH); }

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

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.WHITE);
            FontMetrics fm = g2.getFontMetrics();
            int tx = (W - fm.stringWidth(category)) / 2;
            int ty = H_CLOSED / 2 + fm.getAscent() / 2 - 2;
            g2.drawString(category, tx, ty);

            g2.dispose();
        }
    }
}
