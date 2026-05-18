package model;

/**
 * Represents a Product entity in the inventory system.
 * Keeps track of item details, sale price, and current stock availability.
 * * @author Yasmina
 * @version 1.0
 */
public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;

    /**
     * Default constructor for creating an empty Product instance.
     * Utilized by the data access layer's reflection factory.
     */
    public Product(){
    }

    /**
     * Constructs a full Product instance with an explicit database identifier.
     * Used for updating information or managing active catalog records.
     *
     * @param id    The unique identifier of the product.
     * @param name  The designation name of the product.
     * @param price The decimal monetary unit cost of the product.
     * @param stock The current quantity left in inventory.
     */
    public Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    /**
     * Constructs a new Product without an ID.
     * Used for adding a fresh item configuration to the storage subsystem.
     *
     * @param name  The designation name of the product.
     * @param price The decimal monetary unit cost of the product.
     * @param stock The initial physical quantity available.
     */
    public Product(String name, double price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", price=" + price + ", stock=" + stock + "]";
    }
}
