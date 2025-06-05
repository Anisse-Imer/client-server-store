package common.tables.mainshop;

public class Database {
    private int id;
    private String dbName;
    private int idShop;

    // Default constructor
    public Database() {}

    // Constructor with parameters
    public Database(String dbName, int idShop) {
        this.dbName = dbName;
        this.idShop = idShop;
    }

    // Constructor with all parameters
    public Database(int id, String dbName, int idShop) {
        this.id = id;
        this.dbName = dbName;
        this.idShop = idShop;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public int getIdShop() {
        return idShop;
    }

    public void setIdShop(int idShop) {
        this.idShop = idShop;
    }

    @Override
    public String toString() {
        return "Database{" +
                "id=" + id +
                ", dbName='" + dbName + '\'' +
                ", idShop=" + idShop +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Database database = (Database) o;
        return id == database.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}