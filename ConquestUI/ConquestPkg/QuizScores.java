package ConquestPkg;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

public class QuizScores {

    public QuizScores(JFrame frame, String playerName) {

        frame.getContentPane().removeAll();

        JPanel bg = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, new Color(139, 0, 0),
                                              getWidth(), getHeight(), new Color(80, 0, 0)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                int cx = getWidth() / 2, cy = getHeight() / 2;
                float r = Math.max(getWidth(), getHeight()) * 0.6f;
                g2.setPaint(new RadialGradientPaint(cx, cy, r,
                    new float[]{0f, 0.5f, 1f},
                    new Color[]{new Color(200,50,50,80), new Color(139,0,0,40), new Color(80,0,0,0)}
                ));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setPaint(new RadialGradientPaint(cx, cy, r * 1.2f,
                    new float[]{0.6f, 1f},
                    new Color[]{new Color(0,0,0,0), new Color(0,0,0,100)}
                ));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        bg.setOpaque(false);
        bg.setBorder(BorderFactory.createEmptyBorder(30, 50, 20, 50));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        JLabel title = new JLabel("ConQuest", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("History of Scores!", JLabel.CENTER);
        subtitle.setFont(new Font("Arial", Font.BOLD, 18));
        subtitle.setForeground(new Color(255, 200, 200));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(title);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        headerPanel.add(subtitle);

        String[] columns = { "#", "Player", "Category", "Score", "Date", "Time" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        List<String[]> entries = parseScores();
        for (int i = 0; i < entries.size(); i++) {
            String[] e = entries.get(i);
            model.addRow(new Object[]{ i + 1, e[0], e[1], e[2], e[3], e[4] });
        }

        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 15));
        table.setForeground(Color.WHITE);
        table.setBackground(new Color(255, 255, 255, 0));
        table.setOpaque(false);
        table.setRowHeight(34);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFocusable(false);
        table.setRowSelectionAllowed(false);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setHorizontalAlignment(JLabel.CENTER);
                setForeground(Color.WHITE);
                setBackground(row % 2 == 0
                    ? new Color(255, 255, 255, 20)
                    : new Color(0, 0, 0, 20));
                setOpaque(true);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        };
        for (int i = 0; i < columns.length; i++)
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 15));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setBackground(new Color(188, 74, 74));
        tableHeader.setReorderingAllowed(false);
        tableHeader.setPreferredSize(new Dimension(0, 38));
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setHorizontalAlignment(JLabel.CENTER);
                setForeground(Color.WHITE);
                setBackground(new Color(188, 74, 74));
                setFont(new Font("Arial", Font.BOLD, 15));
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        };
        for (int i = 0; i < columns.length; i++)
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);

        JScrollPane scrollPane = new JScrollPane(table) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 20));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(new Color(255, 255, 255, 60), 1, 20),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

     
        if (entries.isEmpty()) {
            JLabel empty = new JLabel("No Results Yet", JLabel.CENTER);
            empty.setFont(new Font("Arial", Font.BOLD, 24));
            empty.setForeground(new Color(255, 200, 200));
            scrollPane.setViewportView(empty);
        }

        JButton backBtn = new JButton("Back") {
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
        backBtn.setFont(new Font("Arial", Font.BOLD, 17));
        backBtn.setForeground(Color.WHITE);
        backBtn.setPreferredSize(new Dimension(160, 48));
        backBtn.setContentAreaFilled(false);
        backBtn.setOpaque(false);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> new MainMenu(frame, playerName));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
        bottomPanel.add(backBtn);

        bg.add(headerPanel, BorderLayout.NORTH);
        bg.add(scrollPane,  BorderLayout.CENTER);
        bg.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(bg);
        frame.revalidate();
        frame.repaint();
    }

    private List<String[]> parseScores() {
        List<String[]> results = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("ConquestPkg/score.txt"))) {
            String player = null, category = null, score = null, date = null, time = null;
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Player: "))        player   = line.substring(8).trim();
                else if (line.startsWith("Category: ")) category = line.substring(10).trim();
                else if (line.startsWith("Score: "))    score    = line.substring(7).trim();
                else if (line.startsWith("Date: "))     date     = line.substring(6).trim();
                else if (line.startsWith("Time: "))     time     = line.substring(6).trim();
                else if (line.startsWith("---") && player != null) {
                    results.add(new String[]{ player, category, score, date, time });
                    player = category = score = date = time = null;
                }
            }
        } catch (IOException e) { /* file doesn't exist yet */ }
        return results;
    }
}
