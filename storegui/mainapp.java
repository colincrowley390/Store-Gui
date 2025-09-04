package com.example.storegui;

import com.example.storegui.customer.CustomerController;
import com.example.storegui.customer.CustomerView;
import com.example.storegui.database.DatabaseConnection;
import com.example.storegui.order.OrderController;
import com.example.storegui.order.OrderView;
import com.example.storegui.product.ProductController;
import com.example.storegui.product.ProductView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Main application class for the Store GUI.
 * This class initializes the JavaFX application and sets up the main window with tabs for Customers, Products, and Orders.
 */
public class mainapp extends Application {

    // Launch the application
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize the database connection
        try {
            DatabaseConnection.getInstance().getConnection();
            System.out.println("Database connection established successfully.");
        } catch (RuntimeException e) {
            System.err.println("Failed to establish database connection: " + e.getMessage());
            return; // Exit the application if the database connection fails
        }

        // Create a TabPane to hold different tabs for Customer, Product, and Order
        TabPane tabPane = new TabPane();

        // Create the Customer View and Controller, and add it as a tab in the TabPane
        CustomerView CustomerView = new CustomerView();
        CustomerController customerController = new CustomerController(CustomerView, primaryStage);
        Tab customerTab = new Tab("Customers", CustomerView.getView());
        // Disable closing the Customer tab
        customerTab.setClosable(false);

        // Create the Product View and Controller, and add it as a tab in the TabPane
        ProductView productView = new ProductView();
        ProductController productController = new ProductController(productView, primaryStage);
        Tab productTab = new Tab("Products", productView.getView());
        // Disable closing the Product tab
        productTab.setClosable(false);

        // Create the Order View and Controller, and add it as a tab in the TabPane
        OrderView orderView = new OrderView();
        OrderController orderController = new OrderController(orderView, CustomerController.getCustomers(), ProductController.getProducts(), primaryStage);
        Tab orderTab = new Tab("Orders", orderView.getView());
        // Disable closing the Order tab
        orderTab.setClosable(false);

        // Add all the tabs to the TabPane
        tabPane.getTabs().addAll(customerTab, orderTab, productTab);

        // Scene Setup
        Scene scene = new Scene(tabPane, 700, 450);

        // Linking css file
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/styles.css")).toExternalForm());

        // Set the title of the primary stage and add the scene to it
        primaryStage.setTitle("Store");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
