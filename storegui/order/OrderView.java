package com.example.storegui.order;

import com.example.storegui.customer.Customer;
import com.example.storegui.product.Product;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.Month;

// Represents the UI of the Order Tab in the store system
public class OrderView {

    // Dropdown lists for Customers, Products and Orders
    private final ComboBox<Customer> customerDropdown;
    private final ComboBox<Product> productDropdown;
    private final ComboBox<Order> orderDropdown;

    // Dropdowns for filtering
    private final ComboBox<Customer> filterCustomerDropdown;
    private final ComboBox<Month> filterMonthDropdown;
    private final Button filterButton;

    // Same fields as Order
    private final TextField quantityField;
    private final DatePicker dateField;

    // Text area to display order
    private final TextArea orderSummaryArea;

    // Buttons for Creating, Removing, Listing, Saving and Refreshing orders
    private final Button createButton, removeButton, listButton, saveButton, loadButton, refreshButton;

    // Buttons for Sorting
    private final RadioButton sortByDate;
    private final Button sortButton;

    public OrderView() {
        // UI Elements

        // Setting prompts and buttons
        customerDropdown = new ComboBox<>();
        customerDropdown.setPromptText("Select Customer");

        productDropdown = new ComboBox<>();
        productDropdown.setPromptText("Select Product");

        orderDropdown = new ComboBox<>();
        orderDropdown.setPromptText("Select Order");

        quantityField = new TextField();
        quantityField.setPromptText("Enter Quantity");

        dateField = new DatePicker();
        dateField.setPromptText("Enter Date");

        orderSummaryArea = new TextArea();
        orderSummaryArea.setEditable(false);

        createButton = new Button("Create Order");
        Tooltip createTooltip = new Tooltip("Fill in the details for the Order you want to create");
        createButton.setTooltip(createTooltip);

        removeButton = new Button("Remove Order");
        Tooltip removeTooltip = new Tooltip("Pick the Order you want to remove");
        removeButton.setTooltip(removeTooltip);

        listButton = new Button("List Order");
        Tooltip listTooltip = new Tooltip("Click to see all the orders");
        listButton.setTooltip(listTooltip);

        saveButton = new Button("Save Order");
        Tooltip saveTooltip = new Tooltip("Click to save the orders added");
        saveButton.setTooltip(saveTooltip);

        loadButton = new Button("Load Orders");
        Tooltip loadToolTip = new Tooltip("Click to load the orders added");
        loadButton.setTooltip(loadToolTip);

        refreshButton = new Button("Refresh page");
        Tooltip refreshToolTip = new Tooltip("Click to refresh the page");
        refreshButton.setTooltip(refreshToolTip);

        // Sorting buttons
        ToggleGroup sortGroup = new ToggleGroup();
        sortByDate = new RadioButton("Sort by Date");
        sortByDate.setToggleGroup(sortGroup);
        sortByDate.setSelected(true);

        RadioButton sortByProduct = new RadioButton("Sort by Product");
        sortByProduct.setToggleGroup(sortGroup);

        sortButton = new Button("Sort Orders");
        Tooltip sortTooltip = new Tooltip("Click to sort orders");
        listButton.setTooltip(sortTooltip);

        // UI elements for filtering
        filterCustomerDropdown = new ComboBox<>();
        filterCustomerDropdown.setPromptText("Filter by Customer");

        filterMonthDropdown = new ComboBox<>();
        filterMonthDropdown.setPromptText("Filter by Month");
        filterMonthDropdown.getItems().addAll(Month.values()); // Populate with months

        filterButton = new Button("Filter Orders");
        Tooltip filterTooltip = new Tooltip("Click to filter orders");
        listButton.setTooltip(filterTooltip);

        // Title Label
        Label titleLabel = new Label("Order Management");

        // Organize elements horizontally with spacing
        HBox input = new HBox(10, customerDropdown, productDropdown, quantityField, dateField);
        HBox createBox = new HBox (10, createButton, listButton);
        HBox serializeBox = new HBox(10, saveButton, loadButton);
        HBox sortingOptions = new HBox(10, sortByDate, sortByProduct, sortButton);
        HBox filterOptions = new HBox(10, filterCustomerDropdown, filterMonthDropdown, filterButton);
        HBox removeBox = new HBox (10, orderDropdown, removeButton);

        // Organize elements vertically with padding
        VBox layout = new VBox(10, titleLabel, input, createBox, orderSummaryArea, serializeBox, sortingOptions, filterOptions, removeBox, refreshButton);
        layout.setPadding(new javafx.geometry.Insets(10));

        // Passing the Layout variable into the view to be edited elsewhere
        this.view = layout;
    }

    // Layout
    private final VBox view;

    // Getters for UI components
    public VBox getView() { return view; }
    public ComboBox<Customer> getCustomerDropdown() { return customerDropdown; }
    public ComboBox<Product> getProductDropdown() { return productDropdown; }
    public ComboBox<Order> getOrderDropdown() { return orderDropdown; }
    public TextField getQuantityField() { return quantityField; }
    public DatePicker getDateField() { return dateField; }
    public TextArea getOrderSummaryArea() { return orderSummaryArea; }
    public Button getCreateButton() { return createButton; }
    public Button getRemoveButton() { return removeButton; }
    public Button getListButton() { return listButton; }
    public Button getSaveButton() { return saveButton; }
    public Button getRefreshButton() { return refreshButton; }
    public Button getLoadButton() { return loadButton; }
    public Button getSortButton() { return sortButton; }
    public RadioButton getSortByDate() { return sortByDate; }
    public ComboBox<Customer> getFilterCustomerDropdown() { return filterCustomerDropdown; }
    public ComboBox<Month> getFilterMonthDropdown() { return filterMonthDropdown; }
    public Button getFilterButton() { return filterButton; }
}