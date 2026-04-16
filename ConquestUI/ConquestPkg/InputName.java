package ConquestPkg;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class InputName {

    JFrame frame;
    JTextField nameField;
    JButton proceedBtn;

    public InputName() {
        createFrame();
        buildUI();
        frame.setVisible(true);
    }

    public void createFrame() {
        frame = new JFrame("ConQuest");
        frame.setSize(1100, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }

    private void buildUI() {
        JPanel bg = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, new Color(139, 0, 0),
                                              getWidth(), getHeight(), new Color(80, 0, 0)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        bg.setOpaque(false);

        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 25));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
                g2.setColor(new Color(255, 255, 255, 60));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 32, 32);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(520, 380));
        card.setBorder(BorderFactory.createEmptyBorder(44, 50, 44, 50));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        JLabel title = new JLabel("ConQuest");
        title.setFont(new Font("Arial", Font.BOLD, 52));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Player Name");
        sub.setFont(new Font("Arial", Font.PLAIN, 16));
        sub.setForeground(new Color(255, 200, 200));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameField = new JTextField(25) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        nameField.setText("Enter Player Name");
        nameField.setForeground(new Color(200, 180, 180));
        nameField.setCaretColor(Color.WHITE);
        nameField.setFont(new Font("Arial", Font.PLAIN, 22));
        nameField.setOpaque(false);
        nameField.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(new Color(255, 255, 255, 100), 2, 16),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        nameField.setPreferredSize(new Dimension(420, 58));
        nameField.setMaximumSize(new Dimension(420, 58));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (nameField.getText().equals("Enter Player Name")) {
                    nameField.setText("");
                    nameField.setForeground(Color.WHITE);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (nameField.getText().isEmpty()) {
                    nameField.setText("Enter Player Name");
                    nameField.setForeground(new Color(200, 180, 180));
                }
            }
        });

        proceedBtn = new JButton("Proceed") {
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
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(new Color(0, 0, 0, 40));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        proceedBtn.setFont(new Font("Arial", Font.BOLD, 20));
        proceedBtn.setForeground(Color.WHITE);
        proceedBtn.setPreferredSize(new Dimension(420, 58));
        proceedBtn.setMaximumSize(new Dimension(420, 58));
        proceedBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        proceedBtn.setContentAreaFilled(false);
        proceedBtn.setOpaque(false);
        proceedBtn.setFocusPainted(false);
        proceedBtn.setBorderPainted(false);
        proceedBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        proceedBtn.addActionListener(e -> handleProceed());

        nameField.addActionListener(e -> handleProceed());

        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 6)));
        content.add(sub);
        content.add(Box.createRigidArea(new Dimension(0, 36)));
        content.add(nameField);
        content.add(Box.createRigidArea(new Dimension(0, 16)));
        content.add(proceedBtn);

        card.add(content, BorderLayout.CENTER);
        bg.add(card);

        frame.setContentPane(bg);
    }

    public void handleProceed() {
        String playerName = nameField.getText().trim();
        if (playerName.isEmpty() || playerName.equals("Enter Player Name")) {
            JOptionPane.showMessageDialog(frame, "Please enter your name!");
        } else {
            frame.getContentPane().removeAll();
            new MainMenu(frame, playerName);
            frame.revalidate();
            frame.repaint();
        }
    }
}
