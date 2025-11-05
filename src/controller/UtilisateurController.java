package controller;

import database.DBConnection;
import model.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilisateurController {

    public Utilisateur authenticate(String username, String password) {
        String sql = "SELECT id_utilisateur, nom, username, password, role FROM utilisateur WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            if (ps == null) return null;
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Utilisateur u = new Utilisateur();
                    u.setId(rs.getInt("id_utilisateur"));
                    u.setNom(rs.getString("nom"));
                    u.setUsername(rs.getString("username"));
                    u.setPassword(rs.getString("password"));
                    u.setRole(rs.getString("role"));
                    return u;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur auth: " + e.getMessage());
        }
        return null;
    }
}


