package common.dao.mainshop;

import common.tables.InvoiceDetail;
import server.DbManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailDAO {

    public Long addInvoiceDetail(int idDetailCopy, int idInvoiceCopy, InvoiceDetail invoiceDetail) {
        String sql = "INSERT INTO invoice_detail (id_detail_copy, id_invoice_copy, id_invoice, product_id, price, quantity) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, idDetailCopy);
            stmt.setInt(2, idInvoiceCopy);
            stmt.setLong(3, invoiceDetail.getId_invoice());
            stmt.setInt(4, invoiceDetail.getId_product());
            stmt.setFloat(5, invoiceDetail.getPrice());
            stmt.setInt(6, invoiceDetail.getQuantity());

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Lignes affectées: " + rowsAffected);

            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        long generatedId = rs.getLong(1);
                        invoiceDetail.setId((int) generatedId);
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

    public InvoiceDetail getInvoiceDetailById(int id) {
        String sql = "SELECT * FROM invoice_detail WHERE id = ?";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                InvoiceDetail invoiceDetail = new InvoiceDetail(
                        rs.getInt("product_id"),
                        rs.getLong("id_invoice"),
                        rs.getFloat("price"),
                        rs.getInt("quantity")
                );
                invoiceDetail.setId(rs.getInt("id"));
                return invoiceDetail;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<InvoiceDetail> getAllInvoiceDetails() {
        List<InvoiceDetail> invoiceDetails = new ArrayList<>();
        String sql = "SELECT * FROM invoice_detail";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                InvoiceDetail invoiceDetail = new InvoiceDetail(
                        rs.getInt("product_id"),
                        rs.getLong("id_invoice"),
                        rs.getFloat("price"),
                        rs.getInt("quantity")
                );
                invoiceDetail.setId(rs.getInt("id"));
                invoiceDetails.add(invoiceDetail);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoiceDetails;
    }

    public List<InvoiceDetail> getInvoiceDetailsByInvoiceId(Long invoiceId) {
        List<InvoiceDetail> invoiceDetails = new ArrayList<>();
        String sql = "SELECT * FROM invoice_detail WHERE id_invoice = ?";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, invoiceId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                InvoiceDetail invoiceDetail = new InvoiceDetail(
                        rs.getInt("product_id"),
                        rs.getLong("id_invoice"),
                        rs.getFloat("price"),
                        rs.getInt("quantity")
                );
                invoiceDetail.setId(rs.getInt("id"));
                invoiceDetails.add(invoiceDetail);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoiceDetails;
    }

    public List<InvoiceDetail> getInvoiceDetailsByProductId(int productId) {
        List<InvoiceDetail> invoiceDetails = new ArrayList<>();
        String sql = "SELECT * FROM invoice_detail WHERE product_id = ?";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                InvoiceDetail invoiceDetail = new InvoiceDetail(
                        rs.getInt("product_id"),
                        rs.getLong("id_invoice"),
                        rs.getFloat("price"),
                        rs.getInt("quantity")
                );
                invoiceDetail.setId(rs.getInt("id"));
                invoiceDetails.add(invoiceDetail);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoiceDetails;
    }

    public boolean updateInvoiceDetail(int id, int idDetailCopy, int idInvoiceCopy, InvoiceDetail invoiceDetail) {
        String sql = "UPDATE invoice_detail SET id_detail_copy = ?, id_invoice_copy = ?, id_invoice = ?, product_id = ?, price = ?, quantity = ? WHERE id = ?";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idDetailCopy);
            stmt.setInt(2, idInvoiceCopy);
            stmt.setLong(3, invoiceDetail.getId_invoice());
            stmt.setInt(4, invoiceDetail.getId_product());
            stmt.setFloat(5, invoiceDetail.getPrice());
            stmt.setInt(6, invoiceDetail.getQuantity());
            stmt.setInt(7, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteInvoiceDetail(int id) {
        String sql = "DELETE FROM invoice_detail WHERE id = ?";
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

    public boolean deleteInvoiceDetailsByInvoiceId(Long invoiceId) {
        String sql = "DELETE FROM invoice_detail WHERE id_invoice = ?";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, invoiceId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getTotalAmountByInvoice(Long invoiceId) {
        String sql = "SELECT SUM(price * quantity) FROM invoice_detail WHERE id_invoice = ?";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, invoiceId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public int getTotalQuantityByInvoice(Long invoiceId) {
        String sql = "SELECT SUM(quantity) FROM invoice_detail WHERE id_invoice = ?";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, invoiceId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getDetailCountByInvoice(Long invoiceId) {
        String sql = "SELECT COUNT(*) FROM invoice_detail WHERE id_invoice = ?";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, invoiceId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean invoiceDetailExists(int idDetailCopy, int idInvoiceCopy) {
        String sql = "SELECT COUNT(*) FROM invoice_detail WHERE id_detail_copy = ? AND id_invoice_copy = ?";
        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idDetailCopy);
            stmt.setInt(2, idInvoiceCopy);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<InvoiceDetail> getInvoiceDetailsByInvoiceCopyId(int idInvoiceCopy) {
        List<InvoiceDetail> invoiceDetails = new ArrayList<>();
        String sql = "SELECT * FROM invoice_detail WHERE id_invoice_copy = ?";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idInvoiceCopy);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                InvoiceDetail invoiceDetail = new InvoiceDetail(
                        rs.getInt("product_id"),
                        rs.getLong("id_invoice"),
                        rs.getFloat("price"),
                        rs.getInt("quantity")
                );
                invoiceDetail.setId(rs.getInt("id"));
                invoiceDetails.add(invoiceDetail);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoiceDetails;
    }
}