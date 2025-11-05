package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/pharmacie_scolaire";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connexion PostgreSQL réussie !");
            return conn;
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Driver PostgreSQL introuvable : " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.out.println("❌ Erreur de connexion : " + e.getMessage());
            return null;
        }
    }
}
