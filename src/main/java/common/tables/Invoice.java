package common.tables;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class Invoice implements Serializable {
    private int id;
    private float price;
    private String payment_method;
    private LocalDateTime  date;
    private boolean paid;

    public Invoice( float price, String payment_method, LocalDateTime  date, boolean paid) {
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime  date) {
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

    public void setTotalAmount(double totalAmount) {
    }
}
