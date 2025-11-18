package view;

import controller.PatientController;
import model.Patient;
import model.Eleve;
import model.Enseignant;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class PatientForm extends JFrame {
    private final PatientController controller = new PatientController();

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Nom", "Sexe", "Date Naissance", "Type", "Détails"}, 0
    ) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(tableModel);

    private final JTextField nomField = new JTextField();
    private final JComboBox<String> sexeCombo = new JComboBox<>(new String[]{"M", "F"});
    private final JTextField dateNaissanceField = new JTextField();
    private final JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Eleve", "Enseignant"});
    
    // Champs spécifiques Élève
    private final JTextField matriculeField = new JTextField();
    private final JTextField classeField = new JTextField();
    
    // Champs spécifiques Enseignant
    private final JTextField departementField = new JTextField();
    private final JTextField fonctionField = new JTextField();

    private final JPanel elevePanel = new JPanel(new GridLayout(2, 2, 6, 6));
    private final JPanel enseignantPanel = new JPanel(new GridLayout(2, 2, 6, 6));

    private Integer selectedId = null;

    public PatientForm(JFrame parent) {
        super("Gestion des Patients");
        setSize(1000, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setupTypePanels();
        JPanel form = buildFormPanel();
        JPanel actions = buildActionsPanel();

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(form, BorderLayout.EAST);
        add(actions, BorderLayout.SOUTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> onTableSelection());

        typeCombo.addActionListener(e -> onTypeChange());

        loadTable();
    }

    private void setupTypePanels() {
        elevePanel.setLayout(new BoxLayout(elevePanel, BoxLayout.Y_AXIS));
        elevePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 1),
            "Informations Élève",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(52, 152, 219)
        ));
        
        JLabel matriculeLabel = new JLabel("Matricule:");
        matriculeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        matriculeLabel.setForeground(new Color(52, 73, 94));
        elevePanel.add(matriculeLabel);
        styleTextField(matriculeField);
        elevePanel.add(matriculeField);
        elevePanel.add(Box.createVerticalStrut(10));
        
        JLabel classeLabel = new JLabel("Classe:");
        classeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        classeLabel.setForeground(new Color(52, 73, 94));
        elevePanel.add(classeLabel);
        styleTextField(classeField);
        elevePanel.add(classeField);

        enseignantPanel.setLayout(new BoxLayout(enseignantPanel, BoxLayout.Y_AXIS));
        enseignantPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113), 1),
            "Informations Enseignant",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(46, 204, 113)
        ));
        
        JLabel departementLabel = new JLabel("Département:");
        departementLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        departementLabel.setForeground(new Color(52, 73, 94));
        enseignantPanel.add(departementLabel);
        styleTextField(departementField);
        enseignantPanel.add(departementField);
        enseignantPanel.add(Box.createVerticalStrut(10));
        
        JLabel fonctionLabel = new JLabel("Fonction:");
        fonctionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        fonctionLabel.setForeground(new Color(52, 73, 94));
        enseignantPanel.add(fonctionLabel);
        styleTextField(fonctionField);
        enseignantPanel.add(fonctionField);
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setBackground(Color.WHITE);
        field.setForeground(new Color(52, 73, 94));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Effet de focus
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                    BorderFactory.createEmptyBorder(9, 11, 9, 11)
                ));
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
                ));
            }
        });
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(250, 250, 250));

        // Styliser les champs de base
        styleTextField(nomField);
        styleTextField(dateNaissanceField);
        styleComboBox(sexeCombo);
        styleComboBox(typeCombo);

        JPanel baseFields = new JPanel();
        baseFields.setLayout(new BoxLayout(baseFields, BoxLayout.Y_AXIS));
        baseFields.setBackground(new Color(250, 250, 250));
        
        // Nom
        JLabel nomLabel = new JLabel("Nom:");
        nomLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nomLabel.setForeground(new Color(52, 73, 94));
        baseFields.add(nomLabel);
        baseFields.add(Box.createVerticalStrut(5));
        baseFields.add(nomField);
        baseFields.add(Box.createVerticalStrut(15));
        
        // Sexe
        JLabel sexeLabel = new JLabel("Sexe:");
        sexeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sexeLabel.setForeground(new Color(52, 73, 94));
        baseFields.add(sexeLabel);
        baseFields.add(Box.createVerticalStrut(5));
        baseFields.add(sexeCombo);
        baseFields.add(Box.createVerticalStrut(15));
        
        // Date Naissance
        JLabel dateLabel = new JLabel("Date Naissance (YYYY-MM-DD):");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(52, 73, 94));
        baseFields.add(dateLabel);
        baseFields.add(Box.createVerticalStrut(5));
        baseFields.add(dateNaissanceField);
        baseFields.add(Box.createVerticalStrut(15));
        
        // Type
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        typeLabel.setForeground(new Color(52, 73, 94));
        baseFields.add(typeLabel);
        baseFields.add(Box.createVerticalStrut(5));
        baseFields.add(typeCombo);
        baseFields.add(Box.createVerticalStrut(20));

        panel.add(baseFields);
        
        elevePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        enseignantPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        elevePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        enseignantPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        panel.add(elevePanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(enseignantPanel);

        onTypeChange(); // Initialiser l'affichage

        return panel;
    }
    
    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        combo.setBackground(Color.WHITE);
        combo.setForeground(new Color(52, 73, 94));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Effet de focus
        combo.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                combo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                    BorderFactory.createEmptyBorder(7, 9, 7, 9)
                ));
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                combo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));
            }
        });
    }

    private void onTypeChange() {
        String type = (String) typeCombo.getSelectedItem();
        elevePanel.setVisible("Eleve".equals(type));
        enseignantPanel.setVisible("Enseignant".equals(type));
        revalidate();
        repaint();
    }

    private JPanel buildActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addBtn = new JButton("Ajouter");
        JButton updateBtn = new JButton("Modifier");
        JButton deleteBtn = new JButton("Supprimer");
        JButton clearBtn = new JButton("Vider");

        panel.add(clearBtn);
        panel.add(addBtn);
        panel.add(updateBtn);
        panel.add(deleteBtn);

        addBtn.addActionListener(e -> onAdd());
        updateBtn.addActionListener(e -> onUpdate());
        deleteBtn.addActionListener(e -> onDelete());
        clearBtn.addActionListener(e -> clearForm());
        return panel;
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        List<Patient> list = controller.findAll();
        for (Patient p : list) {
            String details = "";
            if ("Eleve".equals(p.getTypePatient())) {
                Eleve e = controller.findEleveByPatientId(p.getId());
                if (e != null) {
                    details = "Mat: " + e.getMatricule() + " - Classe: " + e.getClasse();
                }
            } else if ("Enseignant".equals(p.getTypePatient())) {
                Enseignant en = controller.findEnseignantByPatientId(p.getId());
                if (en != null) {
                    details = "Dept: " + en.getDepartement() + " - " + en.getFonction();
                }
            }
            
            Date date = p.getDateNaissance();
            String dateStr = (date != null) ? date.toString() : "";
            tableModel.addRow(new Object[]{
                    p.getId(), p.getNom(), p.getSexe(), dateStr, p.getTypePatient(), details
            });
        }
    }

    private void onTableSelection() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            selectedId = (Integer) tableModel.getValueAt(row, 0);
            Patient p = controller.findById(selectedId);
            if (p != null) {
                nomField.setText(p.getNom());
                sexeCombo.setSelectedItem(String.valueOf(p.getSexe()));
                dateNaissanceField.setText(p.getDateNaissance() != null ? p.getDateNaissance().toString() : "");
                typeCombo.setSelectedItem(p.getTypePatient());
                
                if ("Eleve".equals(p.getTypePatient())) {
                    Eleve e = controller.findEleveByPatientId(selectedId);
                    if (e != null) {
                        matriculeField.setText(e.getMatricule());
                        classeField.setText(e.getClasse());
                    }
                } else if ("Enseignant".equals(p.getTypePatient())) {
                    Enseignant en = controller.findEnseignantByPatientId(selectedId);
                    if (en != null) {
                        departementField.setText(en.getDepartement());
                        fonctionField.setText(en.getFonction());
                    }
                }
                onTypeChange();
            }
        }
    }

    private void onAdd() {
        Patient p = readPatientForm(false);
        Eleve eleve = null;
        Enseignant enseignant = null;

        if (p == null) return;

        String type = (String) typeCombo.getSelectedItem();
        if ("Eleve".equals(type)) {
            eleve = readEleveForm();
            if (eleve == null) return;
        } else if ("Enseignant".equals(type)) {
            enseignant = readEnseignantForm();
            if (enseignant == null) return;
        }

        if (controller.insert(p, eleve, enseignant)) {
            JOptionPane.showMessageDialog(this, "Patient ajouté", "Succès", JOptionPane.INFORMATION_MESSAGE);
            loadTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Échec d'ajout", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onUpdate() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une ligne", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Patient p = readPatientForm(true);
        Eleve eleve = null;
        Enseignant enseignant = null;

        if (p == null) return;

        String type = (String) typeCombo.getSelectedItem();
        if ("Eleve".equals(type)) {
            eleve = readEleveForm();
            if (eleve == null) return;
        } else if ("Enseignant".equals(type)) {
            enseignant = readEnseignantForm();
            if (enseignant == null) return;
        }

        if (controller.update(p, eleve, enseignant)) {
            JOptionPane.showMessageDialog(this, "Mise à jour réussie", "Succès", JOptionPane.INFORMATION_MESSAGE);
            loadTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Échec de mise à jour", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDelete() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une ligne", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int c = JOptionPane.showConfirmDialog(this, "Supprimer le patient sélectionné ?", "Confirmer", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            if (controller.delete(selectedId)) {
                JOptionPane.showMessageDialog(this, "Supprimé", "Succès", JOptionPane.INFORMATION_MESSAGE);
                loadTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Suppression échouée", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Patient readPatientForm(boolean includeId) {
        String nom = nomField.getText().trim();
        String sexeStr = (String) sexeCombo.getSelectedItem();
        String dateTxt = dateNaissanceField.getText().trim();
        String type = (String) typeCombo.getSelectedItem();

        if (nom.isEmpty() || dateTxt.isEmpty() || type == null) {
            JOptionPane.showMessageDialog(this, "Champs requis manquants (Nom, Date, Type)", "Validation", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Date dateNaissance;
        try {
            LocalDate localDate = LocalDate.parse(dateTxt);
            dateNaissance = Date.valueOf(localDate);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Date invalide. Format attendu: YYYY-MM-DD", "Validation", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Patient p = new Patient(nom, sexeStr.charAt(0), dateNaissance, type);
        if (includeId) p.setId(selectedId);
        return p;
    }

    private Eleve readEleveForm() {
        String matricule = matriculeField.getText().trim();
        String classe = classeField.getText().trim();

        if (matricule.isEmpty() || classe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Matricule et Classe requis pour un Élève", "Validation", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return new Eleve(0, matricule, classe);
    }

    private Enseignant readEnseignantForm() {
        String departement = departementField.getText().trim();
        String fonction = fonctionField.getText().trim();

        if (departement.isEmpty() || fonction.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Département et Fonction requis pour un Enseignant", "Validation", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return new Enseignant(0, departement, fonction);
    }

    private void clearForm() {
        selectedId = null;
        nomField.setText("");
        sexeCombo.setSelectedIndex(0);
        dateNaissanceField.setText("");
        typeCombo.setSelectedIndex(0);
        matriculeField.setText("");
        classeField.setText("");
        departementField.setText("");
        fonctionField.setText("");
        table.clearSelection();
        onTypeChange();
    }
}

