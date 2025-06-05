package common.tables.mainshop;

import java.sql.Date;

public class ShopInvoice {
    private int id;
    private int idCopy;
    private int idShop;
    private float price;
    private String paymentMethod;
    private Date date;
    private boolean paid;

    // Default constructor
    public ShopInvoice() {}

    // Constructor with parameters
    public ShopInvoice(int idCopy, int idShop, float price, String paymentMethod, Date date, boolean paid) {
        this.idCopy = idCopy;
        this.idShop = idShop;
        this.price = price;
        this.paymentMethod = paymentMethod;
        this.date = date;
        this.paid = paid;
    }

    // Constructor with all parameters
    public ShopInvoice(int id, int idCopy, int idShop, float price, String paymentMethod, Date date, boolean paid) {
        this.id = id;
        this.idCopy = idCopy;
        this.idShop = idShop;
        this.price = price;
        this.paymentMethod = paymentMethod;
        this.date = date;
        this.paid = paid;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCopy() {
        return idCopy;
    }

    public void setIdCopy(int idCopy) {
        this.idCopy = idCopy;
    }

    public int getIdShop() {
        return idShop;
    }

    public void setIdShop(int idShop) {
        this.idShop = idShop;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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
        return "ShopInvoice{" +
                "id=" + id +
                ", idCopy=" + idCopy +
                ", idShop=" + idShop +
                ", price=" + price +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", date=" + date +
                ", paid=" + paid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopInvoice that = (ShopInvoice) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}