package common.dao;

import common.tables.Product;
import server.DbManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public boolean addProduct(Product product) {
        String sql = "INSERT INTO product (id_family, name, price, quantity) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, product.getId_family());
            stmt.setString(2, product.getName());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());

            ResultSet rowsAffected = stmt.executeQuery();
            System.out.println("Success" + rowsAffected.first());
            return rowsAffected.first(); // Retourne vrai si l'ajout a réussi

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProduct(Product product) {
        double price = product.getPrice();
        int id = product.getId();
        String sql = "UPDATE product SET price = ? WHERE id = ?";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, product.getId_family());
            stmt.setString(2, product.getName());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());

            ResultSet rowsAffected = stmt.executeQuery();
            System.out.println("Success" + rowsAffected.first());
            return rowsAffected.first(); // Retourne vrai si l'ajout a réussi

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Product getProductById(int id) {
        String sql = "SELECT * FROM product WHERE id = ?";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Product(
                        rs.getInt("id"),
                        rs.getInt("id_family"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getInt("id_family"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public boolean updateProductPrice(int id, double newPrice) {
        String sql = "UPDATE product SET price = ? WHERE id = ?";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newPrice);
            stmt.setInt(2, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retourne vrai si la mise à jour a réussi

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateProductQuantity(int id, int newQuantity) {
        String sql = "UPDATE product SET quantity = ? WHERE id = ?";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newQuantity);
            stmt.setInt(2, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retourne vrai si la mise à jour a réussi

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM product WHERE id = ?";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retourne vrai si la suppression a réussi

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addProduct2(Product product) {
        String sql = "INSERT INTO product (id_family, name, price, quantity) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, product.getId_family());
            stmt.setString(2, product.getName());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProduct2(Product product) {
        String sql = "UPDATE store_bdd.product SET name = ?, price = ?, quantity = ? WHERE id = ?;";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getQuantity());
            stmt.setInt(4, product.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
