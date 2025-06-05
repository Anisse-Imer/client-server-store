package common.tables.mainshop;

public class InvoiceDetail {
    private int id;
    private int idDetailCopy;
    private int idInvoiceCopy;
    private int idInvoice;
    private int productId;
    private float price;
    private int quantity;

    // Default constructor
    public InvoiceDetail() {}

    // Constructor with parameters
    public InvoiceDetail(int idDetailCopy, int idInvoiceCopy, int idInvoice, int productId, float price, int quantity) {
        this.idDetailCopy = idDetailCopy;
        this.idInvoiceCopy = idInvoiceCopy;
        this.idInvoice = idInvoice;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    // Constructor with all parameters
    public InvoiceDetail(int id, int idDetailCopy, int idInvoiceCopy, int idInvoice, int productId, float price, int quantity) {
        this.id = id;
        this.idDetailCopy = idDetailCopy;
        this.idInvoiceCopy = idInvoiceCopy;
        this.idInvoice = idInvoice;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdDetailCopy() {
        return idDetailCopy;
    }

    public void setIdDetailCopy(int idDetailCopy) {
        this.idDetailCopy = idDetailCopy;
    }

    public int getIdInvoiceCopy() {
        return idInvoiceCopy;
    }

    public void setIdInvoiceCopy(int idInvoiceCopy) {
        this.idInvoiceCopy = idInvoiceCopy;
    }

    public int getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(int idInvoice) {
        this.idInvoice = idInvoice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "InvoiceDetail{" +
                "id=" + id +
                ", idDetailCopy=" + idDetailCopy +
                ", idInvoiceCopy=" + idInvoiceCopy +
                ", idInvoice=" + idInvoice +
                ", productId=" + productId +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceDetail that = (InvoiceDetail) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}