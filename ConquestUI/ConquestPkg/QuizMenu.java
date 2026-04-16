package ConquestPkg;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class QuizMenu {

    public QuizMenu(JFrame frame, String playerName) {

        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        main.setOpaque(false);

     
        JLabel welcome = new JLabel("Welcome " + playerName, JLabel.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 22));
        main.add(welcome, BorderLayout.NORTH);

        JPanel middle = new JPanel(new BorderLayout(40, 0));
        middle.setOpaque(false);
        middle.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        HoverCategoryBtn geoBtn   = new HoverCategoryBtn("Geography");
        HoverCategoryBtn driveBtn = new HoverCategoryBtn("Driving");
        HoverCategoryBtn mediaBtn = new HoverCategoryBtn("Media");
        JPanel leftCol = makeColumn(geoBtn, driveBtn, mediaBtn);

        JLabel title = new JLabel("ConQuest", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 64));

        JLabel chooseSub = new JLabel("Choose which quiz will you be taking!", JLabel.CENTER);
        chooseSub.setFont(new Font("Arial", Font.PLAIN, 14));
        chooseSub.setForeground(Color.DARK_GRAY);

        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);
        JPanel titleStack = new JPanel();
        titleStack.setLayout(new BoxLayout(titleStack, BoxLayout.Y_AXIS));
        titleStack.setOpaque(false);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        chooseSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleStack.add(title);
        titleStack.add(Box.createRigidArea(new Dimension(0, 4)));
        titleStack.add(chooseSub);
        titlePanel.add(titleStack);

        HoverCategoryBtn genBtn   = new HoverCategoryBtn("General Knowledge");
        HoverCategoryBtn brainBtn = new HoverCategoryBtn("Brainrot");
        HoverCategoryBtn progBtn  = new HoverCategoryBtn("Programming");
        JPanel rightCol = makeColumn(genBtn, brainBtn, progBtn);

        middle.add(leftCol,    BorderLayout.WEST);
        middle.add(titlePanel, BorderLayout.CENTER);
        middle.add(rightCol,   BorderLayout.EAST);
        main.add(middle, BorderLayout.CENTER);

        JButton backBtn = makeBackBtn("Back");
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);
        bottom.add(backBtn);
        main.add(bottom, BorderLayout.SOUTH);

        frame.add(main, BorderLayout.CENTER);



        //TO THE ASSIGNED TASKERS
        //
        //
        //
        backBtn.addActionListener(e  -> new MainMenu(frame, playerName));
        driveBtn.addActionListener(e -> new DrivingQuiz(frame, playerName));
        geoBtn.addActionListener(e   -> JOptionPane.showMessageDialog(frame, "Geography — Coming Soon!"));
        mediaBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Media — Coming Soon!"));
        genBtn.addActionListener(e   -> JOptionPane.showMessageDialog(frame, "General Knowledge — Coming Soon!"));
        brainBtn.addActionListener(e -> new BrainrotQuiz(frame, playerName));
        progBtn.addActionListener(e  -> JOptionPane.showMessageDialog(frame, "Programming — Coming Soon!"));

        frame.revalidate();
        frame.repaint();
    }



    private JPanel makeColumn(HoverCategoryBtn b1, HoverCategoryBtn b2, HoverCategoryBtn b3) {
       
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
                Color bg = hovered ? new Color(173, 216, 230) : UIManager.getColor("Panel.background");
                if (bg == null) bg = new Color(238, 238, 238);
                g2.setColor(bg); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(Color.DARK_GRAY); g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setPreferredSize(new Dimension(160, 50));
        btn.setContentAreaFilled(false); btn.setOpaque(false);
        btn.setFocusPainted(false); btn.setBorderPainted(false);
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

            Color base = UIManager.getColor("Panel.background");
            if (base == null) base = new Color(238, 238, 238);
            int r  = (int)(base.getRed()   + (173 - base.getRed())   * alpha);
            int gv = (int)(base.getGreen() + (216 - base.getGreen()) * alpha);
            int b  = (int)(base.getBlue()  + (230 - base.getBlue())  * alpha);
            g2.setColor(new Color(r, gv, b));
            g2.fillRoundRect(0, 0, W, curH, ARC, ARC);

            g2.setColor(Color.DARK_GRAY);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(1, 1, W - 2, curH - 2, ARC, ARC);

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.BLACK);
            FontMetrics fm = g2.getFontMetrics();
            int tx = (W - fm.stringWidth(category)) / 2;
            int ty = H_CLOSED / 2 + fm.getAscent() / 2 - 2;
            g2.drawString(category, tx, ty);

            g2.dispose();
        }
    }
}
