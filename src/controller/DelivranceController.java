package controller;

import database.DBConnection;
import model.Delivrance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DelivranceController {

    public boolean enregistrerDelivrance(Delivrance d) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;

        try {
            conn.setAutoCommit(false);

            // Vérifier le stock disponible
            String sqlCheckStock = "SELECT quantite_stock FROM medicament WHERE id_medicament = ?";
            PreparedStatement psCheck = conn.prepareStatement(sqlCheckStock);
            psCheck.setInt(1, d.getIdMedicament());
            ResultSet rs = psCheck.executeQuery();
            
            if (!rs.next()) {
                conn.rollback();
                return false; // Médicament introuvable
            }
            
            int stockActuel = rs.getInt("quantite_stock");
            if (stockActuel < d.getQuantite()) {
                conn.rollback();
                return false; // Stock insuffisant
            }

            // Décrémenter le stock
            String sqlUpdateStock = "UPDATE medicament SET quantite_stock = quantite_stock - ? WHERE id_medicament = ?";
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateStock);
            psUpdate.setInt(1, d.getQuantite());
            psUpdate.setInt(2, d.getIdMedicament());
            psUpdate.executeUpdate();

            // Enregistrer la délivrance
            String sqlInsert = "INSERT INTO delivrance(date_sortie, id_patient, id_medicament, quantite, id_utilisateur, motif) VALUES (?,?,?,?,?,?)";
            PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
            psInsert.setDate(1, d.getDateSortie());
            psInsert.setInt(2, d.getIdPatient());
            psInsert.setInt(3, d.getIdMedicament());
            psInsert.setInt(4, d.getQuantite());
            psInsert.setInt(5, d.getIdUtilisateur());
            psInsert.setString(6, d.getMotif());
            psInsert.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.out.println("Erreur rollback: " + ex.getMessage());
            }
            System.out.println("Erreur enregistrerDelivrance: " + e.getMessage());
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                System.out.println("Erreur fermeture connexion: " + e.getMessage());
            }
        }
    }

    public List<Delivrance> findAll() {
        List<Delivrance> list = new ArrayList<>();
        String sql = "SELECT id_delivrance, date_sortie, id_patient, id_medicament, quantite, id_utilisateur, motif FROM delivrance ORDER BY date_sortie DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            if (ps == null) return list;
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Delivrance d = new Delivrance(
                            rs.getInt("id_delivrance"),
                            rs.getDate("date_sortie"),
                            rs.getInt("id_patient"),
                            rs.getInt("id_medicament"),
                            rs.getInt("quantite"),
                            rs.getInt("id_utilisateur"),
                            rs.getString("motif")
                    );
                    list.add(d);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur findAll delivrance: " + e.getMessage());
        }
        return list;
    }

    public int getStockDisponible(int idMedicament) {
        String sql = "SELECT quantite_stock FROM medicament WHERE id_medicament = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            if (ps == null) return 0;
            ps.setInt(1, idMedicament);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantite_stock");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur getStockDisponible: " + e.getMessage());
        }
        return 0;
    }
}

