package view;

import controller.DelivranceController;
import controller.MedicamentController;
import controller.PatientController;
import model.Delivrance;
import model.Medicament;
import model.Patient;
import model.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DelivranceForm extends JFrame {
    private final DelivranceController delivranceController = new DelivranceController();
    private final MedicamentController medicamentController = new MedicamentController();
    private final PatientController patientController = new PatientController();
    private final Utilisateur utilisateur;

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Date", "Patient", "Médicament", "Quantité", "Motif"}, 0
    ) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(tableModel);

    private final JComboBox<String> patientCombo = new JComboBox<>();
    private final JComboBox<String> medicamentCombo = new JComboBox<>();
    private final JSpinner quantiteSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
    private final JTextField motifField = new JTextField();
    private final JTextField dateField = new JTextField();
    private final JLabel stockLabel = new JLabel("Stock disponible: -");

    private final Map<String, Integer> patientIdMap = new HashMap<>();
    private final Map<String, Integer> medicamentIdMap = new HashMap<>();

    public DelivranceForm(JFrame parent, Utilisateur utilisateur) {
        super("Délivrance de Médicaments");
        this.utilisateur = utilisateur;
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel form = buildFormPanel();
        JPanel actions = buildActionsPanel();

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(form, BorderLayout.EAST);
        add(actions, BorderLayout.SOUTH);

        medicamentCombo.addActionListener(e -> updateStockDisplay());

        loadCombos();
        loadTable();
        
        // Date par défaut = aujourd'hui
        dateField.setText(LocalDate.now().toString());
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridLayout(12, 1, 6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Patient:"));
        panel.add(patientCombo);
        panel.add(new JLabel("Médicament:"));
        panel.add(medicamentCombo);
        panel.add(stockLabel);
        panel.add(new JLabel("Quantité:"));
        panel.add(quantiteSpinner);
        panel.add(new JLabel("Motif:"));
        panel.add(motifField);

        return panel;
    }

    private JPanel buildActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addBtn = new JButton("Enregistrer Délivrance");
        JButton refreshBtn = new JButton("Actualiser");

        panel.add(refreshBtn);
        panel.add(addBtn);

        addBtn.addActionListener(e -> onEnregistrer());
        refreshBtn.addActionListener(e -> {
            loadCombos();
            loadTable();
        });
        return panel;
    }

    private void loadCombos() {
        patientCombo.removeAllItems();
        patientIdMap.clear();
        List<Patient> patients = patientController.findAll();
        for (Patient p : patients) {
            String display = p.getNom() + " (" + p.getTypePatient() + ")";
            patientCombo.addItem(display);
            patientIdMap.put(display, p.getId());
        }

        medicamentCombo.removeAllItems();
        medicamentIdMap.clear();
        List<Medicament> medicaments = medicamentController.findAll();
        for (Medicament m : medicaments) {
            String display = m.getNom() + " (Stock: " + m.getQuantiteStock() + ")";
            medicamentCombo.addItem(display);
            medicamentIdMap.put(display, m.getIdMedicament());
        }

        if (!medicaments.isEmpty()) {
            updateStockDisplay();
        }
    }

    private void updateStockDisplay() {
        String selected = (String) medicamentCombo.getSelectedItem();
        if (selected != null && medicamentIdMap.containsKey(selected)) {
            int idMedicament = medicamentIdMap.get(selected);
            int stock = delivranceController.getStockDisponible(idMedicament);
            stockLabel.setText("Stock disponible: " + stock);
            stockLabel.setForeground(stock < 10 ? Color.RED : Color.BLACK);
        }
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        List<Delivrance> list = delivranceController.findAll();
        for (Delivrance d : list) {
            Patient p = patientController.findById(d.getIdPatient());
            Medicament m = medicamentController.findAll().stream()
                    .filter(med -> med.getIdMedicament() == d.getIdMedicament())
                    .findFirst().orElse(null);

            String patientNom = (p != null) ? p.getNom() : "ID:" + d.getIdPatient();
            String medicamentNom = (m != null) ? m.getNom() : "ID:" + d.getIdMedicament();

            Date date = d.getDateSortie();
            String dateStr = (date != null) ? date.toString() : "";
            tableModel.addRow(new Object[]{
                    d.getId(), dateStr, patientNom, medicamentNom, d.getQuantite(), d.getMotif()
            });
        }
    }

    private void onEnregistrer() {
        String dateTxt = dateField.getText().trim();
        String patientSelected = (String) patientCombo.getSelectedItem();
        String medicamentSelected = (String) medicamentCombo.getSelectedItem();
        int quantite = (Integer) quantiteSpinner.getValue();
        String motif = motifField.getText().trim();

        if (dateTxt.isEmpty() || patientSelected == null || medicamentSelected == null || motif.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!patientIdMap.containsKey(patientSelected) || !medicamentIdMap.containsKey(medicamentSelected)) {
            JOptionPane.showMessageDialog(this, "Erreur de sélection", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date dateSortie;
        try {
            LocalDate localDate = LocalDate.parse(dateTxt);
            dateSortie = Date.valueOf(localDate);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Date invalide. Format attendu: YYYY-MM-DD", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idPatient = patientIdMap.get(patientSelected);
        int idMedicament = medicamentIdMap.get(medicamentSelected);

        // Vérifier le stock
        int stockDispo = delivranceController.getStockDisponible(idMedicament);
        if (stockDispo < quantite) {
            JOptionPane.showMessageDialog(this, 
                "Stock insuffisant! Stock disponible: " + stockDispo, 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        Delivrance d = new Delivrance(dateSortie, idPatient, idMedicament, quantite, utilisateur.getId(), motif);
        if (delivranceController.enregistrerDelivrance(d)) {
            JOptionPane.showMessageDialog(this, "Délivrance enregistrée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            loadTable();
            loadCombos();
            motifField.setText("");
            quantiteSpinner.setValue(1);
        } else {
            JOptionPane.showMessageDialog(this, "Échec d'enregistrement (stock insuffisant?)", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}

