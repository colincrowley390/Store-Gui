package com.example.storegui.customer;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

// Represents a customer in the store system
public class Customer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String name;  // Customer's full name
    private final String email; // Customer's email address
    private final LocalDate dob;   // Customer's date of birth

    // Private constructor to enforce the use of the Builder
    private Customer(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.dob = builder.dob;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDob() {
        return dob;
    }

    // No setters as Customers cannot be changed once added

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(name, customer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    // Format: "Customer - Email - dob"
    @Override
    public String toString() {
        return name + " | " + email + " | " + dob;
    }

    // Static nested Builder class
    public static class Builder {
        private String name;
        private String email;
        private LocalDate dob;

        // Setter for name
        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        // Setter for email
        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        // Setter for date of birth
        public Builder setDob(LocalDate dob) {
            this.dob = dob;
            return this;
        }

        // Build method to create a Customer object
        public Customer build() {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be null or empty.");
            }
            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("Email cannot be null or empty.");
            }
            if (dob == null) {
                throw new IllegalArgumentException("Date of birth cannot be null.");
            }
            return new Customer(this);
        }
    }
}