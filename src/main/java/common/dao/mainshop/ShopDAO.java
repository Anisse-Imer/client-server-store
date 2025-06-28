package common.dao.mainshop;

import common.tables.mainshop.Shop;
import server.DbManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShopDAO {

    public Long addShop(Shop shop) {
        String sql = "INSERT INTO shop (shop_name) VALUES (?)";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, shop.getShopName());

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Lignes affectées: " + rowsAffected);

            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        long generatedId = rs.getLong(1);
                        shop.setId((int) generatedId);
                        System.out.println("ID généré (dans DAO) : " + generatedId);
                        return generatedId;
                    } else {
                        System.out.println("Aucune clé générée.");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Shop getShopById(int id) {
        String sql = "SELECT * FROM shop WHERE id = ?";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Shop shop = new Shop(
                        rs.getInt("id"),
                        rs.getString("shop_name")
                );
                return shop;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Shop> getAllShops() {
        List<Shop> shops = new ArrayList<>();
        String sql = "SELECT * FROM shop";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Shop shop = new Shop(
                        rs.getInt("id"),
                        rs.getString("shop_name")
                );
                shops.add(shop);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shops;
    }

    public boolean updateShop(Shop shop) {
        String sql = "UPDATE shop SET shop_name = ? WHERE id = ?";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, shop.getShopName());
            stmt.setInt(2, shop.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteShop(int id) {
        String sql = "DELETE FROM shop WHERE id = ?";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean shopExists(String shopName) {
        String sql = "SELECT COUNT(*) FROM shop WHERE shop_name = ?";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, shopName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}