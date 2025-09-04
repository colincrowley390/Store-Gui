package com.example.storegui.order;

import com.example.storegui.customer.Customer;
import com.example.storegui.product.Product;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

// Represents an Order in the store system
public class Order implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Customer customer; // The customer who placed the order
    private final Product product;   // The product being ordered
    private final int quantity;      // The quantity of the product ordered
    private final LocalDate date;    // The date the order was placed

    // Constructor
    public Order(Customer customer, Product product, int quantity, LocalDate date) {
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
        this.date = date;
    }

    // Getter methods
    public Customer getCustomer() {
        return customer;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getDate() {
        return date;
    }

    // No setters as Orders cannot be changed once made

    // Format: "Customer - Product xQuantity (Date)"
    @Override
    public String toString() {
        return customer + " - " + product + " x" + quantity + " (" + date + ")";
    }
}
