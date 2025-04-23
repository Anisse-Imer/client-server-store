package server;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbManager {
    private static final String URL = "jdbc:mysql://localhost:3306/store_bdd";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String SCHEMA_FILE = "resources/init.sql"; // Path to your SQL schema file

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

            // Read SQL file content
            StringBuilder sqlScript = new StringBuilder();

            // Option 1: Try to load as resource from classpath first
            try (InputStream inputStream = DbManager.class.getClassLoader().getResourceAsStream("init.sql")) {
                if (inputStream != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sqlScript.append(line).append("\n");
                        }
                    }
                } else {
                    // Option 2: Try to load from file system if not found in classpath
                    // First try in the project root directory
                    File sqlFile = new File("init.sql");
                    if (!sqlFile.exists()) {
                        // Then try in the src/main/resources directory
                        sqlFile = new File("src/main/resources/init.sql");
                    }

                    if (sqlFile.exists()) {
                        try (BufferedReader reader = new BufferedReader(new FileReader(sqlFile))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                sqlScript.append(line).append("\n");
                            }
                        }
                    } else {
                        System.err.println("Fichier SQL introuvable. Création d'un script SQL par défaut.");
                        // Provide default SQL script inline if file not found
                        sqlScript.append("-- Drop tables if they exist to start fresh\n");
                        sqlScript.append("DROP TABLE IF EXISTS invoice_product;\n");
                        sqlScript.append("DROP TABLE IF EXISTS product;\n");
                        sqlScript.append("DROP TABLE IF EXISTS family;\n");
                        sqlScript.append("DROP TABLE IF EXISTS invoice;\n\n");

                        sqlScript.append("-- Create family table\n");
                        sqlScript.append("CREATE TABLE family (\n");
                        sqlScript.append("    id INT AUTO_INCREMENT PRIMARY KEY,\n");
                        sqlScript.append("    name VARCHAR(255) NOT NULL\n");
                        sqlScript.append(");\n\n");

                        sqlScript.append("-- Create product table\n");
                        sqlScript.append("CREATE TABLE product (\n");
                        sqlScript.append("    id INT AUTO_INCREMENT PRIMARY KEY,\n");
                        sqlScript.append("    id_family INT NOT NULL,\n");
                        sqlScript.append("    name VARCHAR(255) NOT NULL,\n");
                        sqlScript.append("    price FLOAT NOT NULL,\n");
                        sqlScript.append("    quantity INT NOT NULL,\n");
                        sqlScript.append("    FOREIGN KEY (id_family) REFERENCES family(id)\n");
                        sqlScript.append(");\n\n");

                        sqlScript.append("-- Create invoice table\n");
                        sqlScript.append("CREATE TABLE invoice (\n");
                        sqlScript.append("    id INT AUTO_INCREMENT PRIMARY KEY,\n");
                        sqlScript.append("    price FLOAT NOT NULL,\n");
                        sqlScript.append("    payment_method VARCHAR(255) NOT NULL,\n");
                        sqlScript.append("    date DATE NOT NULL,\n");
                        sqlScript.append("    paid BOOLEAN NOT NULL DEFAULT FALSE\n");
                        sqlScript.append(");\n\n");

                        sqlScript.append("-- Create invoice_product junction table\n");
                        sqlScript.append("CREATE TABLE invoice_product (\n");
                        sqlScript.append("    id INT AUTO_INCREMENT PRIMARY KEY,\n");
                        sqlScript.append("    product_id INT NOT NULL,\n");
                        sqlScript.append("    id_invoice INT NOT NULL,\n");
                        sqlScript.append("    price FLOAT NOT NULL,\n");
                        sqlScript.append("    FOREIGN KEY (product_id) REFERENCES product(id),\n");
                        sqlScript.append("    FOREIGN KEY (id_invoice) REFERENCES invoice(id)\n");
                        sqlScript.append(");\n\n");
                    }
                }
            }

            // Split SQL script by semicolons to execute each statement separately
            String[] sqlStatements = sqlScript.toString().split(";");

            for (String sql : sqlStatements) {
                sql = sql.trim();
                if (!sql.isEmpty()) {
                    try {
                        statement.execute(sql);
                    } catch (SQLException e) {
                        System.err.println("Erreur lors de l'exécution de la requête SQL: " + sql);
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("Base de données initialisée avec succès !");

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation de la base de données !");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier SQL !");
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