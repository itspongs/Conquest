    package ConquestPkg;

    import java.awt.*;
    import javax.swing.*;

    public class InputName {

        JFrame frame;
        JTextField nameField;
        JButton proceedBtn;

        // CONSTRUCTOR
        public InputName() {
            createFrame();
            createComponents();
            addComponents();
            addActions();
            frame.setVisible(true);
        }

        // METHOD 1: Frame
        public void createFrame() {
            frame = new JFrame("Conquest");
            frame.setSize(1100, 700);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new GridBagLayout());
            frame.setResizable(false);
        }

        // METHOD 2: Components
        public void createComponents() {
            nameField = new JTextField(25);
            proceedBtn = new JButton("Proceed");

            // Placeholder behavior
            nameField.setText("Enter Player Name");
            nameField.setForeground(Color.GRAY);
            nameField.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent e) {
                    if (nameField.getText().equals("Enter Player Name")) {
                        nameField.setText("");
                        nameField.setForeground(Color.BLACK);
                    }
                }
                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    if (nameField.getText().isEmpty()) {
                        nameField.setText("Enter Player Name");
                        nameField.setForeground(Color.GRAY);
                    }
                }
            });

            // Styling
            nameField.setHorizontalAlignment(JTextField.LEFT);
            nameField.setFont(new Font("Arial", Font.PLAIN, 26));
            nameField.setPreferredSize(new Dimension(480, 60));
            nameField.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(Color.GRAY, 2, 10),
                    BorderFactory.createEmptyBorder(5, 12, 5, 12)
            ));

            proceedBtn.setFont(new Font("Arial", Font.BOLD, 22));
            proceedBtn.setPreferredSize(new Dimension(480, 65));
            proceedBtn.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(Color.GRAY, 2, 16),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
            ));
            proceedBtn.setContentAreaFilled(false);
            proceedBtn.setOpaque(false);
            proceedBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    proceedBtn.setBackground(new Color(173, 216, 230));
                    proceedBtn.setContentAreaFilled(true);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    proceedBtn.setContentAreaFilled(false);
                }
            });
        }

        // METHOD 3: Add Components (CENTERED)
        public void addComponents() {
            JPanel group = new JPanel();
            group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));

            JLabel label = new JLabel("Player Name");
            label.setFont(new Font("Arial", Font.BOLD, 24));
            label.setAlignmentX(Component.LEFT_ALIGNMENT);

            nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
            proceedBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

            group.add(label);
            group.add(Box.createRigidArea(new Dimension(0, 6)));
            group.add(nameField);
            group.add(Box.createRigidArea(new Dimension(0, 12)));
            group.add(proceedBtn);

            frame.setLayout(new GridBagLayout());
            frame.add(group, new GridBagConstraints());
        }

        // METHOD 4: Actions
        public void addActions() {
            proceedBtn.addActionListener(e -> handleProceed());
        }

        // METHOD 5: Logic
        public void handleProceed() {
            String playerName = nameField.getText().trim();

            if (playerName.isEmpty() || playerName.equals("Enter Player Name")) {
                JOptionPane.showMessageDialog(frame, "Please enter your name!");
            } else {
                // 🔥 DO NOT DISPOSE FRAME
                frame.getContentPane().removeAll();

                // Load Main Menu in SAME frame
                new MainMenu(frame, playerName);

                frame.revalidate();
                frame.repaint();
            }
        }
    }