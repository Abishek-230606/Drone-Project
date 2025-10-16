import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DispatchPage extends JFrame implements ActionListener {
    JLabel statusLabel;
    JPasswordField passwordField;
    JButton confirmButton;
    String expectedPassword;

    public DispatchPage(String expectedPassword) {
        this.expectedPassword = expectedPassword;

        setTitle("🚁 Drone Dispatch");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));
        getContentPane().setBackground(new Color(240, 255, 255));

        statusLabel = new JLabel("🚁 Drone is picking up supplies...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(statusLabel);

        passwordField = new JPasswordField();
        passwordField.setVisible(false);
        add(passwordField);

        confirmButton = new JButton("Confirm Dispatch");
        confirmButton.setVisible(false);
        confirmButton.setBackground(new Color(60, 179, 113));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.addActionListener(this);
        add(confirmButton);

        new Thread(() -> {
            try {
                Thread.sleep(10000);
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("🔐 Enter your password to confirm dispatch:");
                    passwordField.setVisible(true);
                    confirmButton.setVisible(true);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String enteredPassword = String.valueOf(passwordField.getPassword());
        if (enteredPassword.equals(expectedPassword)) {
            statusLabel.setText("✅ Order dispatched successfully!");
            passwordField.setVisible(false);
            confirmButton.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Incorrect password. Try again.");
        }
    }
}