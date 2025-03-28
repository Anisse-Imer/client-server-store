package common.tables;

import java.io.Serializable;
import java.util.Date;

public class Invoice implements Serializable {
    private int id;
    private float price;
    private String payment_method;
    private Date date;
    private boolean paid;

    public Invoice(int id, float price, String payment_method, Date date, boolean paid) {
        this.id = id;
        this.price = price;
        this.payment_method = payment_method;
        this.date = date;
        this.paid = paid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", price=" + price +
                ", payment_method='" + payment_method + '\'' +
                ", date=" + date +
                ", paid=" + paid +
                '}';
    }
}
