package com.example.storegui.customer;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;

class CustomerControllerTest {
    private CustomerController controller;
    private CustomerView view;
    private Stage primaryStage;

    @BeforeAll
    static void setUpApplication() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    @BeforeEach
    void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            primaryStage = new Stage(); // Create a dummy Stage
            latch.countDown();
        });
        latch.await();
        view = new CustomerView();
        controller = new CustomerController(view, primaryStage);
        controller.getCustomers().clear(); // Clear the customer list to ensure a clean state
    }

    @Test
    void testAddCustomer_success() {
        view.getNameField().setText("John Doe");
        view.getEmailField().setText("john@example.com");
        view.getDobField().setValue(LocalDate.of(1990, 1, 1));

        controller.addCustomer();

        assertEquals(1, controller.getCustomers().size(), "Customer list should contain one customer.");
        Customer addedCustomer = controller.getCustomers().get(0);
        assertEquals("John Doe", addedCustomer.getName(), "Customer name should be 'John Doe'.");
        assertEquals("john@example.com", addedCustomer.getEmail(), "Customer email should be 'john@example.com'.");
        assertEquals(LocalDate.of(1990, 1, 1), addedCustomer.getDob(), "Customer DOB should be 1990-01-01.");
    }

    @Test
    void testAddCustomer_withInvalidName() {
        view.getNameField().setText(""); // Invalid name
        view.getEmailField().setText("john@example.com");
        view.getDobField().setValue(LocalDate.of(1990, 1, 1));

        controller.addCustomer();

        // Assuming the controller handles invalid names
        assertEquals(0, controller.getCustomers().size(), "Customer list should be empty.");
    }
}