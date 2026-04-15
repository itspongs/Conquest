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
        frame.setSize(700, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridBagLayout());
        frame.setResizable(false);
    }

    // METHOD 2: Components
    public void createComponents() {
        nameField = new JTextField(15);
        proceedBtn = new JButton("Proceed");

        // Styling
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setFont(new Font("Arial", Font.PLAIN, 30));
        proceedBtn.setFont(new Font("Arial", Font.BOLD, 25));
    }

    // METHOD 3: Add Components (CENTERED)
    public void addComponents() {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        // TITLE
        JLabel title = new JLabel("Input Player Name", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 27));

        gbc.gridy = 0;
        frame.add(title, gbc);

        // TEXT FIELD
        gbc.gridy = 1;
        frame.add(nameField, gbc);

        // BUTTON
        gbc.gridy = 2;
        frame.add(proceedBtn, gbc);
    }

    // METHOD 4: Actions
    public void addActions() {
        proceedBtn.addActionListener(e -> handleProceed());
    }

    // METHOD 5: Logic
    public void handleProceed() {
        String playerName = nameField.getText();

        if (playerName.isEmpty()) {
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