package com.example.storegui.product;

import com.example.storegui.utils.DataManager;
import com.example.storegui.model.StoreData;
import com.example.storegui.customer.CustomerController;
import com.example.storegui.order.OrderController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.ArrayList;

// Represents the functions of the Product Tab in the store system
public class ProductController {

    // array to hold the list of products
    private static ArrayList<Product> productList = new ArrayList<>();

    // variable for using ProductView class
    private final ProductView view;

    public ProductController(ProductView view, Stage primaryStage) {

        // ProductView class
        this.view = view;

        // loading actions
        updateDisplay();
        setupEventHandlers(productList);
        setupExitHandler(primaryStage);
    }

    // adding a product
    private void addProduct() {
        String name = view.getNameField().getText();
        String priceText = view.getPriceField().getText();
        String stockText = view.getStockField().getText();
        String description = view.getDescriptionField().getText();

        // if all fields are filled
        if (!name.isEmpty() && !priceText.isEmpty() && !stockText.isEmpty() && !description.isEmpty()) {
            try {
                // add a new product to list
                double price = Double.parseDouble(priceText);
                int stock = Integer.parseInt(stockText);
                productList.add(new Product(name, price, stock, description));

                // and then clears all the fields
                view.getNameField().clear();
                view.getPriceField().clear();
                view.getStockField().clear();
                view.getDescriptionField().clear();

            } catch (NumberFormatException e) {
                // make sure right characters are input
                showAlert("Price and Stock must be numeric.");
            }
        } else {
            // if any field is empty, show a warning
            showAlert("Please fill in all fields.");
        }
    }

    // saving product list to a file
    private void saveProducts() {
        StoreData storeData = new StoreData();
        storeData.setCustomers(CustomerController.getCustomers());
        storeData.setProducts(productList);
        storeData.setOrders(OrderController.getOrders());

        DataManager.getInstance().saveAllDataInThread(storeData,
                () -> showAlert("All data saved successfully."),
                () -> showAlert("Error saving all data.")
        );
    }

    // loading product list from a file
    private void loadProducts() {
        DataManager.getInstance().loadAllDataInThread(storeData -> {
            productList = storeData.getProducts();
            CustomerController.setCustomers(storeData.getCustomers());
            OrderController.setOrders(storeData.getOrders());
            updateDisplay();
            showAlert("All data loaded successfully.");
        }, () -> showAlert("Error loading all data."));
    }


    // changing details of a product
    private void editProduct() {
        // Get the selected product from the list
        Product selected = view.getProductListView().getSelectionModel().getSelectedItem();

        // If no product is selected, show a warning
        if (selected == null) {
            showAlert("Select a product to edit.");
            return;
        }

        // Get new values from input fields
        String newName = view.getNameField().getText().trim();
        String priceText = view.getPriceField().getText().trim();
        String stockText = view.getStockField().getText().trim();
        String newDescription = view.getDescriptionField().getText().trim();

        // Ensure name is not empty
        if (newName.isEmpty()) {
            showAlert("Product name cannot be empty.");
            return;
        }

        // Ensure price and stock are valid numbers
        try {
            double newPrice = Double.parseDouble(priceText);
            int newStock = Integer.parseInt(stockText);

            // Update product details
            selected.setName(newName);
            selected.setPrice(newPrice);
            selected.setStock(newStock);
            selected.setDescription(newDescription);

            // Refresh UI
            updateDisplay();
            saveProducts();

            // and then clears all the fields
            view.getNameField().clear();
            view.getPriceField().clear();
            view.getStockField().clear();
            view.getDescriptionField().clear();

        } catch (NumberFormatException e) {
            showAlert("Enter valid numbers for price and stock.");
        }
    }

    // removing a product
    private void removeProduct() {
        // Get the selected product from the list
        Product selected = view.getProductListView().getSelectionModel().getSelectedItem();

        // If no product is selected, show a warning
        if (selected == null) {
            showAlert("Please select a product to remove.");
            return;
        }

        // Confirmation popup
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Product");
        alert.setHeaderText("Are you sure you want to remove this product?");
        alert.setContentText(selected.getName() + " (€" + selected.getPrice() + ")");

        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yes, no);

        alert.showAndWait().ifPresent(choice -> {
            if (choice == yes) {
                productList.remove(selected); // Remove from list
                updateDisplay(); // Refresh UI
                saveProducts();}
        });
    }

    // viewing a product
    private void viewProduct() {
        Product selected = view.getProductListView().getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Select a product to view.");
            return;
        }

        // Display product details, including the description
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Product Details");
        alert.setHeaderText(selected.getName());
        alert.setContentText(
                "Price: €" + selected.getPrice() +
                        "\nStock: " + selected.getStock() +
                        "\nDescription: " + selected.getDescription() // Show description only here
        );
        alert.showAndWait();
    }

    // confirmation popup for closing the window
    private void confirmExit(Stage stage) {
        // confirmation popup
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Do you want to save before exiting?");

        // three options
        ButtonType saveAndExit = new ButtonType("Save & Exit");
        ButtonType exitWithoutSaving = new ButtonType("Exit Without Saving");
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        // getting all the buttons
        alert.getButtonTypes().setAll(saveAndExit, exitWithoutSaving, cancel);

        alert.showAndWait().ifPresent(choice -> {
            if (choice == saveAndExit) {
                // first saves then closes
                saveProducts();
                stage.close();
            } else if (choice == exitWithoutSaving) {
                // closes without saving
                stage.close();
            }
            // if Cancel is chosen the window stays open
        });
    }

    // displays warning when an issue occurs
    public void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    // assigning an action to each button
    private void setupEventHandlers(ArrayList<Product> products) {
        view.getAddButton().setOnAction(_ -> addProduct());
        view.getListButton().setOnAction(_ -> updateDisplay());
        view.getEditButton().setOnAction(_ -> editProduct());
        view.getViewButton().setOnAction(_ -> viewProduct());
        view.getRemoveButton().setOnAction(_ -> removeProduct());
        view.getRefreshButton().setOnAction(_ -> refreshView(products));
        view.getSaveButton().setOnAction(_ -> {
            saveProducts();
            showAlert("Products saved successfully.");
        });
        view.getLoadButton().setOnAction(_ -> {
            loadProducts();
            showAlert("Products loaded successfully");
        });
    }

    // setting up the exit handler
    private void setupExitHandler(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            confirmExit(primaryStage);
        });
    }

    // clears text area and appends all product details
    private void updateDisplay() {
        view.getProductListView().getItems().setAll(productList);
    }

    // returning the finished list of products
    public static ArrayList<Product> getProducts() {
        return productList;
    }

    // Reduces stock for a product if enough stock is available
    public void reduceStock(Product product, int amount) {
        if (sufficientStock(product, amount)) {
            product.setStock(product.getStock() - amount);
            saveProducts(); // Save changes after reducing stock
        } else {
            showAlert("Insufficient stock for " + product.getName());
        }
    }

    // Reload Customer and Product Lists after being added
    private void refreshView(ArrayList<Product> products) {
        setupEventHandlers(products);
    }

    // Checks if there is enough stock for the requested amount
    public boolean sufficientStock(Product product, int amount) {
        return product.getStock() >= amount;
    }

    // Method to help load products
    public static void setProducts(ArrayList<Product> products) {
        productList = products;
    }
}