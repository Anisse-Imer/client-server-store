package common.tables;

import java.io.Serializable;

public class InvoiceDetail implements Serializable {
    private int id;
    private int id_product;
    private Long id_invoice;
    private float price;
    private int quantity;

    public InvoiceDetail() {

    }

    public int getId() {
        return id;
    }

    public InvoiceDetail(int id_product, Long id_invoice, float price, int quantity) {
        this.id_product = id_product;
        this.id_invoice = id_invoice;
        this.price = price;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public Long getId_invoice() {
        return id_invoice;
    }

    public void setId_invoice(Long id_invoice) {
        this.id_invoice = id_invoice;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "InvoiceDetail{" +
                "id=" + id +
                ", id_product=" + id_product +
                ", id_invoice=" + id_invoice +
                ", price=" + price +
                '}';
    }
}
