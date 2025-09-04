package com.example.storegui.product;

import java.io.Serial;
import java.io.Serializable;

// Represents a product in the store system
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;        // Product name
    private double price;       // Price of the product
    private int stock;          // Available stock quantity
    private String description; // Short description of the product

    // Constructor
    public Product(String name, double price, int stock, String description) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getDescription() {
        return description;
    }

    // Setter methods
    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Format: "Customer - Product - Stock" , description can be seen when pressing on the item
    @Override
    public String toString() {
        return name + " - €" + price + " (" + stock + " in stock)";
    }
}
