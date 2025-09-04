package com.example.storegui.model;

import com.example.storegui.customer.Customer;
import com.example.storegui.order.Order;
import com.example.storegui.product.Product;

import java.io.Serializable;
import java.util.ArrayList;

// This class is used to store data for the store application.
public class StoreData implements Serializable {
    private ArrayList<Customer> customers;
    private ArrayList<Product> products;
    private ArrayList<Order> orders;

    // Constructor
    public StoreData() {
        this.customers = new ArrayList<>();
        this.products = new ArrayList<>();
        this.orders = new ArrayList<>();
    }

    // Getters and setters
    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }
}
