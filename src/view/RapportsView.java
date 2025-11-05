package view;

import controller.DelivranceController;
import controller.MedicamentController;
import controller.PatientController;
import database.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RapportsView extends JFrame {
    private final JTextArea statsArea = new JTextArea();
    private final JTextField dateDebutField = new JTextField();
    private final JTextField dateFinField = new JTextField();

    public RapportsView(JFrame parent) {
        super("Rapports et Statistiques");
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel de filtres
        JPanel filterPanel = new JPanel(new GridLayout(3, 2, 6, 6));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Période d'analyse"));
        filterPanel.add(new JLabel("Date début (YYYY-MM-DD):"));
        filterPanel.add(dateDebutField);
        filterPanel.add(new JLabel("Date fin (YYYY-MM-DD):"));
        filterPanel.add(dateFinField);
        JButton generateBtn = new JButton("Générer Rapport");
        JButton exportBtn = new JButton("Exporter PDF (à venir)");
        filterPanel.add(generateBtn);
        filterPanel.add(exportBtn);

        // Zone de texte pour les statistiques
        statsArea.setEditable(false);
        statsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(statsArea);

        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        generateBtn.addActionListener(e -> generateRapport());
        exportBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, 
            "L'export PDF sera implémenté avec une bibliothèque comme iText ou Apache PDFBox", 
            "Info", 
            JOptionPane.INFORMATION_MESSAGE));

        // Générer le rapport par défaut
        generateRapport();
    }

    private void generateRapport() {
        StringBuilder rapport = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        rapport.append("═══════════════════════════════════════════════════════════\n");
        rapport.append("  RAPPORT DE GESTION - PHARMACIE SCOLAIRE\n");
        rapport.append("═══════════════════════════════════════════════════════════\n");
        rapport.append("Date de génération: ").append(sdf.format(new Date())).append("\n\n");

        // Statistiques générales
        rapport.append("─── STATISTIQUES GÉNÉRALES ───\n\n");
        rapport.append("Nombre total de médicaments: ").append(getTotalMedicaments()).append("\n");
        rapport.append("Nombre total de patients: ").append(getTotalPatients()).append("\n");
        rapport.append("Nombre total de délivrances: ").append(getTotalDelivrances()).append("\n\n");

        // Statistiques par période si dates spécifiées
        String dateDebut = dateDebutField.getText().trim();
        String dateFin = dateFinField.getText().trim();
        
        if (!dateDebut.isEmpty() && !dateFin.isEmpty()) {
            rapport.append("─── STATISTIQUES PÉRIODE (").append(dateDebut).append(" à ").append(dateFin).append(") ───\n\n");
            rapport.append("Délivrances dans la période: ").append(getDelivrancesPeriode(dateDebut, dateFin)).append("\n");
            rapport.append("Quantité totale délivrée: ").append(getQuantiteDelivreePeriode(dateDebut, dateFin)).append(" unités\n\n");
        }

        // Top médicaments délivrés
        rapport.append("─── TOP 5 MÉDICAMENTS LES PLUS DÉLIVRÉS ───\n\n");
        rapport.append(getTopMedicaments());

        // Médicaments en alerte
        rapport.append("\n─── ALERTES ACTUELLES ───\n\n");
        rapport.append("Médicaments périmés: ").append(getMedicamentsPerimes()).append("\n");
        rapport.append("Médicaments stock bas (<10): ").append(getMedicamentsStockBas()).append("\n");

        // Répartition par type de patient
        rapport.append("\n─── RÉPARTITION PAR TYPE DE PATIENT ───\n\n");
        rapport.append(getRepartitionPatients());

        statsArea.setText(rapport.toString());
    }

    private int getTotalMedicaments() {
        String sql = "SELECT COUNT(*) FROM medicament";
        return getIntFromQuery(sql);
    }

    private int getTotalPatients() {
        String sql = "SELECT COUNT(*) FROM patient";
        return getIntFromQuery(sql);
    }

    private int getTotalDelivrances() {
        String sql = "SELECT COUNT(*) FROM delivrance";
        return getIntFromQuery(sql);
    }

    private int getDelivrancesPeriode(String dateDebut, String dateFin) {
        String sql = "SELECT COUNT(*) FROM delivrance WHERE date_sortie BETWEEN ? AND ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            if (ps == null) return 0;
            ps.setDate(1, java.sql.Date.valueOf(dateDebut));
            ps.setDate(2, java.sql.Date.valueOf(dateFin));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Erreur getDelivrancesPeriode: " + e.getMessage());
        }
        return 0;
    }

    private int getQuantiteDelivreePeriode(String dateDebut, String dateFin) {
        String sql = "SELECT COALESCE(SUM(quantite), 0) FROM delivrance WHERE date_sortie BETWEEN ? AND ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            if (ps == null) return 0;
            ps.setDate(1, java.sql.Date.valueOf(dateDebut));
            ps.setDate(2, java.sql.Date.valueOf(dateFin));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Erreur getQuantiteDelivreePeriode: " + e.getMessage());
        }
        return 0;
    }

    private String getTopMedicaments() {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT m.nom, SUM(d.quantite) as total " +
                     "FROM delivrance d " +
                     "JOIN medicament m ON d.id_medicament = m.id_medicament " +
                     "GROUP BY m.id_medicament, m.nom " +
                     "ORDER BY total DESC " +
                     "LIMIT 5";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            if (ps == null) return "Erreur de connexion\n";
            try (ResultSet rs = ps.executeQuery()) {
                int rank = 1;
                while (rs.next()) {
                    sb.append(rank).append(". ").append(rs.getString("nom"))
                      .append(" - ").append(rs.getInt("total")).append(" unités\n");
                    rank++;
                }
                if (rank == 1) {
                    sb.append("Aucune délivrance enregistrée\n");
                }
            }
        } catch (SQLException e) {
            sb.append("Erreur: ").append(e.getMessage()).append("\n");
        }
        return sb.toString();
    }

    private int getMedicamentsPerimes() {
        String sql = "SELECT COUNT(*) FROM medicament WHERE date_peremption < CURRENT_DATE";
        return getIntFromQuery(sql);
    }

    private int getMedicamentsStockBas() {
        String sql = "SELECT COUNT(*) FROM medicament WHERE quantite_stock < 10";
        return getIntFromQuery(sql);
    }

    private String getRepartitionPatients() {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT type_patient, COUNT(*) as nombre FROM patient GROUP BY type_patient";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            if (ps == null) return "Erreur de connexion\n";
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sb.append(rs.getString("type_patient")).append(": ")
                      .append(rs.getInt("nombre")).append("\n");
                }
            }
        } catch (SQLException e) {
            sb.append("Erreur: ").append(e.getMessage()).append("\n");
        }
        return sb.toString();
    }

    private int getIntFromQuery(String sql) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            if (ps == null) return 0;
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur getIntFromQuery: " + e.getMessage());
        }
        return 0;
    }
}

