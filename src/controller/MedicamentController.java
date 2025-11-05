package controller;

import database.DBConnection;
import model.Medicament;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentController {

    public List<Medicament> findAll() {
        List<Medicament> list = new ArrayList<>();
        String sql = "SELECT id_medicament, nom, categorie, quantite_stock, date_peremption FROM medicament ORDER BY nom";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            if (ps == null) return list;
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Medicament m = new Medicament(
                            rs.getInt("id_medicament"),
                            rs.getString("nom"),
                            rs.getString("categorie"),
                            rs.getInt("quantite_stock"),
                            rs.getDate("date_peremption")
                    );
                    list.add(m);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur findAll medicament: " + e.getMessage());
        }
        return list;
    }

    public boolean insert(Medicament m) {
        String sql = "INSERT INTO medicament(nom, categorie, quantite_stock, date_peremption) VALUES (?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            if (ps == null) return false;
            ps.setString(1, m.getNom());
            ps.setString(2, m.getCategorie());
            ps.setInt(3, m.getQuantiteStock());
            ps.setDate(4, m.getDatePeremption());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Erreur insert medicament: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Medicament m) {
        String sql = "UPDATE medicament SET nom=?, categorie=?, quantite_stock=?, date_peremption=? WHERE id_medicament=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            if (ps == null) return false;
            ps.setString(1, m.getNom());
            ps.setString(2, m.getCategorie());
            ps.setInt(3, m.getQuantiteStock());
            ps.setDate(4, m.getDatePeremption());
            ps.setInt(5, m.getIdMedicament());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Erreur update medicament: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM medicament WHERE id_medicament=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            if (ps == null) return false;
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Erreur delete medicament: " + e.getMessage());
            return false;
        }
    }
}


