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
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Couleur de fond standard desktop
        getContentPane().setBackground(SystemColor.window);
        setLayout(new BorderLayout());

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(SystemColor.window);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Panel du formulaire avec style desktop
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(SystemColor.window);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEtchedBorder(),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Titre
        JLabel titleLabel = new JLabel("Pharmacie Scolaire");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(titleLabel, gbc);

        // Sous-titre
        JLabel subtitleLabel = new JLabel("Connexion");
        subtitleLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setForeground(Color.GRAY);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(subtitleLabel, gbc);

        // Réinitialiser les contraintes pour les champs
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Label nom d'utilisateur
        JLabel usernameLabel = new JLabel("Nom d'utilisateur:");
        usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(usernameLabel, gbc);

        // Champ nom d'utilisateur
        usernameField = new JTextField(20);
        styleDesktopTextField(usernameField);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(usernameField, gbc);

        // Label mot de passe
        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(passwordLabel, gbc);

        // Champ mot de passe
        passwordField = new JPasswordField(20);
        styleDesktopTextField(passwordField);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(passwordField, gbc);

        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        formPanel.add(statusLabel, gbc);

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(SystemColor.window);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 0, 5);
        formPanel.add(buttonPanel, gbc);

        // Bouton de connexion
        JButton loginBtn = new JButton("Se connecter");
        styleDesktopButton(loginBtn);
        buttonPanel.add(loginBtn);

        // Bouton Annuler
        JButton cancelBtn = new JButton("Annuler");
        styleDesktopCancelButton(cancelBtn);
        buttonPanel.add(cancelBtn);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // Actions des boutons
        loginBtn.addActionListener(e -> onLogin());
        cancelBtn.addActionListener(e -> System.exit(0));
        
        getRootPane().setDefaultButton(loginBtn);
        
        // Donner le focus au champ username
        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
    }

    private void styleDesktopTextField(JTextField field) {
        field.setFont(new Font("Tahoma", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColor.controlDkShadow),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        field.setBackground(Color.WHITE);
        field.setPreferredSize(new Dimension(200, 28));
    }

    private void styleDesktopButton(JButton button) {
        button.setFont(new Font("Tahoma", Font.BOLD, 12));
        button.setBackground(SystemColor.control);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(6, 15, 6, 15)
        ));
        button.setFocusPainted(true);
        button.setMnemonic('C'); // Alt+C pour le raccourci
        
        // Effet hover simple
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(SystemColor.controlHighlight);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SystemColor.control);
            }
        });
    }

    private void styleDesktopCancelButton(JButton button) {
        button.setFont(new Font("Tahoma", Font.PLAIN, 12));
        button.setBackground(SystemColor.control);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(6, 15, 6, 15)
        ));
        button.setFocusPainted(true);
        button.setMnemonic('A'); // Alt+A pour le raccourci
    }

    private void onLogin() {
        String username = usernameField.getText().trim();
        char[] pwd = passwordField.getPassword();
        String password = new String(pwd);

        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Veuillez remplir tous les champs", Color.RED);
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
            showStatus("Identifiants invalides", Color.RED);
            passwordField.setText("");
            usernameField.requestFocus();
        }
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    public static void main(String[] args) {
        try {
            // Utiliser le look and feel système - CORRECTION ICI
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setVisible(true);
        });
    }
}