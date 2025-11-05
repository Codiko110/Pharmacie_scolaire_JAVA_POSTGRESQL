package view;

import controller.UtilisateurController;
import model.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel statusLabel;
    private final UtilisateurController utilisateurController;

    public Login() {
        super("Connexion - Pharmacie Scolaire");
        this.utilisateurController = new UtilisateurController();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(380, 260);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        formPanel.add(new JLabel("Nom d'utilisateur:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Mot de passe:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton loginBtn = new JButton("Se connecter");
        actions.add(loginBtn);

        statusLabel = new JLabel(" ");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setForeground(Color.DARK_GRAY);

        add(formPanel, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
        add(statusLabel, BorderLayout.NORTH);

        loginBtn.addActionListener(e -> onLogin());
        getRootPane().setDefaultButton(loginBtn);
    }

    private void onLogin() {
        String username = usernameField.getText().trim();
        char[] pwd = passwordField.getPassword();
        String password = new String(pwd);

        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Veuillez remplir tous les champs", new Color(128, 0, 0));
            return;
        }

        Utilisateur u = utilisateurController.authenticate(username, password);
        if (u != null) {
            showStatus("Connexion réussie. Bienvenue " + u.getNom() + " !", new Color(0, 128, 0));
            SwingUtilities.invokeLater(() -> {
                dispose();
                Dashbord dash = new Dashbord(u);
                dash.setVisible(true);
            });
        } else {
            showStatus("Identifiants invalides", new Color(128, 0, 0));
        }
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }
}


