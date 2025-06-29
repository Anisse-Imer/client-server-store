package common.dao.mainshop;

import common.tables.Invoice;
import server.DbManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShopInvoiceDAO {

    public Long addShopInvoice(int idCopy, int shopId, Invoice invoice) {
        String sql = "INSERT INTO shop_invoice (id_copy, id_shop, price, payment_method, date, paid) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, idCopy);
            stmt.setInt(2, shopId);
            stmt.setFloat(3, invoice.getPrice());
            stmt.setString(4, invoice.getPayment_method());
            stmt.setDate(5, Date.valueOf(invoice.getDate().toLocalDate()));
            stmt.setBoolean(6, invoice.isPaid());

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Lignes affectées: " + rowsAffected);

            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        long generatedId = rs.getLong(1);
                        invoice.setId((int) generatedId);
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

    public Invoice getShopInvoiceById(int id) {
        String sql = "SELECT * FROM shop_invoice WHERE id = ?";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Invoice invoice = new Invoice(
                        rs.getFloat("price"),
                        rs.getString("payment_method"),
                        rs.getDate("date").toLocalDate().atStartOfDay(),
                        rs.getBoolean("paid")
                );
                invoice.setId(rs.getInt("id"));
                return invoice;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Invoice> getAllShopInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM shop_invoice";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Invoice invoice = new Invoice(
                        rs.getFloat("price"),
                        rs.getString("payment_method"),
                        rs.getDate("date").toLocalDate().atStartOfDay(),
                        rs.getBoolean("paid")
                );
                invoice.setId(rs.getInt("id"));
                invoices.add(invoice);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    public List<Invoice> getShopInvoicesByShopId(int shopId) {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM shop_invoice WHERE id_shop = ?";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, shopId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Invoice invoice = new Invoice(
                        rs.getFloat("price"),
                        rs.getString("payment_method"),
                        rs.getDate("date").toLocalDate().atStartOfDay(),
                        rs.getBoolean("paid")
                );
                invoice.setId(rs.getInt("id"));
                invoices.add(invoice);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    public List<Invoice> getPaidInvoices(int shopId) {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM shop_invoice WHERE id_shop = ? AND paid = TRUE";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, shopId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Invoice invoice = new Invoice(
                        rs.getFloat("price"),
                        rs.getString("payment_method"),
                        rs.getDate("date").toLocalDate().atStartOfDay(),
                        rs.getBoolean("paid")
                );
                invoice.setId(rs.getInt("id"));
                invoices.add(invoice);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    public List<Invoice> getUnpaidInvoices(int shopId) {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM shop_invoice WHERE id_shop = ? AND paid = FALSE";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, shopId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Invoice invoice = new Invoice(
                        rs.getFloat("price"),
                        rs.getString("payment_method"),
                        rs.getDate("date").toLocalDate().atStartOfDay(),
                        rs.getBoolean("paid")
                );
                invoice.setId(rs.getInt("id"));
                invoices.add(invoice);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    public boolean updateShopInvoice(int id, int idCopy, int shopId, Invoice invoice) {
        String sql = "UPDATE shop_invoice SET id_copy = ?, id_shop = ?, price = ?, payment_method = ?, date = ?, paid = ? WHERE id = ?";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCopy);
            stmt.setInt(2, shopId);
            stmt.setFloat(3, invoice.getPrice());
            stmt.setString(4, invoice.getPayment_method());
            stmt.setDate(5, Date.valueOf(invoice.getDate().toLocalDate()));
            stmt.setBoolean(6, invoice.isPaid());
            stmt.setInt(7, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean markInvoiceAsPaid(int id) {
        String sql = "UPDATE shop_invoice SET paid = TRUE WHERE id = ?";
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

    public boolean markInvoiceAsUnpaid(int id) {
        String sql = "UPDATE shop_invoice SET paid = FALSE WHERE id = ?";
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

    public boolean deleteShopInvoice(int id) {
        String sql = "DELETE FROM shop_invoice WHERE id = ?";
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

    public double getTotalRevenueByShop(int shopId) {
        String sql = "SELECT SUM(price) FROM shop_invoice WHERE id_shop = ? AND paid = TRUE";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, shopId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public int getInvoiceCountByShop(int shopId) {
        String sql = "SELECT COUNT(*) FROM shop_invoice WHERE id_shop = ?";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, shopId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Invoice> getInvoicesByDateRange(int shopId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM shop_invoice WHERE id_shop = ? AND date >= ? AND date <= ?";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, shopId);
            stmt.setDate(2, Date.valueOf(startDate.toLocalDate()));
            stmt.setDate(3, Date.valueOf(endDate.toLocalDate()));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Invoice invoice = new Invoice(
                        rs.getFloat("price"),
                        rs.getString("payment_method"),
                        rs.getDate("date").toLocalDate().atStartOfDay(),
                        rs.getBoolean("paid")
                );
                invoice.setId(rs.getInt("id"));
                invoices.add(invoice);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    public boolean shopInvoiceExists(int idCopy, int shopId) {
        String sql = "SELECT COUNT(*) FROM shop_invoice WHERE id_copy = ? AND id_shop = ?";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCopy);
            stmt.setInt(2, shopId);
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