package main;

import database.DBConnection;
import java.sql.Connection;
import javax.swing.SwingUtilities;
import view.Login;

public class App {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("🎉 Base de données connectée avec succès !");
        } else {
            System.out.println("⚠️ Échec de la connexion à la base de données.");
        }

        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setVisible(true);
        });
    }
}
