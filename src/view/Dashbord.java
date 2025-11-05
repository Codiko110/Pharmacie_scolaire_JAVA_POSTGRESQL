package view;

import model.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class Dashbord extends JFrame {
    private final Utilisateur utilisateur;

    public Dashbord(Utilisateur utilisateur) {
        super("Tableau de bord - Pharmacie Scolaire");
        this.utilisateur = utilisateur;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel welcome = new JLabel("Connecté en tant que: " + utilisateur.getNom() + " (" + utilisateur.getRole() + ")");
        welcome.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttons = new JPanel(new GridLayout(2, 3, 12, 12));
        buttons.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton medsBtn = new JButton("Médicaments");
        JButton patientsBtn = new JButton("Patients");
        JButton delivranceBtn = new JButton("Délivrance");
        JButton alertesBtn = new JButton("Alertes");
        JButton rapportsBtn = new JButton("Rapports");
        JButton logoutBtn = new JButton("Déconnexion");

        buttons.add(medsBtn);
        buttons.add(patientsBtn);
        buttons.add(delivranceBtn);
        buttons.add(alertesBtn);
        buttons.add(rapportsBtn);
        buttons.add(logoutBtn);

        add(welcome, BorderLayout.NORTH);
        add(buttons, BorderLayout.CENTER);

        medsBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new MedicamentForm(this).setVisible(true)));
        patientsBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new PatientForm(this).setVisible(true)));
        delivranceBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new DelivranceForm(this, utilisateur).setVisible(true)));
        alertesBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new AlertesView(this).setVisible(true)));
        rapportsBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new RapportsView(this).setVisible(true)));
        logoutBtn.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });
    }
}


