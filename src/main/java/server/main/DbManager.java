package server.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbManager {
    private static final String URL = "jdbc:mysql://localhost:3306/main_bdd";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Charger le driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion réussie à la base de données !");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur : Driver MySQL non trouvé !");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erreur : Impossible de se connecter à la base de données !");
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connexion fermée.");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion !");
                e.printStackTrace();
            }
        }
    }

    public static void init() {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();

            // Execute SQL statements directly
            String[] sqlStatements = {
                    "DROP TABLE IF EXISTS invoice_detail",
                    "DROP TABLE IF EXISTS shop_invoice",
                    "DROP TABLE IF EXISTS product",
                    "DROP TABLE IF EXISTS shop",
                    "DROP TABLE IF EXISTS database",

                    "CREATE TABLE database (id INT AUTO_INCREMENT PRIMARY KEY, db_name VARCHAR(255) NOT NULL, id_shop INT NOT NULL)",

                    "CREATE TABLE shop (id INT AUTO_INCREMENT PRIMARY KEY, shop_name VARCHAR(255) NOT NULL)",

                    "CREATE TABLE product (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) NOT NULL, price FLOAT NOT NULL, quantity INT NOT NULL)",

                    "CREATE TABLE shop_invoice (id INT AUTO_INCREMENT PRIMARY KEY, id_copy INT NOT NULL, id_shop INT NOT NULL, price FLOAT NOT NULL, payment_method VARCHAR(255) NOT NULL, date DATE NOT NULL, paid BOOLEAN NOT NULL DEFAULT FALSE, FOREIGN KEY (id_shop) REFERENCES shop(id))",

                    "CREATE TABLE invoice_detail (id INT AUTO_INCREMENT PRIMARY KEY, id_detail_copy INT NOT NULL, id_invoice_copy INT NOT NULL, id_invoice INT NOT NULL, product_id INT NOT NULL, price FLOAT NOT NULL, quantity INT NOT NULL, FOREIGN KEY (id_invoice) REFERENCES shop_invoice(id), FOREIGN KEY (product_id) REFERENCES product(id))",

                    "ALTER TABLE database ADD CONSTRAINT fk_database_shop FOREIGN KEY (id_shop) REFERENCES shop(id)"
            };

            for (String sql : sqlStatements) {
                try {
                    statement.execute(sql);
                } catch (SQLException e) {
                    System.err.println("Erreur lors de l'exécution de la requête SQL: " + sql);
                    e.printStackTrace();
                }
            }

            System.out.println("Base de données initialisée avec succès !");

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation de la base de données !");
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}