package view;

import controller.MedicamentController;
import database.DBConnection;
import model.Medicament;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AlertesView extends JFrame {
    private final MedicamentController medicamentController = new MedicamentController();

    private final DefaultTableModel perimesModel = new DefaultTableModel(
            new String[]{"ID", "Nom", "Catégorie", "Stock", "Date Péremption"}, 0
    ) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    private final DefaultTableModel bientotPerimesModel = new DefaultTableModel(
            new String[]{"ID", "Nom", "Catégorie", "Stock", "Date Péremption", "Jours restants"}, 0
    ) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    private final DefaultTableModel stockBasModel = new DefaultTableModel(
            new String[]{"ID", "Nom", "Catégorie", "Stock", "Date Péremption"}, 0
    ) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    private final JTable perimesTable = new JTable(perimesModel);
    private final JTable bientotPerimesTable = new JTable(bientotPerimesModel);
    private final JTable stockBasTable = new JTable(stockBasModel);

    private final JSpinner seuilStockSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));
    private final JSpinner joursAlerteSpinner = new JSpinner(new SpinnerNumberModel(30, 1, 365, 1));

    public AlertesView(JFrame parent) {
        super("Alertes - Pharmacie Scolaire");
        setSize(1000, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Onglet Médicaments Périmés
        JPanel perimesPanel = new JPanel(new BorderLayout());
        perimesPanel.add(new JScrollPane(perimesTable), BorderLayout.CENTER);
        JPanel perimesHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        perimesHeader.add(new JLabel("Médicaments périmés (date de péremption dépassée)"));
        perimesPanel.add(perimesHeader, BorderLayout.NORTH);
        tabbedPane.addTab("Périmés", perimesPanel);

        // Onglet Bientôt Périmés
        JPanel bientotPanel = new JPanel(new BorderLayout());
        bientotPanel.add(new JScrollPane(bientotPerimesTable), BorderLayout.CENTER);
        JPanel bientotHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bientotHeader.add(new JLabel("Alerter si expiration dans:"));
        bientotHeader.add(joursAlerteSpinner);
        bientotHeader.add(new JLabel("jours"));
        JButton refreshBientot = new JButton("Actualiser");
        refreshBientot.addActionListener(e -> loadBientotPerimes());
        bientotHeader.add(refreshBientot);
        bientotPanel.add(bientotHeader, BorderLayout.NORTH);
        tabbedPane.addTab("Bientôt Périmés", bientotPanel);

        // Onglet Stock Bas
        JPanel stockBasPanel = new JPanel(new BorderLayout());
        stockBasPanel.add(new JScrollPane(stockBasTable), BorderLayout.CENTER);
        JPanel stockBasHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        stockBasHeader.add(new JLabel("Alerter si stock <"));
        stockBasHeader.add(seuilStockSpinner);
        JButton refreshStock = new JButton("Actualiser");
        refreshStock.addActionListener(e -> loadStockBas());
        stockBasHeader.add(refreshStock);
        stockBasPanel.add(stockBasHeader, BorderLayout.NORTH);
        tabbedPane.addTab("Stock Bas", stockBasPanel);

        add(tabbedPane, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshAll = new JButton("Actualiser Toutes les Alertes");
        refreshAll.addActionListener(e -> loadAll());
        actions.add(refreshAll);
        add(actions, BorderLayout.SOUTH);

        // Couleurs pour les alertes
        perimesTable.setDefaultRenderer(Object.class, new ColorRenderer(Color.RED));
        bientotPerimesTable.setDefaultRenderer(Object.class, new ColorRenderer(Color.ORANGE));
        stockBasTable.setDefaultRenderer(Object.class, new ColorRenderer(Color.YELLOW));

        loadAll();
    }

    private void loadAll() {
        loadPerimes();
        loadBientotPerimes();
        loadStockBas();
    }

    private void loadPerimes() {
        perimesModel.setRowCount(0);
        String sql = "SELECT id_medicament, nom, categorie, quantite_stock, date_peremption FROM medicament WHERE date_peremption < CURRENT_DATE ORDER BY date_peremption";
        loadMedicamentsFromQuery(sql, perimesModel, false);
    }

    private void loadBientotPerimes() {
        bientotPerimesModel.setRowCount(0);
        int jours = (Integer) joursAlerteSpinner.getValue();
        String sql = "SELECT id_medicament, nom, categorie, quantite_stock, date_peremption, " +
                     "(date_peremption - CURRENT_DATE) as jours_restants " +
                     "FROM medicament " +
                     "WHERE date_peremption >= CURRENT_DATE AND date_peremption <= CURRENT_DATE + INTERVAL '" + jours + " day' " +
                     "ORDER BY date_peremption";
        loadMedicamentsFromQuery(sql, bientotPerimesModel, true);
    }

    private void loadStockBas() {
        stockBasModel.setRowCount(0);
        int seuil = (Integer) seuilStockSpinner.getValue();
        String sql = "SELECT id_medicament, nom, categorie, quantite_stock, date_peremption FROM medicament WHERE quantite_stock < ? ORDER BY quantite_stock";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            if (ps == null) return;
            ps.setInt(1, seuil);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date date = rs.getDate("date_peremption");
                    String dateStr = (date != null) ? date.toString() : "";
                    stockBasModel.addRow(new Object[]{
                            rs.getInt("id_medicament"),
                            rs.getString("nom"),
                            rs.getString("categorie"),
                            rs.getInt("quantite_stock"),
                            dateStr
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur loadStockBas: " + e.getMessage());
        }
    }

    private void loadMedicamentsFromQuery(String sql, DefaultTableModel model, boolean includeJours) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            if (ps == null) return;
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date date = rs.getDate("date_peremption");
                    String dateStr = (date != null) ? date.toString() : "";
                    if (includeJours) {
                        int joursRestants = rs.getInt("jours_restants");
                        model.addRow(new Object[]{
                                rs.getInt("id_medicament"),
                                rs.getString("nom"),
                                rs.getString("categorie"),
                                rs.getInt("quantite_stock"),
                                dateStr,
                                joursRestants
                        });
                    } else {
                        model.addRow(new Object[]{
                                rs.getInt("id_medicament"),
                                rs.getString("nom"),
                                rs.getString("categorie"),
                                rs.getInt("quantite_stock"),
                                dateStr
                        });
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur loadMedicamentsFromQuery: " + e.getMessage());
        }
    }

    // Renderer pour colorer les lignes
    private static class ColorRenderer extends javax.swing.table.DefaultTableCellRenderer {
        private final Color alertColor;

        public ColorRenderer(Color alertColor) {
            this.alertColor = alertColor;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                c.setBackground(alertColor);
                c.setForeground(Color.BLACK);
            }
            return c;
        }
    }
}

