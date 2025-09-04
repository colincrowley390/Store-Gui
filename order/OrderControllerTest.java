package com.example.storegui.order;

import com.example.storegui.customer.Customer;
import com.example.storegui.product.Product;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

// Test class for OrderController
class OrderControllerTest {
    private OrderController controller;
    private OrderView view;
    private Stage primaryStage;
    private ArrayList<Customer> customers;
    private ArrayList<Product> products;

    // This method is called once before all tests in this class
    @BeforeAll
    static void setUpApplication() throws InterruptedException {
        // Initialize JavaFX application thread
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    // This method is called before each test
    @BeforeEach
    void setUp() throws InterruptedException {
        // Create a new Stage for each test
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            primaryStage = new Stage(); // Create a dummy Stage
            latch.countDown();
        });
        latch.await();

        // Initialize the OrderView
        view = new OrderView();

        // Initialize ArrayLists
        customers = new ArrayList<>();
        products = new ArrayList<>();

        // Initialize controller with the ArrayLists
        controller = new OrderController(view, customers, products, primaryStage);

        // Clear the orders to ensure a clean state for each test
        OrderController.getOrders().clear();
    }

    // Test to check if the OrderController is initialized correctly
    @Test
    void testAddOrder_success() throws InterruptedException {
        // Set up the customers and products lists
        Customer testCustomer = new Customer.Builder()
                .setName("Test Customer")
                .setEmail("test@customer.com")
                .setDob(LocalDate.of(1985, 5, 15))
                .build();
        Product testProduct = new Product("Test Product", 10.0, 100, "Test Description");
        customers.add(testCustomer);
        products.add(testProduct);

        // Setup input data as if provided by the user
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            view.getCustomerDropdown().setValue(testCustomer);
            view.getProductDropdown().setValue(testProduct);
            view.getQuantityField().setText("1");
            view.getDateField().setValue(LocalDate.now());

            // Call the method to create the order
            controller.createOrder();
            latch.countDown();
        });
        latch.await();

        // Wait for the FX thread to process the order creation
        CountDownLatch fxLatch = new CountDownLatch(1);
        Platform.runLater(fxLatch::countDown);
        fxLatch.await();

        // Assert that the orderItems list contains one order
        assertEquals(1, OrderController.getOrders().size(), "Order list should contain one order.");
    }

    // Test to check if the OrderController handles invalid input correctly
    @Test
    void testSortOrdersByDate() throws InterruptedException {
        // Set up the customers and products lists
        Customer customer1 = new Customer.Builder()
                .setName("John Doe")
                .setEmail("john@example.com")
                .setDob(LocalDate.of(1990, 1, 1))
                .build();
        Customer customer2 = new Customer.Builder()
                .setName("Jane Smith")
                .setEmail("jane@example.com")
                .setDob(LocalDate.of(1985, 5, 15))
                .build();
        Product product = new Product("Product A", 10.0, 100, "Amazing Product");

        // Add customers and products to the lists
        customers.add(customer1);
        customers.add(customer2);
        products.add(product);

        // Setup input data as if provided by the user
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            view.getCustomerDropdown().getItems().setAll(customers);
            view.getProductDropdown().getItems().setAll(products);

            view.getCustomerDropdown().setValue(customer1);
            view.getProductDropdown().setValue(product);
            view.getQuantityField().setText("2");
            view.getDateField().setValue(LocalDate.of(2023, Month.JANUARY, 1));
            controller.createOrder();

            view.getCustomerDropdown().setValue(customer2);
            view.getProductDropdown().setValue(product);
            view.getQuantityField().setText("3");
            view.getDateField().setValue(LocalDate.of(2023, Month.FEBRUARY, 1));
            controller.createOrder();
            latch.countDown();
        });
        latch.await();

        // Wait for the FX thread to process the order creation
        CountDownLatch fxLatch = new CountDownLatch(1);
        Platform.runLater(fxLatch::countDown);
        fxLatch.await();

        // Ensure orders are created before sorting
        assertEquals(2, OrderController.getOrders().size(), "Order list should contain two orders.");

        // Set the sort by date option
        Platform.runLater(() -> view.getSortByDate().setSelected(true));
        fxLatch.await();

        // Call the method to sort orders
        controller.sortOrders();

        // Wait for the FX thread to process the sorting
        assertEquals(LocalDate.of(2023, Month.JANUARY, 1), OrderController.getOrders().get(0).getDate(), "First order should be for '2023-01-01'.");
        assertEquals(LocalDate.of(2023, Month.FEBRUARY, 1), OrderController.getOrders().get(1).getDate(), "Second order should be for '2023-02-01'.");
    }

}