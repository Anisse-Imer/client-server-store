package common.tables;

import java.io.Serializable;

public class InvoiceDetail implements Serializable {
    private int id;
    private int id_product;
    private int id_invoice;
    private float price;

    private int quantity;

    public int getId() {
        return id;
    }

    public InvoiceDetail(int id, int id_product, int id_invoice, float price) {
        this.id = id;
        this.id_product = id_product;
        this.id_invoice = id_invoice;
        this.price = price;
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

    public int getId_invoice() {
        return id_invoice;
    }

    public void setId_invoice(int id_invoice) {
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
