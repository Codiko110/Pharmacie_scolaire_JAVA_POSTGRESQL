package view;

import controller.MedicamentController;
import model.Medicament;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class MedicamentForm extends JFrame {
    private final MedicamentController controller = new MedicamentController();

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Nom", "Categorie", "Quantité", "Péremption"}, 0
    ) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(tableModel);
    private final JTextField nomField = new JTextField();
    private final JTextField categorieField = new JTextField();
    private final JSpinner quantiteSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1_000_000, 1));
    private final JTextField peremptionField = new JTextField();

    private Integer selectedId = null;

    public MedicamentForm(JFrame parent) {
        super("Gestion des Médicaments");
        setSize(900, 560);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel form = buildFormPanel();
        JPanel actions = buildActionsPanel();

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(form, BorderLayout.EAST);
        add(actions, BorderLayout.SOUTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> onTableSelection());

        loadTable();
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 1, 6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Nom"));
        panel.add(nomField);
        panel.add(new JLabel("Catégorie"));
        panel.add(categorieField);
        panel.add(new JLabel("Quantité en stock"));
        panel.add(quantiteSpinner);
        panel.add(new JLabel("Date de péremption (YYYY-MM-DD)"));
        peremptionField.setToolTipText("Format: 2025-12-31");
        panel.add(peremptionField);
        return panel;
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
        List<Medicament> list = controller.findAll();
        for (Medicament m : list) {
            Date date = m.getDateExpiration();
            String dateStr = (date != null) ? date.toString() : "";
            tableModel.addRow(new Object[]{
                    m.getIdMedicament(), m.getNom(), m.getCategorie(), m.getQuantite(), dateStr
            });
        }
    }

    private void onTableSelection() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            selectedId = (Integer) tableModel.getValueAt(row, 0);
            nomField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
            categorieField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
            quantiteSpinner.setValue(Integer.parseInt(String.valueOf(tableModel.getValueAt(row, 3))));
            Object dateObj = tableModel.getValueAt(row, 4);
            peremptionField.setText(dateObj != null ? dateObj.toString() : "");
        }
    }

    private void onAdd() {
        Medicament m = readForm(false);
        if (m == null) return;
        if (controller.insert(m)) {
            JOptionPane.showMessageDialog(this, "Médicament ajouté", "Succès", JOptionPane.INFORMATION_MESSAGE);
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
        Medicament m = readForm(true);
        if (m == null) return;
        if (controller.update(m)) {
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
        int c = JOptionPane.showConfirmDialog(this, "Supprimer le médicament sélectionné ?", "Confirmer", JOptionPane.YES_NO_OPTION);
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

    private Medicament readForm(boolean includeId) {
        String nom = nomField.getText().trim();
        String cat = categorieField.getText().trim();
        int qte = (Integer) quantiteSpinner.getValue();
        String dateTxt = peremptionField.getText().trim();

        if (nom.isEmpty() || cat.isEmpty() || dateTxt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Champs requis manquants", "Validation", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        
        Date peremption;
        try {
            // Essayer de parser directement en format YYYY-MM-DD
            LocalDate localDate = LocalDate.parse(dateTxt);
            peremption = Date.valueOf(localDate);
        } catch (Exception ex) {
            // Si ça échoue, essayer d'autres formats ou afficher un message d'erreur
            try {
                // Essayer avec des formats alternatifs
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                java.util.Date utilDate = sdf.parse(dateTxt);
                peremption = new Date(utilDate.getTime());
            } catch (Exception ex2) {
                JOptionPane.showMessageDialog(this, 
                    "Date invalide. Format attendu: YYYY-MM-DD (ex: 2025-12-31)", 
                    "Validation", 
                    JOptionPane.WARNING_MESSAGE);
                return null;
            }
        }

        Medicament m = new Medicament(nom, cat, qte, peremption);
        if (includeId) m.setId(selectedId);
        return m;
    }

    private void clearForm() {
        selectedId = null;
        nomField.setText("");
        categorieField.setText("");
        quantiteSpinner.setValue(0);
        peremptionField.setText("");
        table.clearSelection();
    }
}


