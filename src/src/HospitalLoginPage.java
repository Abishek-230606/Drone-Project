import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.util.Properties;
import java.util.Random;
import jakarta.mail.*;
import jakarta.mail.internet.*;


public class HospitalLoginPage extends JFrame {

    // --- UI Design Constants ---
    private static final Color COLOR_PRIMARY_RED_LIGHT = new Color(190, 30, 45);
    private static final Color COLOR_PRIMARY_RED_DARK = new Color(150, 0, 0);
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Color COLOR_TEXT_DARK = new Color(30, 30, 30);
    private static final Color COLOR_TEXT_RED = new Color(178, 34, 34);

    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font FONT_LABEL = new Font("Segoe UI Semibold", Font.PLAIN, 16);
    private static final Font FONT_FIELD = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 16);

    // --- Swing Components ---
    private JTextField hospitalIdField;
    private JPasswordField passwordField;
    private JTextField otpField;
    private JButton sendOTPButton;
    private JButton loginButton;
    private JLabel statusLabel;

    // --- Internal Logic Variables ---
    private String hospitalName;
    private int generatedOTP = -1;
    private String hospitalEmail_fromDB = ""; // ✅ FIX 1: Variable to store the email

    public HospitalLoginPage(String hospitalName) {
        this.hospitalName = hospitalName;
        setTitle("Login - " + hospitalName);
        setSize(500, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // --- Main Panel (Full Window) ---
        setLayout(new BorderLayout(0, 0));

        // --- 1. Header Panel (Gradient) ---
        add(createHeaderPanel(), BorderLayout.NORTH);

        // --- 2. Form Panel (White) ---
        add(createFormPanel(), BorderLayout.CENTER);

        // --- 3. Add Actions ---
        addActions();

        setVisible(true);
    }

    /**
     * Creates the top gradient header panel with the title.
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 25)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, COLOR_PRIMARY_RED_LIGHT,
                        getWidth(), getHeight(), COLOR_PRIMARY_RED_DARK
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(500, 100));
        JLabel title = new JLabel("🏥 " + hospitalName + " Login");
        title.setFont(FONT_TITLE);
        title.setForeground(COLOR_WHITE);
        headerPanel.add(title);
        return headerPanel;
    }

    /**
     * Creates the central white form panel with all fields and buttons.
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(COLOR_WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 40, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Hospital ID Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel hospitalIdLabel = new JLabel("Hospital ID");
        styleLabel(hospitalIdLabel);
        formPanel.add(hospitalIdLabel, gbc);

        // Row 1: Hospital ID Field
        gbc.gridy = 1;
        hospitalIdField = new JTextField(20);
        styleTextField(hospitalIdField);
        formPanel.add(hospitalIdField, gbc);

        // Row 2: Password Label
        gbc.gridy = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        JLabel passwordLabel = new JLabel("Password");
        styleLabel(passwordLabel);
        formPanel.add(passwordLabel, gbc);

        // Row 3: Password Field
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 5, 5, 5);
        passwordField = new JPasswordField(20);
        styleTextField(passwordField);
        formPanel.add(passwordField, gbc);

        // Row 4: Send OTP Button
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(10, 5, 15, 5);
        sendOTPButton = new JButton("Send OTP");
        styleSecondaryButton(sendOTPButton);
        formPanel.add(sendOTPButton, gbc);

        // Row 5: OTP Label
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 5, 5);
        JLabel otpLabel = new JLabel("Enter OTP");
        styleLabel(otpLabel);
        formPanel.add(otpLabel, gbc);

        // Row 6: OTP Field
        gbc.gridy = 6;
        otpField = new JTextField(20);
        styleTextField(otpField);
        formPanel.add(otpField, gbc);

        // Row 7: Login Button
        gbc.gridy = 7;
        gbc.insets = new Insets(25, 5, 15, 5);
        gbc.ipady = 10;
        loginButton = new GradientRoundedButton("Login", COLOR_PRIMARY_RED_LIGHT, COLOR_PRIMARY_RED_DARK);
        loginButton.setFont(FONT_BUTTON);
        formPanel.add(loginButton, gbc);

        // Row 8: Status Label
        gbc.gridy = 8;
        gbc.insets = new Insets(10, 5, 0, 5);
        gbc.ipady = 0;
        statusLabel = new JLabel(" ");
        statusLabel.setFont(FONT_FIELD);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(statusLabel, gbc);

        return formPanel;
    }

    // --- UI Styling Helper Methods ---
    private void styleLabel(JLabel label) {
        label.setFont(FONT_LABEL);
        label.setForeground(COLOR_TEXT_RED);
    }

    private void styleTextField(JTextComponent field) {
        field.setFont(FONT_FIELD);
        field.setForeground(COLOR_TEXT_DARK);
        field.setBackground(COLOR_WHITE);
        field.setCaretColor(COLOR_TEXT_RED);
        field.setBorder(new MatteBorder(0, 0, 2, 0, COLOR_TEXT_RED));
    }

    private void styleSecondaryButton(JButton button) {
        button.setFont(FONT_BUTTON);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setForeground(COLOR_TEXT_RED);
        button.setBackground(COLOR_WHITE);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(8, COLOR_TEXT_RED, 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setContentAreaFilled(true);
                button.setOpaque(true);
                button.setBackground(COLOR_TEXT_RED);
                button.setForeground(COLOR_WHITE);
            }
            public void mouseExited(MouseEvent evt) {
                button.setContentAreaFilled(false);
                button.setOpaque(false);
                button.setBackground(COLOR_WHITE);
                button.setForeground(COLOR_TEXT_RED);
            }
        });
    }

    private void showError(String message) {
        statusLabel.setForeground(COLOR_TEXT_RED);
        statusLabel.setText(message);
    }

    private void showSuccess(String message) {
        statusLabel.setForeground(COLOR_TEXT_DARK);
        statusLabel.setText(message);
    }

    // --- 🚨 DANGER ZONE 🚨 ---
    private final String SENDER_EMAIL = "jsabishek77@gmail.com";
    private final String SENDER_PASSWORD = "veig xhwe aaie rgxt";


    // --- Internal Logic ---

    private void addActions() {
        sendOTPButton.addActionListener(e -> {
            String hospitalId = hospitalIdField.getText().trim();
            String password = String.valueOf(passwordField.getPassword()).trim();
            if (hospitalId.isEmpty() || password.isEmpty()) {
                showError("❌ Enter Hospital ID and Password first.");
                return;
            }
            showSuccess("🔄 Sending OTP, please wait...");
            sendOTPButton.setEnabled(false);

            new SwingWorker<String, Void>() {
                private String email = null;
                private boolean success = false;

                @Override
                protected String doInBackground() throws Exception {
                    try (Connection conn = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/dronedb", "root", "Sathishdhana#23")) {
                        String query = "SELECT hospital_email FROM registered_hospitals WHERE hospital_id=? AND password=?";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setString(1, hospitalId);
                        stmt.setString(2, password);
                        ResultSet rs = stmt.executeQuery();
                        if (rs.next()) {
                            email = rs.getString("hospital_email");
                            hospitalEmail_fromDB = email; // ✅ FIX 2: Save the email
                            generatedOTP = 100000 + new Random().nextInt(900000);
                            sendEmailOTP(email, generatedOTP);
                            success = true;
                            return "✅ OTP sent to " + email;
                        } else {
                            return "❌ Invalid credentials or hospital not found.";
                        }
                    } catch (Exception ex) {
                        return "❌ DB/Email Error: " + ex.getMessage();
                    }
                }

                @Override
                protected void done() {
                    try {
                        String result = get();
                        if (success) {
                            showSuccess(result);
                        } else {
                            showError(result);
                        }
                    } catch (Exception ex) {
                        showError("❌ Error: " + ex.getMessage());
                    }
                    sendOTPButton.setEnabled(true);
                }
            }.execute();
        });

        loginButton.addActionListener(e -> {
            String enteredOTP = otpField.getText().trim();
            String hospitalId = hospitalIdField.getText().trim();
            if (enteredOTP.isEmpty()) {
                showError("⚠️ Please enter the OTP sent to your email.");
                return;
            }
            if (generatedOTP == -1) {
                showError("❌ Please send an OTP first.");
                return;
            }
            if (String.valueOf(generatedOTP).equals(enteredOTP)) {
                showSuccess("✅ Login Successful! Loading dashboard...");
                Timer timer = new Timer(1000, (ae) -> {
                    // ✅ FIX 3: Pass the hospital ID, name, AND the email
                    new DashboardPage(hospitalId, hospitalName, hospitalEmail_fromDB);
                    dispose();
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                showError("❌ Invalid OTP. Try again!");
            }
        });
    }

    private void sendEmailOTP(String recipientEmail, int otp) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SENDER_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject("Hospital Login OTP - Drone Medical System");
        message.setText("Dear " + hospitalName + ",\n\nYour OTP for login is: " + otp
                + "\n\nThis OTP is valid for 5 minutes.\n\n- Drone Medical System Team");
        Transport.send(message);
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, just use the default
        }

        SwingUtilities.invokeLater(() -> new HospitalLoginPage("SRM Global Hospital"));
    }
}


// --- 1. CUSTOM BUTTON CLASS ---
class GradientRoundedButton extends JButton {
    private Color color1;
    private Color color2;
    private Color hoverColor1;
    private Color hoverColor2;
    private boolean isHovered = false;
    private int arc = 20;

    public GradientRoundedButton(String text, Color color1, Color color2) {
        super(text);
        this.color1 = color1;
        this.color2 = color2;
        this.hoverColor1 = color1.darker();
        this.hoverColor2 = color2.darker();
        setForeground(Color.WHITE);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color c1 = isHovered ? hoverColor1 : color1;
        Color c2 = isHovered ? hoverColor2 : color2;
        GradientPaint gp = new GradientPaint(0, 0, c1, 0, getHeight(), c2);
        g2.setPaint(gp);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc, arc));
        g2.dispose();
        super.paintComponent(g);
    }
}


// --- 2. CUSTOM BORDER CLASS (FOR GHOST BUTTON) ---
class RoundedBorder implements Border {
    private int radius;
    private Color color;
    private int thickness;

    public RoundedBorder(int radius, Color color, int thickness) {
        this.radius = radius;
        this.color = color;
        this.thickness = thickness;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius + this.thickness, this.radius + this.thickness, this.radius + this.thickness, this.radius + this.thickness);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));
        g2.draw(new RoundRectangle2D.Double(x + (thickness/2.0), y + (thickness/2.0), width - thickness, height - thickness, radius, radius));
        g2.dispose();
    }
}