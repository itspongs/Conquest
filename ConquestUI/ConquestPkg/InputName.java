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
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
    }

    // METHOD 2: Components
    public void createComponents() {
        nameField = new JTextField();
        nameField.setBounds(100, 70, 200, 30);

        proceedBtn = new JButton("Proceed");
        proceedBtn.setBounds(140, 120, 120, 30);
    }

    // METHOD 3: Add Components
    public void addComponents() {
        JLabel title = new JLabel("Input Player Name");
        title.setBounds(50, 20, 300, 40);

        // CENTER the text
        title.setHorizontalAlignment(JLabel.CENTER);

        // MAKE TEXT BIGGER
        title.setFont(new Font("Arial", Font.BOLD, 20));

        // Center text inside the text field
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));

        // Bigger button text
        proceedBtn.setFont(new Font("Arial", Font.BOLD, 14));

        frame.add(title);
        frame.add(nameField);
        frame.add(proceedBtn);
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
            frame.dispose();
            new MainMenu(playerName);
        }
    }
}