package common.tables;

public class Product {
    private int id;
    private int id_family;
    private String name;
    private double price;
    private int quantity;

    public Product() {}
    public Product(int id, int id_family, String name, double price, int quantity) {
        this.id = id;
        this.id_family = id_family;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
