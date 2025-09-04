package com.example.storegui.product;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// Represents the UI of the Product Tab in the store system
public class ProductView {

    // Same fields as Product
    private final TextField nameField, priceField, stockField, descriptionField;

    // Buttons for Adding, Editing, Viewing, and Listing and Removing products
    private final Button addButton, editButton, viewButton, listButton, removeButton, saveButton, refreshButton, loadButton;

    // Variable for viewing the product list in a list format
    private final ListView<Product> productListView = new ListView<>();

    public ProductView() {
        // UI Elements

        // Setting prompts and buttons
        nameField = new TextField();
        nameField.setPromptText("Enter Name");

        priceField = new TextField();
        priceField.setPromptText("Enter Price");

        stockField = new TextField();
        stockField.setPromptText("Enter Stock amount");

        descriptionField = new TextField();
        descriptionField.setPromptText("Enter Description");

        addButton = new Button("Add Product");
        Tooltip addTooltip = new Tooltip("Fill in the details for the Product you want to add");
        addButton.setTooltip(addTooltip);

        editButton = new Button("Edit Product");
        Tooltip editTooltip = new Tooltip("Click on the product you want to edit (edit details in the fields above)");
        editButton.setTooltip(editTooltip);

        viewButton = new Button("View Product");
        Tooltip viewTooltip = new Tooltip("Click on the product you want to see more information about");
        viewButton.setTooltip(viewTooltip);

        listButton = new Button("List Products");
        Tooltip listTooltip = new Tooltip("Click to see all the products");
        listButton.setTooltip(listTooltip);

        saveButton = new Button("Save Product");
        Tooltip saveTooltip = new Tooltip("Click to save the products added");
        saveButton.setTooltip(saveTooltip);

        loadButton = new Button("Load Products");
        Tooltip loadToolTip = new Tooltip("Click to load the products added");
        loadButton.setTooltip(loadToolTip);

        removeButton = new Button("Remove Product");
        Tooltip removeTooltip = new Tooltip("Click on the Product you want to remove");
        removeButton.setTooltip(removeTooltip);

        refreshButton = new Button("Refresh page");
        Tooltip refreshToolTip = new Tooltip("Click to refresh the page");
        refreshButton.setTooltip(refreshToolTip);

        // Set ListView Display
        productListView.setPrefHeight(150);
    }

    public VBox getView() {
        // Create Title
        Label titleLabel = new Label("Product Management");

        // Organize elements horizontally with spacing
        HBox inputBox = new HBox(10, nameField, priceField, stockField, descriptionField);
        HBox productBox = new HBox(10, addButton, editButton, saveButton);
        HBox listBox = new HBox(10, listButton, viewButton, removeButton);
        HBox serializeBox = new HBox(10, saveButton, loadButton);

        // Organize elements vertically with padding
        VBox root = new VBox(10, titleLabel, inputBox, productBox, productListView, serializeBox, listBox, refreshButton);
        root.setPadding(new javafx.geometry.Insets(10));

        // HBox and Vbox returned as the View
        return root;
    }

    // Getters for UI components
    public Button getAddButton() { return addButton; }
    public Button getEditButton() { return editButton; }
    public Button getViewButton() { return viewButton; }
    public Button getListButton() { return listButton; }
    public Button getRemoveButton() { return removeButton; }
    public Button getSaveButton() { return saveButton; }
    public Button getLoadButton() { return loadButton; }
    public Button getRefreshButton() { return refreshButton; }
    public TextField getNameField() { return nameField; }
    public TextField getPriceField() { return priceField; }
    public TextField getStockField() { return stockField; }
    public TextField getDescriptionField() { return descriptionField; }
    public ListView<Product> getProductListView() { return productListView; }
}
