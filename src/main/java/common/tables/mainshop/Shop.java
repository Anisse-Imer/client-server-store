package common.tables.mainshop;

public class Shop {
    private int id;
    private String shopName;

    // Default constructor
    public Shop() {}

    // Constructor with parameters
    public Shop(String shopName) {
        this.shopName = shopName;
    }

    // Constructor with all parameters
    public Shop(int id, String shopName) {
        this.id = id;
        this.shopName = shopName;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", shopName='" + shopName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shop shop = (Shop) o;
        return id == shop.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}