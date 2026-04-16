package ConquestPkg;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class QuizScores {

    public QuizScores(JFrame frame, String playerName) {

        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));

        // ── Title ─────────────────────────────────────────────────
        JLabel title = new JLabel("ConQuest", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 40));

        JLabel subtitle = new JLabel("History of Scores!", JLabel.CENTER);
        subtitle.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(title);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(subtitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 14)));

        main.add(titlePanel, BorderLayout.NORTH);

        // ── Table ─────────────────────────────────────────────────
        String[] columns = { "Number", "Player", "Category", "Score", "Date", "Time" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        List<String[]> entries = parseScores();
        if (entries.isEmpty()) {
            // empty model — "No Results Yet" shown in the scroll pane
        } else {
            for (int i = 0; i < entries.size(); i++) {
                String[] e = entries.get(i);
                model.addRow(new Object[]{ i + 1, e[0], e[1], e[2], e[3], e[4] });
            }
        }

        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 15));
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFocusable(false);
        table.setRowSelectionAllowed(false);

        // Center all columns
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < columns.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        // Header style
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 15));
        header.setReorderingAllowed(false);
        header.setBorder(new RoundedBorder(Color.DARK_GRAY, 2, 20));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new RoundedBorder(Color.DARK_GRAY, 2, 20));
        scrollPane.setPreferredSize(new Dimension(0, 280));

        // "No Results Yet" overlay when empty
        if (entries.isEmpty()) {
            JLabel empty = new JLabel("No Results Yet", JLabel.CENTER);
            empty.setFont(new Font("Arial", Font.BOLD, 22));
            empty.setForeground(Color.GRAY);
            scrollPane.setViewportView(empty);
        }

        main.add(scrollPane, BorderLayout.CENTER);

        // ── Back button ───────────────────────────────────────────
        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Arial", Font.BOLD, 16));
        backBtn.setPreferredSize(new Dimension(160, 42));
        backBtn.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(Color.DARK_GRAY, 2, 20),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                backBtn.setBackground(new Color(173, 216, 230));
                backBtn.setContentAreaFilled(true);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                backBtn.setContentAreaFilled(false);
            }
        });
        backBtn.addActionListener(e -> new MainMenu(frame, playerName));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
        bottomPanel.add(backBtn);

        main.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(main, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    // ── Parse score.txt into list of [player, category, score, date, time] ──
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
        } catch (IOException e) {
            // file doesn't exist yet — return empty list
        }
        return results;
    }
}
