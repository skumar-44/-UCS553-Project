
package ui;

import dao.AuthDAO;
import model.AuthUser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.sql.SQLException;

public class LoginFrame extends JFrame {

    private final AuthDAO authDAO = new AuthDAO();

    private final Color background = new Color(18, 18, 18);
    private final Color fieldColor = new Color(40, 40, 40);
    private final Color accent = new Color(235, 99, 79);
    private final Color textColor = new Color(245, 245, 245);
    private final Color mutedColor = new Color(175, 175, 175);

    public LoginFrame() {
        setTitle("Arena | Gaming Tournament Manager");
        setSize(460, 570);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setForeground(Color.BLACK);
        tabs.setBackground(Color.WHITE);
        tabs.setOpaque(true);

        // Force white tabs instead of the operating system's default tab colors.
        tabs.setUI(new BasicTabbedPaneUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                tabAreaInsets = new Insets(5, 5, 5, 5);
            }

            @Override
            protected void paintTabBackground(
                    Graphics g, int tabPlacement, int tabIndex,
                    int x, int y, int w, int h, boolean isSelected) {

                g.setColor(Color.WHITE);
                g.fillRect(x, y, w, h);
            }

            @Override
            protected void paintTabBorder(
                    Graphics g, int tabPlacement, int tabIndex,
                    int x, int y, int w, int h, boolean isSelected) {

                g.setColor(new Color(200, 200, 200));
                g.drawRect(x, y, w, h);
            }
        });

        tabs.addTab("Sign In", createLoginPanel());
        tabs.addTab("Register", createRegisterPanel());

        add(tabs);
        getContentPane().setBackground(background);
    }

    // ---------------- SIGN IN ----------------

    private JPanel createLoginPanel() {
        JPanel panel = createFormPanel();

        panel.add(createHeading("Welcome Back"));
        panel.add(createSubtitle("Sign in to your account"));
        panel.add(Box.createVerticalStrut(28));

        JTextField emailField = createTextField();
        JPasswordField passwordField = createPasswordField();

        addField(panel, "Email", emailField);
        addField(panel, "Password", passwordField);

        panel.add(Box.createVerticalStrut(15));

        JButton loginButton = createButton("Sign In");
        panel.add(loginButton);

        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                showError("Please enter your email and password.");
                return;
            }

            try {
                AuthUser user = authDAO.signIn(email, password);

                if (user == null) {
                    showError("Invalid email or password.");
                    passwordField.setText("");
                    return;
                }

                openDashboard(user);

            } catch (SQLException ex) {
                showError("Database error: " + ex.getMessage());
            }
        });

        return panel;
    }

    // ---------------- REGISTER ----------------

    private JPanel createRegisterPanel() {
        JPanel panel = createFormPanel();

        panel.add(createHeading("Create Account"));
        panel.add(createSubtitle("Join the gaming community"));
        panel.add(Box.createVerticalStrut(15));

        JTextField nameField = createTextField();
        JTextField emailField = createTextField();
        JPasswordField passwordField = createPasswordField();
        JPasswordField confirmField = createPasswordField();

        addField(panel, "Full Name", nameField);
        addField(panel, "Email", emailField);
        addField(panel, "Password", passwordField);
        addField(panel, "Confirm Password", confirmField);

        panel.add(Box.createVerticalStrut(5));

        JButton registerButton = createButton("Create Account");
        panel.add(registerButton);

        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirm = new String(confirmField.getPassword());

            if (name.isEmpty() || email.isEmpty()
                    || password.isEmpty() || confirm.isEmpty()) {
                showError("Please fill in all fields.");
                return;
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                showError("Please enter a valid email address.");
                return;
            }

            if (password.length() < 8) {
                showError("Password must contain at least 8 characters.");
                return;
            }

            if (!password.equals(confirm)) {
                showError("Passwords do not match.");
                return;
            }

            try {
                authDAO.register(name, email, password);

                JOptionPane.showMessageDialog(
                        this,
                        "Account created successfully! You can now sign in.",
                        "Registration Successful",
                        JOptionPane.INFORMATION_MESSAGE);

                emailField.setText(email);
                passwordField.setText("");
                confirmField.setText("");

            } catch (SQLException ex) {
                if (ex.getMessage() != null
                        && ex.getMessage().toLowerCase().contains("unique")) {
                    showError("An account with this email already exists.");
                } else {
                    showError("Could not create account: " + ex.getMessage());
                }
            }
        });

        return panel;
    }

    // ---------------- UI COMPONENTS ----------------

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(background);
        panel.setBorder(new EmptyBorder(25, 45, 25, 45));
        return panel;
    }

    private JLabel createHeading(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 27));
        label.setForeground(textColor);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createSubtitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(mutedColor);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        styleField(field);
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        styleField(field);
        return field;
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(textColor);
        field.setBackground(fieldColor);
        field.setCaretColor(Color.WHITE);

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(75, 75, 75)),
                new EmptyBorder(5, 8, 5, 8)));

        field.setPreferredSize(new Dimension(300, 36));
        field.setMaximumSize(new Dimension(300, 36));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void addField(JPanel panel, String text, JTextField field) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(textColor);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        panel.add(Box.createVerticalStrut(14));
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(accent);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setPreferredSize(new Dimension(200, 42));
        button.setMaximumSize(new Dimension(200, 42));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        return button;
    }

    // ---------------- COMMON METHODS ----------------

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void openDashboard(AuthUser user) {
        dispose();

        MainFrame dashboard = new MainFrame();
        dashboard.setTitle("Arena | Welcome, " + user.getName());
        dashboard.setLocationRelativeTo(null);
        dashboard.setVisible(true);
    }
}
