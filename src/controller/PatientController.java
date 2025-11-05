package controller;

import database.DBConnection;
import model.Patient;
import model.Eleve;
import model.Enseignant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientController {

    public List<Patient> findAll() {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT id_patient, nom, sexe, date_naissance, type_patient FROM patient ORDER BY nom";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            if (ps == null) return list;
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Patient p = new Patient(
                            rs.getInt("id_patient"),
                            rs.getString("nom"),
                            rs.getString("sexe").charAt(0),
                            rs.getDate("date_naissance"),
                            rs.getString("type_patient")
                    );
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur findAll patient: " + e.getMessage());
        }
        return list;
    }

    public Patient findById(int id) {
        String sql = "SELECT id_patient, nom, sexe, date_naissance, type_patient FROM patient WHERE id_patient = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            if (ps == null) return null;
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Patient(
                            rs.getInt("id_patient"),
                            rs.getString("nom"),
                            rs.getString("sexe").charAt(0),
                            rs.getDate("date_naissance"),
                            rs.getString("type_patient")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur findById patient: " + e.getMessage());
        }
        return null;
    }

    public Eleve findEleveByPatientId(int idPatient) {
        String sql = "SELECT id_patient, matricule, classe FROM eleve WHERE id_patient = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            if (ps == null) return null;
            ps.setInt(1, idPatient);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Eleve(
                            rs.getInt("id_patient"),
                            rs.getString("matricule"),
                            rs.getString("classe")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur findEleve: " + e.getMessage());
        }
        return null;
    }

    public Enseignant findEnseignantByPatientId(int idPatient) {
        String sql = "SELECT id_patient, departement, fonction FROM enseignant WHERE id_patient = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {
            if (ps == null) return null;
            ps.setInt(1, idPatient);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Enseignant(
                            rs.getInt("id_patient"),
                            rs.getString("departement"),
                            rs.getString("fonction")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur findEnseignant: " + e.getMessage());
        }
        return null;
    }

    public boolean insert(Patient p, Eleve eleve, Enseignant enseignant) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;

        try {
            conn.setAutoCommit(false);

            // Insert patient
            String sqlPatient = "INSERT INTO patient(nom, sexe, date_naissance, type_patient) VALUES (?,?,?,?)";
            PreparedStatement psPatient = conn.prepareStatement(sqlPatient, Statement.RETURN_GENERATED_KEYS);
            psPatient.setString(1, p.getNom());
            psPatient.setString(2, String.valueOf(p.getSexe()));
            psPatient.setDate(3, p.getDateNaissance());
            psPatient.setString(4, p.getTypePatient());
            psPatient.executeUpdate();

            ResultSet rs = psPatient.getGeneratedKeys();
            int idPatient = 0;
            if (rs.next()) {
                idPatient = rs.getInt(1);
            }

            // Insert selon le type
            if ("Eleve".equals(p.getTypePatient()) && eleve != null) {
                String sqlEleve = "INSERT INTO eleve(id_patient, matricule, classe) VALUES (?,?,?)";
                PreparedStatement psEleve = conn.prepareStatement(sqlEleve);
                psEleve.setInt(1, idPatient);
                psEleve.setString(2, eleve.getMatricule());
                psEleve.setString(3, eleve.getClasse());
                psEleve.executeUpdate();
            } else if ("Enseignant".equals(p.getTypePatient()) && enseignant != null) {
                String sqlEnseignant = "INSERT INTO enseignant(id_patient, departement, fonction) VALUES (?,?,?)";
                PreparedStatement psEnseignant = conn.prepareStatement(sqlEnseignant);
                psEnseignant.setInt(1, idPatient);
                psEnseignant.setString(2, enseignant.getDepartement());
                psEnseignant.setString(3, enseignant.getFonction());
                psEnseignant.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.out.println("Erreur rollback: " + ex.getMessage());
            }
            System.out.println("Erreur insert patient: " + e.getMessage());
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

    public boolean update(Patient p, Eleve eleve, Enseignant enseignant) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;

        try {
            conn.setAutoCommit(false);

            // Update patient
            String sqlPatient = "UPDATE patient SET nom=?, sexe=?, date_naissance=?, type_patient=? WHERE id_patient=?";
            PreparedStatement psPatient = conn.prepareStatement(sqlPatient);
            psPatient.setString(1, p.getNom());
            psPatient.setString(2, String.valueOf(p.getSexe()));
            psPatient.setDate(3, p.getDateNaissance());
            psPatient.setString(4, p.getTypePatient());
            psPatient.setInt(5, p.getId());
            psPatient.executeUpdate();

            // Supprimer anciennes données
            PreparedStatement psDelEleve = conn.prepareStatement("DELETE FROM eleve WHERE id_patient=?");
            psDelEleve.setInt(1, p.getId());
            psDelEleve.executeUpdate();

            PreparedStatement psDelEnseignant = conn.prepareStatement("DELETE FROM enseignant WHERE id_patient=?");
            psDelEnseignant.setInt(1, p.getId());
            psDelEnseignant.executeUpdate();

            // Insert nouvelles données selon le type
            if ("Eleve".equals(p.getTypePatient()) && eleve != null) {
                String sqlEleve = "INSERT INTO eleve(id_patient, matricule, classe) VALUES (?,?,?)";
                PreparedStatement psEleve = conn.prepareStatement(sqlEleve);
                psEleve.setInt(1, p.getId());
                psEleve.setString(2, eleve.getMatricule());
                psEleve.setString(3, eleve.getClasse());
                psEleve.executeUpdate();
            } else if ("Enseignant".equals(p.getTypePatient()) && enseignant != null) {
                String sqlEnseignant = "INSERT INTO enseignant(id_patient, departement, fonction) VALUES (?,?,?)";
                PreparedStatement psEnseignant = conn.prepareStatement(sqlEnseignant);
                psEnseignant.setInt(1, p.getId());
                psEnseignant.setString(2, enseignant.getDepartement());
                psEnseignant.setString(3, enseignant.getFonction());
                psEnseignant.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.out.println("Erreur rollback: " + ex.getMessage());
            }
            System.out.println("Erreur update patient: " + e.getMessage());
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

    public boolean delete(int id) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;

        try {
            // La suppression en cascade s'occupe des tables eleve et enseignant
            String sql = "DELETE FROM patient WHERE id_patient=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int result = ps.executeUpdate();
            conn.close();
            return result == 1;
        } catch (SQLException e) {
            System.out.println("Erreur delete patient: " + e.getMessage());
            try {
                conn.close();
            } catch (SQLException ex) {
                System.out.println("Erreur fermeture connexion: " + ex.getMessage());
            }
            return false;
        }
    }
}

