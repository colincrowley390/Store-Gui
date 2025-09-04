package com.example.storegui.customer;

import com.example.storegui.database.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // Save a single customer to the database
    public static void saveCustomer(Customer customer) throws SQLException {
        // Validate customer object
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        if (customer.getName() == null || customer.getName().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty.");
        }
        if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Customer email cannot be null or empty.");
        }
        if (customer.getDob() == null) {
            throw new IllegalArgumentException("Customer date of birth cannot be null.");
        }

        // Prepare SQL statement
        String sql = "INSERT INTO Customer (name, email, dob) VALUES (?, ?, ?)";
        // Use try-with-resources to ensure the connection and statement are closed
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set parameters
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setDate(3, Date.valueOf(customer.getDob()));
            statement.executeUpdate();
        }
    }

    // Load all customers from the database
    public static List<Customer> loadAllCustomers() throws SQLException {
        String sql = "SELECT name, email, dob FROM Customer"; // SQL query to select all customers
        List<Customer> customers = new ArrayList<>(); // List to hold customer objects

        // Use try-with-resources to ensure the connection, statement, and result set are closed
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            // Iterate through the result set and create customer objects
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                Date dobDate = resultSet.getDate("dob");

                // Validate retrieved data
                if (name == null || name.isEmpty() || email == null || email.isEmpty() || dobDate == null) {
                    throw new SQLException("Invalid data retrieved from the database.");
                }

                // Create a new customer object
                LocalDate dob = dobDate.toLocalDate();
                Customer customer = new Customer.Builder() // Use Builder pattern for creating customer objects
                        .setName(name)
                        .setEmail(email)
                        .setDob(dob)
                        .build();
                customers.add(customer);
            }
        }
        return customers;
    }
}