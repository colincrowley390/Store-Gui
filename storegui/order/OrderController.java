package com.example.storegui.order;

import com.example.storegui.utils.DataManager;
import com.example.storegui.model.StoreData;
import com.example.storegui.customer.Customer;
import com.example.storegui.customer.CustomerController;
import com.example.storegui.product.Product;
import com.example.storegui.product.ProductController;
import com.example.storegui.product.ProductView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Represents the functions of the Order Tab in the store system
public class OrderController {

    // Array to hold the list of order items
    private static ArrayList<Order> orders;

    // Variable for using OrderView class
    private final OrderView view;

    // Constructor
    public OrderController(OrderView view, ArrayList<Customer> customers, ArrayList<Product> products, Stage primaryStage) {
        this.view = view;

        // Initialize orderItems to an empty list if it's null
        if (orders == null) {
            orders = new ArrayList<>();
        }

        // Loading actions
        setupEventHandlers(customers, products);
    }

    // Sets up event handlers for the view components
    private void setupEventHandlers(ArrayList<Customer> customers, ArrayList<Product> products) {
        // Ensure customers and products are not null before using them
        if (customers == null) customers = new ArrayList<>();
        if (products == null) products = new ArrayList<>();

        // Safely populate the dropdowns
        view.getCustomerDropdown().getItems().setAll(customers);
        view.getProductDropdown().getItems().setAll(products);
        view.getOrderDropdown().getItems().setAll(orders);

        // Setup other event handlers
        view.getCreateButton().setOnAction(_ -> createOrder());
        view.getRemoveButton().setOnAction(_ -> removeOrder());
        view.getListButton().setOnAction(_ -> listOrders());

        // Save and load buttons
        view.getSaveButton().setOnAction(_ -> {
            saveOrders();
            showAlert("Orders saved successfully.");
        });
        view.getLoadButton().setOnAction(_ -> {
            loadOrders();
            showAlert("Orders loaded successfully");
        });

        ArrayList<Customer> finalCustomers = customers; // final copy
        ArrayList<Product> finalProducts = products; // final copy

        view.getRefreshButton().setOnAction(_ -> refreshView(finalCustomers, finalProducts));

        // Sort orders by date or product name
        view.getSortButton().setOnAction(_ -> sortOrders());

        // Filter orders by customer and month
        view.getFilterButton().setOnAction(_ -> filterOrders());
        view.getFilterCustomerDropdown().getItems().setAll(customers);
    }

