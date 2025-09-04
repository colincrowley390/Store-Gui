package com.example.storegui.customer;

import javafx.scene.control.*;
import javafx.scene.layout.*;

// Represents the UI of the Customer Tab in the store system
public class CustomerView {

    // Same fields as Customer
    private final TextField nameField, emailField;
    private final DatePicker dobField;

    // Buttons for Adding, Listing, Saving, and Removing customers
    private final Button addButton, listButton, saveButton, loadButton, removeButton, saveToDatabase, loadFromDatabase;

    // ListView to display customers
    private final ListView<Customer> customerListView = new ListView<>();

    public CustomerView() {
        // UI Elements

        // Setting prompts and buttons
        nameField = new TextField();
        nameField.setPromptText("Enter Name");

        emailField = new TextField();
        emailField.setPromptText("Enter Email");

        dobField = new DatePicker();
        dobField.setPromptText("Enter Date of Birth");

        addButton = new Button("Add Customer");
        Tooltip addTooltip = new Tooltip("Fill in the details for the Customer you want to add");
        addButton.setTooltip(addTooltip);

        listButton = new Button("List Customers");
        Tooltip listTooltip = new Tooltip("Click to see all the customers");
        listButton.setTooltip(listTooltip);

        saveButton = new Button("Save Customers");
        Tooltip saveTooltip = new Tooltip("Click to save the customers added");
        saveButton.setTooltip(saveTooltip);

        loadButton = new Button("Load Customers");
        Tooltip loadToolTip = new Tooltip("Click to load the customers added");
        loadButton.setTooltip(loadToolTip);

        saveToDatabase = new Button("Save Customers to Database");
        Tooltip saveDatabase = new Tooltip("Click to save the customers added to a database");
        saveToDatabase.setTooltip(saveDatabase);

        loadFromDatabase = new Button("Load Customers from Database");
        Tooltip loadDatabase = new Tooltip("Click to load the customers in the database");
        loadFromDatabase.setTooltip(loadDatabase);

        removeButton = new Button("Remove Customer");
        Tooltip removeTooltip = new Tooltip("Click on the Customer you want to remove");
        removeButton.setTooltip(removeTooltip);

        // Set ListView Display
        customerListView.setPrefHeight(150);
    }

    public VBox getView() {
        // Create Title
        Label titleLabel = new Label("Customer Management");

        // Organize elements horizontally with spacing
        HBox inputBox = new HBox(10, nameField, emailField, dobField);
        HBox serializeBox = new HBox(10, saveButton, loadButton);
        HBox databaseBox = new HBox(10, saveToDatabase, loadFromDatabase);

        // Organize elements vertically with padding
        VBox root = new VBox(10, titleLabel, inputBox, addButton, customerListView, serializeBox, databaseBox, listButton, removeButton);
        root.setPadding(new javafx.geometry.Insets(10));

        // HBox and Vbox returned as the View
        return root;
    }

    // Getters for UI components
    public Button getAddButton() { return addButton; }
    public Button getListButton() { return listButton; }
    public Button getSaveButton() { return saveButton; }
    public Button getLoadButton() { return loadButton; }
    public Button getRemoveButton() { return removeButton; }
    public TextField getNameField() { return nameField; }
    public TextField getEmailField() { return emailField; }
    public DatePicker getDobField() { return dobField; }
    public ListView<Customer> getCustomerListView() { return customerListView; }
    public Button getSaveToDatabase() { return saveToDatabase; }
    public Button getLoadFromDatabase() { return loadFromDatabase; }
}
