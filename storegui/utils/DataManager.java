package com.example.storegui.utils;

import com.example.storegui.model.StoreData;
import javafx.application.Platform;

import java.io.*;
import java.util.function.Consumer;

/**
 * DataManager is a Singleton class responsible for managing the storage and retrieval of
 * StoreData objects. It provides methods to save and load data from a single unified file.
 */
public class DataManager {
    // Singleton instance
    private static DataManager instance;

    // File path for single unified file
    private static final String STORE_FILE = "storeData.ser";

    // Private constructor to prevent instantiation
    private DataManager() {}

    // Public method to get the Singleton instance
    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    // Save all data at once in a thread
    public void saveAllDataInThread(StoreData storeData, Runnable onSuccess, Runnable onError) {
        new Thread(() -> {
            // Check if the file exists before trying to write to it
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(STORE_FILE))) {
                out.writeObject(storeData);
                if (onSuccess != null) Platform.runLater(onSuccess);
            } catch (IOException e) {
                if (onError != null) Platform.runLater(onError);
            }
        }).start();
    }

    // Load all data at once in a thread
    public void loadAllDataInThread(Consumer<StoreData> onSuccess, Runnable onError) {
        new Thread(() -> {
            // Check if the file exists before trying to read it
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(STORE_FILE))) {
                StoreData storeData = (StoreData) in.readObject();
                if (onSuccess != null) Platform.runLater(() -> onSuccess.accept(storeData));
            } catch (IOException | ClassNotFoundException e) {
                if (onError != null) Platform.runLater(onError);
            }
        }).start();
    }
}