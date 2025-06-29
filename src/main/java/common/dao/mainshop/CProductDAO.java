package common.dao.mainshop;

import common.tables.mainshop.CProduct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CProductDAO {

    // Database connection parameters - adjust these according to your setup
    private static final String DB_URL = "jdbc:mysql://localhost:3306/your_database_name";
    private static final String DB_USER = "your_username";
    private static final String DB_PASSWORD = "your_password";

    // Alternative constructor if you want to pass connection details
    private Connection getConnection() throws SQLException {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }

    /**
     * Retrieves all products from the database
     * @return List of all products, or empty list if none found
     */
    public List<CProduct> getAllProducts() {
        List<CProduct> products = new ArrayList<>();
        String sql = "SELECT id, name, price, quantity FROM product ORDER BY name";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CProduct product = new CProduct(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getFloat("price"),
                        rs.getInt("quantity")
                );
                products.add(product);
            }

            System.out.println("Retrieved " + products.size() + " products from database");

        } catch (SQLException e) {
            System.err.println("Error retrieving products: " + e.getMessage());
            e.printStackTrace();
        }

        return products;
    }

    /**
     * Retrieves a product by its ID
     * @param id Product ID
     * @return Product object or null if not found
     */
    public CProduct getProductById(int id) {
        String sql = "SELECT id, name, price, quantity FROM product WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new CProduct(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getFloat("price"),
                            rs.getInt("quantity")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving product with ID " + id + ": " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Adds a new product to the database
     * @param product Product to add
     * @return Generated product ID, or null if insertion failed
     */
    public Long addProduct(CProduct product) {
        String sql = "INSERT INTO product (name, price, quantity) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getName());
            stmt.setFloat(2, product.getPrice());
            stmt.setInt(3, product.getQuantity());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        Long generatedId = generatedKeys.getLong(1);
                        product.setId(generatedId.intValue()); // Update the product object with the generated ID
                        return generatedId;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Updates an existing product
     * @param product Product to update
     * @return true if update was successful, false otherwise
     */
    public boolean updateProduct(CProduct product) {
        String sql = "UPDATE product SET name = ?, price = ?, quantity = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setFloat(2, product.getPrice());
            stmt.setInt(3, product.getQuantity());
            stmt.setInt(4, product.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates product quantity (useful for inventory management)
     * @param productId Product ID
     * @param newQuantity New quantity
     * @return true if update was successful, false otherwise
     */
    public boolean updateProductQuantity(int productId, int newQuantity) {
        String sql = "UPDATE product SET quantity = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newQuantity);
            stmt.setInt(2, productId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating product quantity: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a product from the database
     * @param productId Product ID to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM product WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a product with the given name exists
     * @param productName Product name to check
     * @return true if product exists, false otherwise
     */
    public boolean productExists(String productName) {
        String sql = "SELECT COUNT(*) FROM product WHERE LOWER(name) = LOWER(?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productName.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error checking if product exists: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}