    // Creates a new order
    public void createOrder() {
        Customer customer = view.getCustomerDropdown().getValue();
        Product product = view.getProductDropdown().getValue();
        String quantityText = view.getQuantityField().getText();
        LocalDate date = view.getDateField().getValue();

        // Validate input
        if (customer == null || product == null || quantityText.isEmpty() || date == null) {
            System.out.println("Failed to create order: Invalid input");
            showAlert("Please select a customer, a product, a quantity and a date.");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                System.out.println("Failed to create order: Invalid quantity");
                showAlert("Enter a valid quantity.");
                return;
            }

            // Create ProductView instance
            ProductView productView = new ProductView();
            Stage primaryStage = new Stage();
            ProductController productController = new ProductController(productView, primaryStage);

            // Check for sufficient stock of the product
            if (!productController.sufficientStock(product, quantity)) {
                System.out.println("Failed to create order: Insufficient stock for " + product.getName());
                showAlert("Insufficient stock for " + product.getName());
                return;
            }

            // Reduce stock
            productController.reduceStock(product, quantity);

            // Add the order to the list
            Order newOrder = new Order(customer, product, quantity, date);
            orders.add(newOrder);

            // Update the dropdown to include the new order
            view.getOrderDropdown().getItems().setAll(orders);

            // Calculate the total cost and append to the summary for only the new order
            double totalCost = newOrder.getProduct().getPrice() * newOrder.getQuantity();
            String summary = "Order Summary:\n" + newOrder.getCustomer().getName() + " - " + newOrder.getProduct().getName() + " x" + newOrder.getQuantity() + " - €" + totalCost + "\n" + "Total Cost: €" + totalCost;

            // Display the order summary
            showAlert(summary);

            // Clear input fields after adding
            view.getQuantityField().clear();
            view.getDateField().setValue(null);

        } catch (NumberFormatException e) {
            System.out.println("Failed to create order: Quantity must be a number");
            showAlert("Quantity must be a number.");
        }
    }


    // Removes an order from the list
    private void removeOrder() {
        // Get the selected order from the dropdown
        Order selectedOrder = view.getOrderDropdown().getValue();

        // Ensure an order is selected
        if (selectedOrder == null) {
            showAlert("Please select an order to remove.");
            return;
        }
        // confirmation popup
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Removal Confirmation");
        alert.setHeaderText("Are you sure you want to remove this order?");

        // show order details
        alert.setContentText(selectedOrder.toString());

        // two options
        ButtonType removeYes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType removeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(removeYes, removeNo);

        alert.showAndWait().ifPresent(choice -> {
            if (choice == removeYes) {

                // Remove the selected order from the orderItems list
                orders.remove(selectedOrder);

                // Save the updated list of orders
                saveOrders();

                // Update the dropdown
                view.getOrderDropdown().getItems().setAll(orders);

                // Clear the dropdown selection and update the dropdown list
                view.getOrderDropdown().getSelectionModel().clearSelection();
                view.getOrderDropdown().getItems().setAll(orders);

                // Clear input fields
                view.getQuantityField().clear();
                view.getDateField().setValue(null);
            }
        });
    }

    // Lists all current orders
    private void listOrders() {
        if (orders.isEmpty()) {
            view.getOrderSummaryArea().setText("No orders have been created.");
            return;
        }

        StringBuilder summary = new StringBuilder("Current Orders:\n");

        // Build a summary of each order
        for (Order item : orders) {
            double cost = item.getProduct().getPrice() * item.getQuantity();
            summary.append(item.getCustomer())
                    .append(" - ")
                    .append(item.getProduct().getName())
                    .append(" x")
                    .append(item.getQuantity())
                    .append(" (")
                    .append(item.getDate())
                    .append(") - €")
                    .append(cost)
                    .append("\n");
        }

        // Display the summary of orders
        view.getOrderSummaryArea().setText(summary.toString());
    }

    // Saves the current orders to a file
    private void saveOrders() {
        StoreData storeData = new StoreData();
        storeData.setCustomers(CustomerController.getCustomers());
        storeData.setProducts(ProductController.getProducts());
        storeData.setOrders(orders);

        DataManager.getInstance().saveAllDataInThread(storeData,
                () -> showAlert("All data saved successfully."),
                () -> showAlert("Error saving all data.")
        );
    }

    // Loads orders from the file
    private void loadOrders() {
        DataManager.getInstance().loadAllDataInThread(storeData -> {
            orders = storeData.getOrders();
            CustomerController.setCustomers(storeData.getCustomers());
            ProductController.setProducts(storeData.getProducts());
            listOrders();
            showAlert("All data loaded successfully.");
        }, () -> showAlert("Error loading all data."));
    }

    // Sorting Orders
    void sortOrders() {
        boolean sortByDate = view.getSortByDate().isSelected();

        if (sortByDate) {
            orders.sort(Comparator.comparing(Order::getDate));  // Sort by date
        } else {
            orders.sort(Comparator.comparing(o -> o.getProduct().getName())); // Sort by product name
        }

        listOrders(); // Refresh the list
    }

    // Method to retrieve all orders
    public static ArrayList<Order> getOrders() {
        return orders;
    }

    // Filter orders by customer and month
    private void filterOrders() {
        Customer selectedCustomer = view.getFilterCustomerDropdown().getValue();
        Month selectedMonth = view.getFilterMonthDropdown().getValue();

        // Validate input
        if (selectedCustomer == null || selectedMonth == null) {
            showAlert("Please select a customer and a month to filter.");
            return;
        }
        // Filter orders based on selected customer and month
        List<Order> filteredOrders = orders.stream()
                .filter(order -> {
                    // Check if the order's customer and month match the selected values
                    boolean customerMatch = order.getCustomer().equals(selectedCustomer);
                    boolean monthMatch = order.getDate().getMonth() == selectedMonth;
                    return customerMatch && monthMatch;
                })
                .toList();

        if (filteredOrders.isEmpty()) {
            // No orders found for the selected customer and month
            view.getOrderSummaryArea().setText("No orders found for " + selectedCustomer.getName() + " in " + selectedMonth);
        } else {
            // Build a summary of the filtered orders
            StringBuilder summary = new StringBuilder("Orders for " + selectedCustomer.getName() + " in " + selectedMonth + ":\n");
            for (Order order : filteredOrders) {
                double cost = order.getProduct().getPrice() * order.getQuantity();
                summary.append(order.getProduct().getName()).append(" x").append(order.getQuantity())
                        .append(" - €").append(cost).append("\n");
            }
            view.getOrderSummaryArea().setText(summary.toString());
        }
    }

    // Reload Customer and Product Lists after being added
    private void refreshView(ArrayList<Customer> customers, ArrayList<Product> products) {
        setupEventHandlers(customers, products);
    }

    // Displays an alert with a given message
    public void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    // Method to help load orders
    public static void setOrders(ArrayList<Order> orders) {
        OrderController.orders = orders;
    }
}

