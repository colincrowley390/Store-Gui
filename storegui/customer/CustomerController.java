package com.example.storegui.customer;

import com.example.storegui.utils.DataManager;
import com.example.storegui.model.StoreData;
import com.example.storegui.order.OrderController;
import com.example.storegui.product.ProductController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

// Represents the functions of the Customer Tab in the store system
public class CustomerController {

    // Array to hold the list of customers
    private static ArrayList<Customer> customerList = new ArrayList<>();

    // Variable for using CustomerView class
    private final CustomerView view;

    public CustomerController(CustomerView view, Stage primaryStage) {

        // CustomerView class
        this.view = view;

        // loading actions
        updateDisplay();
        setupEventHandlers();
        setupExitHandler(primaryStage);
    }

    // adding a customer
    void addCustomer() {
        // retrieves all text from input fields
        String name = view.getNameField().getText();
        String email = view.getEmailField().getText();
        LocalDate dob = view.getDobField().getValue();

        // if all fields are filled
        if (!name.isEmpty() && !email.isEmpty() && dob != null) {
            try {
                Customer customer = new Customer.Builder()
                        .setName(name)
                        .setEmail(email)
                        .setDob(dob)
                        .build();
                customerList.add(customer);

                // clear all the fields
                view.getNameField().clear();
                view.getEmailField().clear();
                view.getDobField().setValue(null);

                updateDisplay();
            } catch (IllegalArgumentException e) {
                // handle validation errors from the Builder
                showAlert(e.getMessage());
            }
        } else {
            // if any field is empty, show a warning
            showAlert("Please fill in all fields.");
        }
    }

    // save customer list to a file
    private void saveCustomers() {
        StoreData storeData = new StoreData(); // create a new StoreData object
        storeData.setCustomers(customerList); // set the customer list
        storeData.setProducts(ProductController.getProducts()); // set the product list
        storeData.setOrders(OrderController.getOrders()); // set the order list

        // save the data in a separate thread
        DataManager.getInstance().saveAllDataInThread(storeData,
                () -> showAlert("All data saved successfully."),
                () -> showAlert("Error saving all data.")
        );
    }

    // load customer list to a file
    private void loadCustomers() {
        DataManager.getInstance().loadAllDataInThread(storeData -> {
            customerList = storeData.getCustomers();
            ProductController.setProducts(storeData.getProducts());
            OrderController.setOrders(storeData.getOrders());
            updateDisplay();
            showAlert("All data loaded successfully.");
        }, () -> showAlert("Error loading all data."));
    }

    // save customer to database
    private void saveCustomerToDatabase(Customer customer) {
        try {
            CustomerDAO.saveCustomer(customer);
            showAlert("Customer saved successfully.");
        } catch (SQLException e) {
            showAlert("Error saving customer: " + e.getMessage());
        }
    }

    // load customers from database
    private void loadCustomersFromDatabase() {
        try {
            // clear the current list
            customerList.clear();
            // load customers from the database
            customerList.addAll(CustomerDAO.loadAllCustomers());
            updateDisplay();
            showAlert("Customers loaded successfully from the database.");
        } catch (SQLException e) {
            showAlert("Error loading customers: " + e.getMessage());
        }
    }

    // confirmation popup for removing a customer
    private void confirmRemove() {
        // get selected customer
        Customer selected = view.getCustomerListView().getSelectionModel().getSelectedItem();

        // if nothing is selected, show a warning
        if (selected == null) {
            showAlert("Please select a customer to remove.");
            return;
        }

        // confirmation popup
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Removal Confirmation");
        alert.setHeaderText("Are you sure you want to remove this customer?");
        // show customer details
        alert.setContentText(selected.toString());

        // two options
        ButtonType removeYes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType removeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(removeYes, removeNo);

        alert.showAndWait().ifPresent(choice -> {
            if (choice == removeYes) {
                customerList.remove(selected);
                updateDisplay();
            }
        });
    }

    // confirmation popup for exiting the application
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
                saveCustomers();
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
    private void setupEventHandlers() {
        view.getAddButton().setOnAction(_ -> addCustomer());
        view.getListButton().setOnAction(_ -> updateDisplay());
        view.getSaveButton().setOnAction(_ -> {
            saveCustomers();
            showAlert("Customers saved successfully.");
        });
        view.getLoadButton().setOnAction(_ -> {
            loadCustomers();
            showAlert("Customers loaded successfully");
        });
        view.getRemoveButton().setOnAction(_ -> confirmRemove());

        view.getSaveToDatabase().setOnAction(_ -> {
            for (Customer customer : customerList) {
                saveCustomerToDatabase(customer);
            }
        });
        view.getLoadFromDatabase().setOnAction(_ -> loadCustomersFromDatabase());
    }

    // setting up the exit handler
    private void setupExitHandler(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            confirmExit(primaryStage);
        });
    }

    // clears text area and appends all customer details
    private void updateDisplay() {
        view.getCustomerListView().getItems().setAll(customerList);
    }

    // returning the finished list of customers
    public static ArrayList<Customer> getCustomers() {
        return customerList;
    }

    // method to help load customers
    public static void setCustomers(ArrayList<Customer> customers) {
        customerList = customers;
    }
}
