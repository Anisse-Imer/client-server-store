package server.main;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CDbManager {
    private static final String URL = "jdbc:mysql://localhost:3306/main_bdd";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String SCHEMA_FILE = "resources/init-centre.sql"; // Path to your SQL schema file

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
            try (InputStream inputStream = CDbManager.class.getClassLoader().getResourceAsStream("init-centre.sql")) {
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
                    File sqlFile = new File("init-centre.sql");
                    if (!sqlFile.exists()) {
                        // Then try in the src/main/resources directory
                        sqlFile = new File("src/main/resources/init-centre.sql");
                    }

                    if (sqlFile.exists()) {
                        try (BufferedReader reader = new BufferedReader(new FileReader(sqlFile))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                sqlScript.append(line).append("\n");
                            }
                        }
                    } else {
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